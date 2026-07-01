package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.condition.Condition;
import com.querybricks.table.Table;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateQuery implements Query {
    private final Table table;
    private final List<ColumnAssignment> assignments;
    private final Condition condition;

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
