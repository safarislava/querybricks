package com.querybricks.database;

import com.querybricks.Bindings;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class JdbcBindings implements Bindings {
    private final PreparedStatement statement;
    private final int index;

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
