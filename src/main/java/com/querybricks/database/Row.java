package com.querybricks.database;

import com.querybricks.column.Column;

/**
 * Represents a single row in a query result set.
 * Provides access to column values in a type-safe manner.
 */
public interface Row {
    /**
     * Retrieves the value of the specified column.
     *
     * @param column the column to retrieve the value for
     * @param <T> the type of the column value
     * @return the value of the column
     */
    <T> T value(Column<T> column);
}
