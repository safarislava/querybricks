package com.querybricks.query;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.column.Column;
import com.querybricks.expression.Expression;

/**
 * Represents the assignment of an expression value to a database column.
 * Typically used in the SET clause of an UPDATE statement.
 */
public class ColumnAssignment implements QueryPart {
    private final Column<?> column;
    private final Expression value;

    /**
     * Constructs a new column assignment.
     *
     * @param column the target database column
     * @param value the expression to be assigned to the column
     */
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
