package com.querybricks.column;

import com.querybricks.Bindings;
import com.querybricks.table.Table;

/**
 * Represents a column associated with a specific database table.
 *
 * @param <T> the Java type representing the value type of the column
 */
public class TableColumn<T> implements BoundColumn<T> {
    private final Table table;
    private final UnboundColumn<T> column;

    /**
     * Constructs a TableColumn for the specified table and unbound column.
     *
     * @param table the table containing the column
     * @param column the unbound representation of the column
     */
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
