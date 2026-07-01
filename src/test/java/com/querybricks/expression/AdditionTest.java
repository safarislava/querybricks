package com.querybricks.expression;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class AdditionTest {
    private final Expression expression = new Addition(
        new Parameter<>(5),
        new Parameter<>(10)
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.expression.sql(),
            Matchers.equalTo("? + ?")
        );
    }
}
