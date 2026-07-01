package com.querybricks.expression;

import com.querybricks.Bindings;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class ParameterTest {
    @Test
    void testSql() {
        MatcherAssert.assertThat(
            new Parameter<>("hello").sql(),
            Matchers.equalTo("?")
        );
    }

    @Test
    void testBind() {
        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };
        new Parameter<>("hello").bind(bindings);
        MatcherAssert.assertThat(
            values,
            Matchers.contains("hello")
        );
    }
}
