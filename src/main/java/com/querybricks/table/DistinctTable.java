package com.querybricks.table;

import com.querybricks.Bindings;

public final class DistinctTable<T extends Table> implements WrappedTable<T> {
    private final T table;

    public DistinctTable(T table) {
        this.table = table;
    }

    @Override
    public T origin() {
        return this.table;
    }

    @Override
    public String sql() {
        return String.format("DISTINCT %s", this.table.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.table.bind(bindings);
    }
}
