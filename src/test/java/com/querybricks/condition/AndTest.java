package com.querybricks.condition;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class AndTest {
    private final Condition condition = new And(
        new FakeCondition("id = 1"),
        new FakeCondition("status = 'active'")
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.condition.sql(),
            Matchers.equalTo("id = 1 AND status = 'active'")
        );
    }
}
