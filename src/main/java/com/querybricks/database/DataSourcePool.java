package com.querybricks.database;

import com.querybricks.column.Column;
import com.querybricks.query.Query;
import com.querybricks.query.ResultedQuery;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataSourcePool implements DbPool {
    private final DataSource source;

    public DataSourcePool(DataSource source) {
        this.source = source;
    }

    @Override
    @SuppressWarnings("SqlSourceToSinkFlow")
    public void execute(Query query) {
        String sql = query.sql();
        try {
            Connection connection = this.source.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
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
        try {
            Connection connection = this.source.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Row> rows = new ArrayList<>();
            while (resultSet.next()) {
                rows.add(new InMemoryRow(columns, resultSet));
            }
            return rows;
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
    }
}
