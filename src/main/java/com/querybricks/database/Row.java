package com.querybricks.database;

import com.querybricks.column.BoundColumn;

public interface Row {
    <T> T value(BoundColumn<T> column);
}
