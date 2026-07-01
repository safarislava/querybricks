package com.querybricks.condition;

import com.querybricks.Bindings;

public final class FakeCondition implements Condition {
    private final String expression;

    public FakeCondition(String expression) {
        this.expression = expression;
    }

    @Override
    public String sql() {
        return this.expression;
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings;
    }
}
