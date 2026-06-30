package com.querybricks.column;

import com.querybricks.table.FakeTable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class TableColumnTest {
    private final BoundColumn<Long> column = new TableColumn<>(
        new FakeTable("users"),
        new RawColumn<>("id")
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.column.sql(),
            Matchers.equalTo("users.id")
        );
    }

    @Test
    void testUnbound() {
        MatcherAssert.assertThat(
            this.column.unbound().sql(),
            Matchers.equalTo("id")
        );
    }
}
