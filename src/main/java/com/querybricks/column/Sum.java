package com.querybricks.column;

import com.querybricks.Bindings;

public class Sum<T> implements AggregatedColumn<T> {
    private final Column<T> column;

    public Sum(Column<T> column) {
        this.column = column;
    }

    @Override
    public String sql() {
        return String.format("SUM(%s)", this.column.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.column.bind(bindings);
    }
}
