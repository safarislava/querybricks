package com.querybricks.column;

import com.querybricks.Bindings;

public class RawColumn<T> implements UnboundColumn<T> {
    private final String name;

    public RawColumn(String name) {
        this.name = name;
    }

    @Override
    public String sql() {
        return this.name;
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings;
    }
}
