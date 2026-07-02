package com.querybricks.database;

import com.querybricks.Bindings;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Bindings implementation designed for JDBC {@link PreparedStatement}.
 * Allows sequential binding of query parameters.
 */
public final class JdbcBindings implements Bindings {
    private final PreparedStatement statement;
    private final int index;

    /**
     * Constructs a new set of JDBC bindings starting at parameter index 1.
     *
     * @param statement the prepared statement to bind values to
     */
    public JdbcBindings(PreparedStatement statement) {
        this(statement, 1);
    }

    private JdbcBindings(PreparedStatement statement, int index) {
        this.statement = statement;
        this.index = index;
    }

    @Override
    public Bindings with(Object value) {
        try {
            this.statement.setObject(this.index, value);
            return new JdbcBindings(this.statement, this.index + 1);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to bind parameter at index " + this.index, e);
        }
    }
}
