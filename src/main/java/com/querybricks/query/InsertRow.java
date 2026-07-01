package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.expression.Expression;
import java.util.List;
import java.util.stream.Collectors;

public class InsertRow implements QueryPart {
    private final List<Expression> values;

    public InsertRow(Expression... values) {
        this.values = List.of(values);
    }

    @Override
    public String sql() {
        return this.values.stream()
            .map(Expression::sql)
            .collect(Collectors.joining(", ", "(", ")"));
    }

    @Override
    public Bindings bind(Bindings bindings) {
        Bindings current = bindings;
        for (Expression value : this.values) {
            current = value.bind(current);
        }
        return current;
    }
}
