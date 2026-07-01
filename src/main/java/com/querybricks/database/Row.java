package com.querybricks.database;

import com.querybricks.column.Column;

public interface Row {
    <T> T value(Column<T> column);
}
