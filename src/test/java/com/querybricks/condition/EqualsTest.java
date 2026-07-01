package com.querybricks.condition;

import com.querybricks.column.RawColumn;
import com.querybricks.expression.Parameter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class EqualsTest {
    private final Condition condition = new Equals(
        new RawColumn<>("id"),
        new Parameter<>(1)
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.condition.sql(),
            Matchers.equalTo("id = ?")
        );
    }
}
