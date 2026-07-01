package com.querybricks.column;

import com.querybricks.Bindings;
import com.querybricks.table.Table;

public class TableColumn<T> implements BoundColumn<T> {
    private final Table table;
    private final UnboundColumn<T> column;

    public TableColumn(Table table, UnboundColumn<T> column) {
        this.table = table;
        this.column = column;
    }

    @Override
    public String sql() {
        return String.format("%s.%s", this.table.sql(), this.column.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.column.bind(bindings);
    }

    @Override
    public UnboundColumn<T> unbound() {
        return this.column;
    }
}
