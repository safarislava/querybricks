package com.querybricks.column;

/**
 * Represents a column that is not bound to any specific table context.
 *
 * @param <T> the Java type representing the value type of the column
 */
public interface UnboundColumn<T> extends Column<T> {
}
