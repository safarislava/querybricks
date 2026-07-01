package com.querybricks.database;

import com.querybricks.Bindings;
import com.querybricks.column.Column;
import com.querybricks.column.ColumnsSelection;
import com.querybricks.column.RawColumn;
import com.querybricks.column.TableColumn;
import com.querybricks.query.Query;
import com.querybricks.query.ResultedQuery;
import com.querybricks.query.SelectQuery;
import com.querybricks.table.FakeFilterableTable;
import org.h2.jdbcx.JdbcDataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class DataSourcePoolTest {
    @Test
    void testSelection() {
        MatcherAssert.assertThat(
            new H2Database(
                "selection",
                List.of("CREATE TABLE users (username VARCHAR(255))", "INSERT INTO users VALUES ('john')"),
                List.of("DROP TABLE users")
            ).selectionValue(
                new SelectQuery(
                    new ColumnsSelection(
                        new TableColumn<>(
                            new FakeFilterableTable("users"),
                            new RawColumn<>("username")
                        )
                    ),
                    new FakeFilterableTable("users")
                ),
                new TableColumn<>(new FakeFilterableTable("users"), new RawColumn<>("username"))
            ),
            Matchers.equalTo("john")
        );
    }

    @Test
    void testSelectionWithDuplicateColumns() {
        MatcherAssert.assertThat(
            new H2Database(
                "duplicates",
                List.of(
                    "CREATE TABLE t1 (id VARCHAR(255))",
                    "CREATE TABLE t2 (id VARCHAR(255))",
                    "INSERT INTO t1 VALUES ('first')",
                    "INSERT INTO t2 VALUES ('second')"
                ),
                List.of("DROP TABLE t1", "DROP TABLE t2")
            ).selectionValues(
                new SelectQuery(
                    new ColumnsSelection(
                        new TableColumn<>(new FakeFilterableTable("t1"), new RawColumn<>("id")),
                        new TableColumn<>(new FakeFilterableTable("t2"), new RawColumn<>("id"))
                    ),
                    new FakeFilterableTable("t1 JOIN t2 ON 1=1")
                ),
                List.of(
                    new TableColumn<>(new FakeFilterableTable("t1"), new RawColumn<>("id")),
                    new TableColumn<>(new FakeFilterableTable("t2"), new RawColumn<>("id"))
                )
            ),
            Matchers.equalTo(List.of("first", "second"))
        );
    }

    @Test
    void testExecute() {
        MatcherAssert.assertThat(
            new H2Database(
                "execute",
                List.of("CREATE TABLE log (message VARCHAR(255))"),
                List.of("DROP TABLE log")
            ).executeAndGet(
                new ResultedQuery() {
                    @Override
                    public String sql() {
                        return "INSERT INTO log VALUES ('test')";
                    }

                    @Override
                    public Bindings bind(Bindings bindings) {
                        return bindings;
                    }

                    @Override
                    public void processColumns(Consumer<Column<?>> consumer) {
                    }
                },
                "SELECT message FROM log"
            ),
            Matchers.equalTo("test")
        );
    }

    private static final class H2Database {
        private final String dbName;
        private final List<String> setups;
        private final List<String> teardowns;

        H2Database(String dbName, List<String> setups, List<String> teardowns) {
            this.dbName = dbName;
            this.setups = setups;
            this.teardowns = teardowns;
        }

        public <T> T selectionValue(ResultedQuery query, Column<T> column) {
            JdbcDataSource source = new JdbcDataSource();
            source.setUrl("jdbc:h2:mem:" + this.dbName + ";DB_CLOSE_DELAY=-1");
            source.setUser("sa");
            source.setPassword("");
            try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement()) {
                for (String sql : this.setups) {
                    stmt.execute(sql);
                }
                try {
                    return new DataSourcePool(source).selection(query).getFirst().value(column);
                } finally {
                    for (String sql : this.teardowns) {
                        stmt.execute(sql);
                    }
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        public List<Object> selectionValues(ResultedQuery query, List<Column<?>> columns) {
            JdbcDataSource source = new JdbcDataSource();
            source.setUrl("jdbc:h2:mem:" + this.dbName + ";DB_CLOSE_DELAY=-1");
            source.setUser("sa");
            source.setPassword("");
            try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement()) {
                for (String sql : this.setups) {
                    stmt.execute(sql);
                }
                try {
                    Row row = new DataSourcePool(source).selection(query).getFirst();
                    List<Object> values = new ArrayList<>();
                    for (Column<?> col : columns) {
                        values.add(row.value(col));
                    }
                    return values;
                } finally {
                    for (String sql : this.teardowns) {
                        stmt.execute(sql);
                    }
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        @SuppressWarnings("SqlSourceToSinkFlow")
        public String executeAndGet(Query query, String checkSql) {
            JdbcDataSource source = new JdbcDataSource();
            source.setUrl("jdbc:h2:mem:" + this.dbName + ";DB_CLOSE_DELAY=-1");
            source.setUser("sa");
            source.setPassword("");
            try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement()) {
                for (String sql : this.setups) {
                    stmt.execute(sql);
                }
                try {
                    new DataSourcePool(source).execute(query);
                    try (ResultSet rs = stmt.executeQuery(checkSql)) {
                        rs.next();
                        return rs.getString(1);
                    }
                } finally {
                    for (String sql : this.teardowns) {
                        stmt.execute(sql);
                    }
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
