package com.querybricks.query;

import com.querybricks.column.RawColumn;
import com.querybricks.column.TableColumn;
import com.querybricks.expression.NumberLiteral;
import com.querybricks.expression.StringLiteral;
import com.querybricks.table.FakeTable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

final class InsertQueryTest {
    @Test
    void testSql() {
        Query query = new InsertQuery(
            new FakeTable("users"),
            List.of(
                new TableColumn<>(new FakeTable("users"), new RawColumn<>("id")).unbound(),
                new TableColumn<>(new FakeTable("users"), new RawColumn<>("username")).unbound()
            ),
            List.of(
                new InsertRow(
                    new NumberLiteral(1),
                    new StringLiteral("john")
                )
            )
        );
        MatcherAssert.assertThat(
            query.sql(),
            Matchers.equalTo("INSERT INTO users (id, username) VALUES (1, 'john')")
        );
    }

    @Test
    void testSqlWithEmptyColumns() {
        Query query = new InsertQuery(
            new FakeTable("users"),
            List.of(),
            List.of(
                new InsertRow(
                    new NumberLiteral(1),
                    new StringLiteral("john")
                )
            )
        );
        try {
            query.sql();
        } catch (IllegalStateException e) {
            MatcherAssert.assertThat(
                e.getMessage(),
                Matchers.equalTo("Insert query must specify at least one column")
            );
        }
    }

    @Test
    void testSqlWithEmptyRows() {
        Query query = new InsertQuery(
            new FakeTable("users"),
            List.of(
                new TableColumn<>(new FakeTable("users"), new RawColumn<>("id")).unbound(),
                new TableColumn<>(new FakeTable("users"), new RawColumn<>("username")).unbound()
            ),
            List.of()
        );
        try {
            query.sql();
        } catch (IllegalStateException e) {
            MatcherAssert.assertThat(
                e.getMessage(),
                Matchers.equalTo("Insert query must have at least one row")
            );
        }
    }
}
