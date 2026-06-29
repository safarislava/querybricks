package com.querybricks.table;

public class FakeFilterableTable implements FilterableTable {
    private final String name;

    public FakeFilterableTable(String name) {
        this.name = name;
    }

    @Override
    public String sql() {
        return this.name;
    }
}
