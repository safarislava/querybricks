package com.querybricks.column;

import com.querybricks.Bindings;
import com.querybricks.table.Table;
import java.util.function.Consumer;

/**
 * Represents the wildcard selection of all columns (e.g., "*") from a table.
 */
public class AllColumns implements Columns {
    private final Table table;

    /**
     * Constructs an AllColumns selection for the specified table.
     *
     * @param table the table whose columns are selected
     */
    public AllColumns(Table table) {
        this.table = table;
    }

    @Override
    public String sql() {
        return "*";
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.table.bind(bindings);
    }

    @Override
    public void processAll(Consumer<Column<?>> consumer) {
        for (Column<?> column : this.table.columns()) {
            consumer.accept(column);
        }
    }
}
