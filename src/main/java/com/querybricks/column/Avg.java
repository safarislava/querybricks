package com.querybricks.column;

import com.querybricks.Bindings;

/**
 * Represents the average aggregate function applied to a column.
 *
 * @param <T> the Java type representing the value type of the column
 */
public class Avg<T> implements AggregatedColumn<T> {
    private final Column<T> column;

    /**
     * Constructs an Avg function for the specified column.
     *
     * @param column the column to aggregate
     */
    public Avg(Column<T> column) {
        this.column = column;
    }

    @Override
    public String sql() {
        return String.format("AVG(%s)", this.column.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.column.bind(bindings);
    }
}
