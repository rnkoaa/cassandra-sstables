package com.richard.cassandra.sstable;

import org.apache.cassandra.config.Config;
import org.apache.cassandra.dht.Murmur3Partitioner;
import org.apache.cassandra.exceptions.InvalidRequestException;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created on 11/28/2016.
 */
public class SimpleBulkLoader {
    public static final String CSV_URL = "http://real-chart.finance.yahoo.com/table.csv?s=%s";

    /**
     * Default output directory
     */
    public static final String DEFAULT_OUTPUT_DIR = "data/sstables";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Keyspace name
     */
    public static final String KEYSPACE = "quote";
    /**
     * Table name
     */
    public static final String TABLE = "historical_prices";

    /**
     * Schema for bulk loading table.
     * It is important not to forget adding keyspace name before table name,
     * otherwise CQLSSTableWriter throws exception.
     */
    public static final String SCHEMA = String.format("CREATE TABLE %s.%s (" +
            "ticker ascii, " +
            "date timestamp, " +
            "open decimal, " +
            "high decimal, " +
            "low decimal, " +
            "close decimal, " +
            "volume bigint, " +
            "adj_close decimal, " +
            "PRIMARY KEY (ticker, date) " +
            ") WITH CLUSTERING ORDER BY (date DESC)", KEYSPACE, TABLE);


    /**
     * INSERT statement to bulk load.
     * It is like prepared statement. You fill in place holder for each data.
     */
    public static final String INSERT_STMT = String.format("INSERT INTO %s.%s (" +
            "ticker, date, open, high, low, close, volume, adj_close" +
            ") VALUES (" +
            "?, ?, ?, ?, ?, ?, ?, ?" +
            ")", KEYSPACE, TABLE);

    public static void run(String[] args) {
        if (args.length == 0) {
            System.out.println("usage: java bulkload.BulkLoad <list of ticker symbols>");
            return;
        }

        // magic!
        Config.setClientMode(true);

        // Create output directory that has keyspace and table name in the path
        File outputDir = new File(DEFAULT_OUTPUT_DIR + File.separator + KEYSPACE + File.separator + TABLE);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new RuntimeException("Cannot create output directory: " + outputDir);
        }

        // Prepare SSTable writer
        CQLSSTableWriter.Builder builder = CQLSSTableWriter.builder();
        // set output directory
        builder.inDirectory(outputDir)
               // set target schema
               .forTable(SCHEMA)
               // set CQL statement to put data
               .using(INSERT_STMT)
               // set partitioner if needed
               // default is Murmur3Partitioner so set if you use different one.
               .withPartitioner(new Murmur3Partitioner());
        CQLSSTableWriter writer = builder.build();

        for (String ticker : args) {
            HttpURLConnection conn;
            try {
                URL url = new URL(String.format(CSV_URL, ticker));
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    CsvListReader csvReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE)
            ) {
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    System.out.println("Historical data not found for " + ticker);
                    continue;
                }

                csvReader.getHeader(true);

                // Write to SSTable while reading data
                List<String> line;
                while ((line = csvReader.read()) != null) {
                    // We use Java types here based on
                    // http://www.datastax.com/drivers/java/2.0/com/datastax/driver/core/DataType.Name.html#asJavaClass%28%29
                    writer.addRow(ticker,
                            DATE_FORMAT.parse(line.get(0)),
                            new BigDecimal(line.get(1)),
                            new BigDecimal(line.get(2)),
                            new BigDecimal(line.get(3)),
                            new BigDecimal(line.get(4)),
                            Long.parseLong(line.get(5)),
                            new BigDecimal(line.get(6)));
                }
            } catch (InvalidRequestException | ParseException | IOException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
        } catch (IOException ignore) {
        }
    }
}
