package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.column.Column;
import com.querybricks.expression.Expression;

public class ColumnAssignment implements QueryPart {
    private final Column<?> column;
    private final Expression value;

    public ColumnAssignment(Column<?> column, Expression value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public String sql() {
        return String.format("%s = %s", this.column.sql(), this.value.sql());
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return this.value.bind(this.column.bind(bindings));
    }
}
