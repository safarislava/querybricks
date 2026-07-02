package com.querybricks.column;

import com.querybricks.Bindings;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A selection of specific columns to be queried.
 */
public class ColumnsSelection implements Columns {
    private final List<Column<?>> columns;

    /**
     * Constructs a ColumnsSelection with the specified columns.
     *
     * @param columns the columns to include in the selection
     */
    public ColumnsSelection(Column<?>... columns) {
        this.columns = List.of(columns);
    }

    @Override
    public String sql() {
        return this.columns.stream().map(Column::sql).collect(Collectors.joining(", "));
    }

    @Override
    public Bindings bind(Bindings bindings) {
        Bindings current = bindings;
        for (Column<?> column : this.columns) {
            current = column.bind(current);
        }
        return current;
    }

    @Override
    public void processAll(Consumer<Column<?>> processor) {
        for (Column<?> column : this.columns) {
            processor.accept(column);
        }
    }
}
