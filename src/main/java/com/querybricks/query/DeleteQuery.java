package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.condition.Condition;
import com.querybricks.table.Table;

public class DeleteQuery implements Query {
    private final Table table;
    private final Condition condition;

    public DeleteQuery(Table table, Condition condition) {
        this.table = table;
        this.condition = condition;
    }

    @Override
    public String sql() {
        return String.format("DELETE FROM %s WHERE %s", this.table.sql(), this.condition.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.condition.bind(this.table.bind(bindings));
    }
}
