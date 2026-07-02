package com.querybricks.column;

/**
 * Represents a column that is bound to a specific table context.
 *
 * @param <T> the Java type representing the value type of the column
 */
public interface BoundColumn<T> extends Column<T> {
    /**
     * Obtains the unbound representation of this column.
     *
     * @return the unbound column representation
     */
    UnboundColumn<T> unbound();
}
