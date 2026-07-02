package com.querybricks.column;

import com.querybricks.Bindings;

/**
 * Represents the count aggregate function applied to a column.
 *
 * @param <T> the Java type representing the value type of the column
 */
public class Count<T> implements AggregatedColumn<T> {
    private final Column<T> column;

    /**
     * Constructs a Count function for the specified column.
     *
     * @param column the column to aggregate
     */
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
