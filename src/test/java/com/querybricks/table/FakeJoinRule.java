package com.querybricks.table;

import com.querybricks.Bindings;

public final class FakeJoinRule implements JoinRule {
    private final String format;

    public FakeJoinRule(String format) {
        this.format = format;
    }

    @Override
    public String sql(Table right) {
        return String.format(this.format, right.sql());
    }

    @Override
    public Bindings bind(Bindings bindings, Table right) {
        return right.bind(bindings);
    }
}
