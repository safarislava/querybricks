package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.condition.Condition;
import com.querybricks.table.Table;

/**
 * Represents a DELETE SQL query that removes rows from a table matching a specific condition.
 */
public class DeleteQuery implements Query {
    private final Table table;
    private final Condition condition;

    /**
     * Constructs a delete query.
     *
     * @param table the table from which rows will be deleted
     * @param condition the condition that rows must match to be deleted
     */
    public DeleteQuery(Table table, Condition condition) {
        this.table = table;
        this.condition = condition;
    }

    @Override
    public String sql() {
        return String.format("DELETE FROM %s WHERE %s", this.table.sql(), this.condition.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.condition.bind(this.table.bind(bindings));
    }
}
