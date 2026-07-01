package com.querybricks.table;

import com.querybricks.Bindings;
import com.querybricks.condition.Condition;

public class HavingTable<T extends HavableTable> implements WrappedTable<T> {
    private final T table;
    private final Condition condition;

    public HavingTable(T table, Condition condition) {
        this.table = table;
        this.condition = condition;
    }

    @Override
    public String sql() {
        return String.format("%s HAVING %s", table.sql(), condition.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.condition.bind(this.table.bind(bindings));
    }

    @Override
    public T origin() {
        return table;
    }
}
