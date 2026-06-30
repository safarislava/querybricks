package com.querybricks.column;

public interface BoundColumn<T> extends Column<T> {
    UnboundColumn<T> unbound();
}
