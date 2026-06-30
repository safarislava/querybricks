package com.querybricks.condition;

import com.querybricks.column.DbColumn;
import com.querybricks.expression.NumberLiteral;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class GreaterThanTest {
    private final Condition condition = new GreaterThan(
        new DbColumn<>("amount"),
        new NumberLiteral(100)
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.condition.sql(),
            Matchers.equalTo("amount > 100")
        );
    }
}
