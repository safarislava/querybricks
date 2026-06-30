package com.querybricks.expression;

public class NumberLiteral implements Literal<Number> {
    private final Number value;

    public NumberLiteral(Number value) {
        this.value = value;
    }

    @Override
    public String sql() {
        return String.valueOf(this.value);
    }
}
