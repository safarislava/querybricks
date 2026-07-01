package com.querybricks;

public interface QueryPart {
    String sql();
    Bindings bind(Bindings bindings);
}
