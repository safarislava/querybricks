package com.querybricks.expression;

import com.querybricks.Bindings;
import com.querybricks.column.Column;

public final class Parameter<T> implements Expression, Column<T> {
    private final T value;

    public Parameter(T value) {
        this.value = value;
    }

    @Override
    public String sql() {
        return "?";
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings.with(this.value);
    }
}
