package com.querybricks.column;

import com.querybricks.Bindings;

/**
 * Represents the sum aggregate function applied to a column.
 *
 * @param <T> the Java type representing the value type of the column
 */
public class Sum<T> implements AggregatedColumn<T> {
    private final Column<T> column;

    /**
     * Constructs a Sum function for the specified column.
     *
     * @param column the column to aggregate
     */
    public Sum(Column<T> column) {
        this.column = column;
    }

    @Override
    public String sql() {
        return String.format("SUM(%s)", this.column.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.column.bind(bindings);
    }
}
