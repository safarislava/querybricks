package com.querybricks.table;

import com.querybricks.condition.FakeCondition;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class FilteredTableTest {
    private final FilteredTable<FakeFilterableTable> table = new FilteredTable<>(
        new FakeFilterableTable("users"),
        new FakeCondition("users.id = 1")
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            table.sql(),
            Matchers.equalTo("users WHERE users.id = 1")
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
