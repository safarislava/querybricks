package com.querybricks.expression;

import com.querybricks.Bindings;
import com.querybricks.column.Column;

public final class NullParameter implements Expression, Column<Object> {
    @Override
    public String sql() {
        return "NULL";
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings;
    }
}
