package com.richard.cassandra.sstable;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created on 11/28/2016.
 */
@Builder
@Value
public class Person {
    private UUID id;
    private Name name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
