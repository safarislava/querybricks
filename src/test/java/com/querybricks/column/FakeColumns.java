package com.querybricks.column;

import com.querybricks.Bindings;
import java.util.function.Consumer;

public final class FakeColumns implements Columns {
    private final String columns;

    public FakeColumns(String columns) {
        this.columns = columns;
    }

    @Override
    public String sql() {
        return this.columns;
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings;
    }

    @Override
    public void processAll(Consumer<Column<?>> consumer) {
    }
}
