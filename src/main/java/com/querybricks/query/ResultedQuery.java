package com.querybricks.query;

import com.querybricks.column.Column;
import java.util.function.Consumer;

/**
 * Represents a SQL query that yields a result set containing columns.
 */
public interface ResultedQuery extends Query {
    /**
     * Processes all columns that are selected/produced by this query.
     *
     * @param consumer the consumer to process each column
     */
    void processColumns(Consumer<Column<?>> consumer);
}
