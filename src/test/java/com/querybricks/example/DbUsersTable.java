package com.querybricks.example;

import com.querybricks.column.BoundColumn;
import com.querybricks.column.RawColumn;
import com.querybricks.column.TableColumn;
import java.util.List;

public class DbUsersTable implements UsersTable {
    private final String name;

    public DbUsersTable(String name) {
        this.name = name;
    }

    @Override
    public String sql() {
        return this.name;
    }

    @Override
    public BoundColumn<Long> id() {
        return new TableColumn<>(this, new RawColumn<>("id"));
    }

    @Override
    public BoundColumn<String> username() {
        return new TableColumn<>(this, new RawColumn<>("username"));
    }

    @Override
    public BoundColumn<String> status() {
        return new TableColumn<>(this, new RawColumn<>("status"));
    }

    @Override
    public BoundColumn<java.time.Instant> createdAt() {
        return new TableColumn<>(this, new RawColumn<>("created_at"));
    }

    @Override
    public List<BoundColumn<?>> columns() {
        return java.util.List.of(id(), username(), status(), createdAt());
    }
}
