package com.querybricks.database;

import com.querybricks.column.Column;
import com.querybricks.query.Query;
import com.querybricks.query.ResultedQuery;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A connection pool implementation that wraps a JDBC {@link DataSource}
 * to execute queries and retrieve rows.
 */
public class DataSourcePool implements DbPool {
    private final DataSource source;

    /**
     * Constructs a DataSourcePool backed by the specified DataSource.
     *
     * @param source the data source used to acquire connections
     */
    public DataSourcePool(DataSource source) {
        this.source = source;
    }

    @Override
    @SuppressWarnings("SqlSourceToSinkFlow")
    public void execute(Query query) {
        String sql = query.sql();
        try (Connection connection = this.source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            query.bind(new JdbcBindings(statement));
            statement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Execution failed: " + sql, e);
        }
    }

    @Override
    @SuppressWarnings("SqlSourceToSinkFlow")
    public List<Row> selection(ResultedQuery query) {
        List<Column<?>> columns = new ArrayList<>();
        query.processColumns(columns::add);

        String sql = query.sql();
        try (Connection connection = this.source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            query.bind(new JdbcBindings(statement));
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Row> rows = new ArrayList<>();
                while (resultSet.next()) {
                    rows.add(new InMemoryRow(columns, resultSet));
                }
                return rows;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
    }
}
