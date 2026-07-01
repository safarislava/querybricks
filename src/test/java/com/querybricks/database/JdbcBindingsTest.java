package com.querybricks.database;

import com.querybricks.Bindings;
import org.h2.jdbcx.JdbcDataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

final class JdbcBindingsTest {
    @Test
    void testJdbcBindings() throws SQLException {
        JdbcDataSource source = new JdbcDataSource();
        source.setUrl("jdbc:h2:mem:test_bindings;DB_CLOSE_DELAY=-1");
        source.setUser("sa");
        source.setPassword("");
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT ?")) {
            Bindings bindings = new JdbcBindings(stmt);
            Bindings next = bindings.with("hello");
            MatcherAssert.assertThat(
                next,
                Matchers.notNullValue()
            );
        }
    }

    @Test
    void testJdbcBindingsError() throws SQLException {
        JdbcDataSource source = new JdbcDataSource();
        source.setUrl("jdbc:h2:mem:test_bindings_err;DB_CLOSE_DELAY=-1");
        source.setUser("sa");
        source.setPassword("");
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {
            Bindings bindings = new JdbcBindings(stmt);
            try {
                bindings.with("hello");
                org.junit.jupiter.api.Assertions.fail("Expected SQLException to be thrown");
            } catch (IllegalStateException e) {
                MatcherAssert.assertThat(
                    e.getMessage(),
                    Matchers.containsString("Failed to bind parameter")
                );
            }
        }
    }
}
