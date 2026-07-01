package com.querybricks.expression;

import com.querybricks.Bindings;
import com.querybricks.QueryPart;

public interface Expression extends QueryPart {
    @Override
    default Bindings bind(Bindings bindings) {
        return bindings;
    }
}
