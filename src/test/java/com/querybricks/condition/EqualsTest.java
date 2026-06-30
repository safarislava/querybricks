package com.querybricks.condition;

import com.querybricks.column.DbColumn;
import com.querybricks.expression.NumberLiteral;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class EqualsTest {
    private final Condition condition = new Equals(
        new DbColumn<>("id"),
        new NumberLiteral(1)
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.condition.sql(),
            Matchers.equalTo("id = 1")
        );
    }
}
