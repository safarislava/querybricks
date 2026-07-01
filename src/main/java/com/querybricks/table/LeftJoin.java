package com.querybricks.table;

import com.querybricks.Bindings;
import com.querybricks.condition.Condition;

public final class LeftJoin implements JoinRule {
    private final Condition condition;

    public LeftJoin(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String sql(Table right) {
        return String.format("LEFT JOIN %s ON %s", right.sql(), this.condition.sql());
    }

    @Override
    public Bindings bind(Bindings bindings, Table right) {
        return this.condition.bind(right.bind(bindings));
    }
}
