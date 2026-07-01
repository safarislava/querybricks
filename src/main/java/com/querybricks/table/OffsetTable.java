package com.querybricks.table;

import com.querybricks.Bindings;

public final class OffsetTable<T extends Table> implements WrappedTable<T> {
    private final T table;
    private final int offset;

    public OffsetTable(T table, int offset) {
        this.table = table;
        this.offset = offset;
    }

    @Override
    public T origin() {
        return this.table;
    }

    @Override
    public String sql() {
        return String.format("%s OFFSET ?", this.table.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.table.bind(bindings).with(this.offset);
    }
}
