package com.querybricks.table;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;
import com.querybricks.column.BoundColumn;
import java.util.List;

public interface Table extends QueryPart {
    List<BoundColumn<?>> columns();

    @Override
    default Bindings bind(Bindings bindings) {
        return bindings;
    }
}
