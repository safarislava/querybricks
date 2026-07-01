package com.querybricks.table;

import com.querybricks.column.BoundColumn;
import java.util.List;

public interface WrappedTable<T extends Table> extends Table {
    T origin();

    @Override
    default List<BoundColumn<?>> columns() {
        return this.origin().columns();
    }
}
