package com.querybricks.example;

import com.querybricks.column.BoundColumn;
import com.querybricks.column.RawColumn;
import com.querybricks.column.TableColumn;

import java.math.BigDecimal;

public class DbOrdersTable implements OrdersTable {
    private final String name;

    public DbOrdersTable(String name) {
        this.name = name;
    }

    @Override
    public String sql() {
        return this.name;
    }

    @Override
    public BoundColumn<Long> userId() {
        return new TableColumn<>(this, new RawColumn<>("user_id"));
    }

    @Override
    public BoundColumn<BigDecimal> amount() {
        return new TableColumn<>(this, new RawColumn<>("amount"));
    }
}
