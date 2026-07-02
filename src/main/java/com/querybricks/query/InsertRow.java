package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.expression.Expression;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a single row of values to be inserted into a table.
 */
public class InsertRow implements QueryPart {
    private final List<Expression> values;

    /**
     * Constructs a new insert row with the specified expression values.
     *
     * @param values the expression values for the columns of the row
     */
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
