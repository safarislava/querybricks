package com.querybricks.query;

import com.querybricks.expression.Parameter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class InsertRowTest {
    private final InsertRow row = new InsertRow(
        new Parameter<>(1),
        new Parameter<>("john")
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.row.sql(),
            Matchers.equalTo("(?, ?)")
        );
    }
}
