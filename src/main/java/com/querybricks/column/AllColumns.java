package com.querybricks.column;

import com.querybricks.Bindings;
import com.querybricks.table.Table;
import java.util.function.Consumer;

public class AllColumns implements Columns {
    private final Table table;

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
