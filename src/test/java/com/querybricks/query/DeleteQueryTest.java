package com.querybricks.query;

import com.querybricks.column.RawColumn;
import com.querybricks.column.TableColumn;
import com.querybricks.condition.Equals;
import com.querybricks.expression.Parameter;
import com.querybricks.table.FakeFilterableTable;
import com.querybricks.table.Table;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class DeleteQueryTest {
    @Test
    void testSql() {
        Table table = new FakeFilterableTable("users");
        MatcherAssert.assertThat(
            new DeleteQuery(
                table,
                new Equals(
                    new TableColumn<>(table, new RawColumn<>("id")),
                    new Parameter<>(1)
                )
            ).sql(),
            Matchers.equalTo("DELETE FROM users WHERE users.id = ?")
        );
    }
}
