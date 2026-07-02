package com.querybricks.column;

import com.querybricks.QueryPart;
import java.util.function.Consumer;

/**
 * Represents a collection or selection of columns in a query.
 */
public interface Columns extends QueryPart {
    /**
     * Processes each column in this collection using the provided consumer.
     *
     * @param consumer the consumer to process the columns
     */
    void processAll(Consumer<Column<?>> consumer);
}
