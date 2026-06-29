package com.querybricks.query;

public class FakeQuery implements Query {
    private final String query;

    public FakeQuery(String query) {
        this.query = query;
    }

    @Override
    public String sql() {
        return query;
    }
}
