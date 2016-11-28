package com.richard.cassandra.sstable;

import lombok.Builder;
import lombok.Value;

/**
 * Created on 11/28/2016.
 */
@Value
@Builder
public class Name {
    private final String lastName;
    private final String firstName;
    private final String middleName;
}
