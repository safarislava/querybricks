package com.querybricks.column;

import com.querybricks.Bindings;

public class Avg<T> implements AggregatedColumn<T> {
    private final Column<T> column;

    public Avg(Column<T> column) {
        this.column = column;
    }

    @Override
    public String sql() {
        return String.format("AVG(%s)", this.column.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.column.bind(bindings);
    }
}
