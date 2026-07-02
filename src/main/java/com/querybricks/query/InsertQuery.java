package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.column.UnboundColumn;
import com.querybricks.table.Table;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an INSERT SQL query that inserts one or more rows into a table.
 */
public class InsertQuery implements Query {
    private final Table table;
    private final List<UnboundColumn<?>> columns;
    private final List<InsertRow> rows;

    /**
     * Constructs an insert query.
     *
     * @param table the table to insert rows into
     * @param columns the columns to be populated with values
     * @param rows the list of rows (values) to insert
     */
    public InsertQuery(Table table, List<UnboundColumn<?>> columns, List<InsertRow> rows) {
        this.table = table;
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public String sql() {
        if (this.columns.isEmpty()) {
            throw new IllegalStateException("Insert query must specify at least one column");
        }
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("Insert query must have at least one row");
        }
        return String.format(
            "INSERT INTO %s (%s) VALUES %s",
            table.sql(),
            columns.stream().map(QueryPart::sql).collect(Collectors.joining(", ")),
            rows.stream().map(InsertRow::sql).collect(Collectors.joining(", "))
        );
    }

    @Override
    public Bindings bind(Bindings bindings) {
        Bindings current = this.table.bind(bindings);
        for (InsertRow row : this.rows) {
            current = row.bind(current);
        }
        return current;
    }
}
