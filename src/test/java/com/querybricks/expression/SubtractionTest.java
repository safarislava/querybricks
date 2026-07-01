package com.querybricks.expression;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class SubtractionTest {
    private final Expression expression = new Subtraction(
        new Parameter<>(100),
        new Parameter<>(50)
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.expression.sql(),
            Matchers.equalTo("? - ?")
        );
    }
}
