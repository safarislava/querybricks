package com.querybricks.query;

import com.querybricks.Bindings;

public class FakeQuery implements Query {
    private final String query;

    public FakeQuery(String query) {
        this.query = query;
    }

    @Override
    public String sql() {
        return query;
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings;
    }
}
