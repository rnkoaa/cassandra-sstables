CREATE TABLE historical_prices (
    ticker ascii,
    date timestamp,
    open decimal,
    high decimal,
    low decimal,
    close decimal,
    volume bigint,
    adj_close decimal,
    PRIMARY KEY (ticker, date)
) WITH CLUSTERING ORDER BY (date DESC);