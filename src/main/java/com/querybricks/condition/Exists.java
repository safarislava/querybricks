package com.querybricks.condition;

import com.querybricks.Bindings;
import com.querybricks.query.Query;

public class Exists implements Condition {
    private final Query query;

    public Exists(Query query) {
        this.query = query;
    }

    @Override
    public String sql() {
        return String.format("EXISTS (%s)", this.query.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.query.bind(bindings);
    }
}
