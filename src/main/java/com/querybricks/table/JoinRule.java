package com.querybricks.table;

import com.querybricks.Bindings;

public interface JoinRule {
    String sql(Table right);
    Bindings bind(Bindings bindings, Table right);
}
