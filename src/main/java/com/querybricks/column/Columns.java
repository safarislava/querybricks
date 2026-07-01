package com.querybricks.column;

import com.querybricks.QueryPart;
import java.util.function.Consumer;

public interface Columns extends QueryPart {
    void processAll(Consumer<Column<?>> consumer);
}
