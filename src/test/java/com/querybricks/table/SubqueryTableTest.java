package com.querybricks.table;

import com.querybricks.query.FakeQuery;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class SubqueryTableTest {
    private final SubqueryTable<FakeFilterableTable> table = new SubqueryTable<>(
        new FakeFilterableTable("users"),
        new FakeQuery("SELECT * FROM users")
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            table.sql(),
            Matchers.equalTo("(SELECT * FROM users)")
        );
    }

    @Test
    void testOrigin() {
        MatcherAssert.assertThat(
            table.origin().sql(),
            Matchers.equalTo("users")
        );
    }
}
