package com.querybricks.column;

import com.querybricks.Bindings;

public class Count<T> implements AggregatedColumn<T> {
    private final Column<T> column;

    public Count(Column<T> column) {
        this.column = column;
    }

    @Override
    public String sql() {
        return String.format("COUNT(%s)", this.column.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.column.bind(bindings);
    }
}
