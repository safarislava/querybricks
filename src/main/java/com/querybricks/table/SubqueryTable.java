package com.querybricks.table;

import com.querybricks.Bindings;
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

    @Override
    public Bindings bind(Bindings bindings) {
        return this.query.bind(bindings);
    }
}
