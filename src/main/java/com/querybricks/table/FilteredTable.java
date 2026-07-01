package com.querybricks.table;

import com.querybricks.Bindings;
import com.querybricks.condition.Condition;

public class FilteredTable<T extends FilterableTable> implements WrappedTable<T> {
    private final T table;
    private final Condition condition;

    public FilteredTable(T table, Condition condition) {
        this.table = table;
        this.condition = condition;
    }

    @Override
    public T origin() {
        return table;
    }

    @Override
    public String sql() {
        return String.format("%s WHERE %s", table.sql(), condition.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.condition.bind(this.table.bind(bindings));
    }
}
