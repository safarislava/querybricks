package com.querybricks.condition;

import com.querybricks.query.FakeQuery;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class ExistsTest {
    private final Condition condition = new Exists(
        new FakeQuery("SELECT 1 FROM users")
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.condition.sql(),
            Matchers.equalTo("EXISTS (SELECT 1 FROM users)")
        );
    }
}
