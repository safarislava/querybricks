package com.querybricks.query;

import com.querybricks.column.Column;
import java.util.function.Consumer;

public interface ResultedQuery extends Query {
    void processColumns(Consumer<Column<?>> consumer);
}
