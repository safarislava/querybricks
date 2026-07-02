package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.column.Column;
import com.querybricks.column.Columns;
import com.querybricks.table.Table;
import java.util.function.Consumer;

/**
 * Represents a SELECT SQL query that retrieves data from a database table.
 */
public class SelectQuery implements ResultedQuery {
    private final Columns columns;
    private final Table table;

    /**
     * Constructs a select query.
     *
     * @param columns the columns to be selected
     * @param table the table from which data will be retrieved
     */
    public SelectQuery(Columns columns, Table table) {
        this.columns = columns;
        this.table = table;
    }

    @Override
    public String sql() {
        return String.format("SELECT %s FROM %s", this.columns.sql(), this.table.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.table.bind(this.columns.bind(bindings));
    }

    @Override
    public void processColumns(Consumer<Column<?>> consumer) {
        this.columns.processAll(consumer);
    }
}
