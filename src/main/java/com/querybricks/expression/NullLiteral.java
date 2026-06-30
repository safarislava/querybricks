package com.querybricks.expression;

public class NullLiteral implements Literal<Object> {
    @Override
    public String sql() {
        return "NULL";
    }
}
