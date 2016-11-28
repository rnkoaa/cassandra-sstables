package com.richard.cassandra.sstable;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created on 11/28/2016.
 */
@Component
public class Runner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        //SimpleBulkLoader.run(new String[]{"GOOG", "YHOO", "ORCL"});
        new ContactsBulkLoader().load();
    }
}
