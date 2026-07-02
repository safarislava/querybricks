package com.querybricks.database;

import com.querybricks.column.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An in-memory implementation of the {@link Row} interface,
 * backing the column values with a map.
 */
public class InMemoryRow implements Row {
    private final Map<String, Object> data;

    /**
     * Constructs an in-memory row with the provided data map.
     *
     * @param map a map containing column names as keys and column values as values
     */
    public InMemoryRow(Map<String, Object> map) {
        data = Map.copyOf(map);
    }

    /**
     * Constructs an in-memory row from a SQL query result set.
     *
     * @param columns the list of columns expected in the row
     * @param resultSet the result set to read values from
     */
    public InMemoryRow(List<Column<?>> columns, ResultSet resultSet) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            try {
                map.put(columns.get(i).sql(), resultSet.getObject(i + 1));
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to read column", e);
            }
        }
        this(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T value(Column<T> column) {
        if (!data.containsKey(column.sql())) {
            throw new IllegalArgumentException("Column not found in row: " + column.sql());
        }
        return (T) data.get(column.sql());
    }
}
