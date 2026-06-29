package com.querybricks.table;

import com.querybricks.query.Query;

public final class SubqueryTable<T extends FilterableTable> implements WrappedTable<T>, FilterableTable {
    private final T table;
    private final Query query;

    public SubqueryTable(T table, Query query) {
        this.table = table;
        this.query = query;
    }

    @Override
    public T origin() {
        return this.table;
    }

    @Override
    public String sql() {
        return String.format("(%s)", this.query.sql());
    }
}
