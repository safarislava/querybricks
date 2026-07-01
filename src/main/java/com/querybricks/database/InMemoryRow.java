package com.querybricks.database;

import com.querybricks.column.BoundColumn;
import com.querybricks.column.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRow implements Row {
    private final Map<String, Object> data;

    public InMemoryRow(Map<String, Object> map) {
        data = Map.copyOf(map);
    }

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
    public <T> T value(BoundColumn<T> column) {
        if (!data.containsKey(column.sql())) {
            throw new IllegalArgumentException("Column not found in row: " + column.sql());
        }
        return (T) data.get(column.sql());
    }
}
