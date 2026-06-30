package com.querybricks.expression;

public class StringLiteral implements Literal<String> {
    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String sql() {
        return String.format("'%s'", this.value);
    }
}
