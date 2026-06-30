package com.querybricks.example;

import com.querybricks.column.BoundColumn;
import com.querybricks.table.FilterableTable;

import java.time.Instant;

public interface UsersTable extends FilterableTable {
    BoundColumn<Long> id();
    BoundColumn<String> username();
    BoundColumn<String> status();
    BoundColumn<Instant> createdAt();
}
