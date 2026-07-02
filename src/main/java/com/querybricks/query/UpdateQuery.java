package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.condition.Condition;
import com.querybricks.table.Table;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an UPDATE SQL query that modifies existing rows in a database table.
 */
public class UpdateQuery implements Query {
    private final Table table;
    private final List<ColumnAssignment> assignments;
    private final Condition condition;

    /**
     * Constructs an update query.
     *
     * @param table the table to update
     * @param assignments the list of column assignments to perform
     * @param condition the condition that determines which rows to update
     */
    public UpdateQuery(Table table, List<ColumnAssignment> assignments, Condition condition) {
        this.table = table;
        this.assignments = List.copyOf(assignments);
        this.condition = condition;
    }

    @Override
    public String sql() {
        if (this.assignments.isEmpty()) {
            throw new IllegalStateException("Update query must specify at least one assignment");
        }
        return String.format(
            "UPDATE %s SET %s WHERE %s",
            this.table.sql(),
            this.assignments.stream().map(QueryPart::sql).collect(Collectors.joining(", ")),
            this.condition.sql()
        );
    }

    @Override
    public Bindings bind(Bindings bindings) {
        Bindings current = this.table.bind(bindings);
        for (ColumnAssignment assignment : this.assignments) {
            current = assignment.bind(current);
        }
        return this.condition.bind(current);
    }
}
