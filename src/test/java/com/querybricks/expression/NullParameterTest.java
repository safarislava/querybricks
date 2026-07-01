package com.querybricks.expression;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class NullParameterTest {
    @Test
    void testSql() {
        MatcherAssert.assertThat(
            new NullParameter().sql(),
            Matchers.equalTo("NULL")
        );
    }
}
