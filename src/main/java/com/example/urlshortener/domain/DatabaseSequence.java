package com.example.urlshortener.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counters")
public class DatabaseSequence {
    @Id
    private String id;
    private long seq;

    public long getSeq() {
        return seq;
    }
}
