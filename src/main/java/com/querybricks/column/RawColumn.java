package com.querybricks.column;

import com.querybricks.Bindings;

/**
 * Represents a raw SQL column defined simply by its name.
 *
 * @param <T> the Java type representing the value type of the column
 */
public class RawColumn<T> implements UnboundColumn<T> {
    private final String name;

    /**
     * Constructs a RawColumn with the specified column name.
     *
     * @param name the name of the column
     */
    public RawColumn(String name) {
        this.name = name;
    }

    @Override
    public String sql() {
        return this.name;
    }

    @Override
    public Bindings bind(Bindings bindings) {
        return bindings;
    }
}
