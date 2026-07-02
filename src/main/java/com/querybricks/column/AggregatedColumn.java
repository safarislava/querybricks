package com.querybricks.column;

/**
 * Represents a column whose value is computed using an aggregate function (e.g., SUM, AVG, COUNT).
 *
 * @param <T> the Java type representing the value type of the column
 */
public interface AggregatedColumn<T> extends Column<T> {
}
