package com.querybricks.condition;

import com.querybricks.column.RawColumn;
import com.querybricks.expression.Parameter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class GreaterThanTest {
    private final Condition condition = new GreaterThan(
        new RawColumn<>("amount"),
        new Parameter<>(100)
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.condition.sql(),
            Matchers.equalTo("amount > ?")
        );
    }
}
