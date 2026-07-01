package com.querybricks.table;

import com.querybricks.Bindings;

public final class LimitedTable<T extends Table> implements WrappedTable<T> {
    private final T table;
    private final int limit;

    public LimitedTable(T table, int limit) {
        this.table = table;
        this.limit = limit;
    }

    @Override
    public T origin() {
        return this.table;
    }

    @Override
    public String sql() {
        return String.format("%s LIMIT ?", this.table.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.table.bind(bindings).with(this.limit);
    }
}
