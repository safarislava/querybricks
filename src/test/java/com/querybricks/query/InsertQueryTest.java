package com.querybricks.query;

import com.querybricks.column.RawColumn;
import com.querybricks.column.TableColumn;
import com.querybricks.expression.Parameter;
import com.querybricks.table.FakeFilterableTable;
import com.querybricks.table.Table;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

final class InsertQueryTest {
    private final Table table = new FakeFilterableTable("users");
    
    @Test
    void testSql() {
        Query query = new InsertQuery(
            table,
            List.of(
                new TableColumn<>(table, new RawColumn<>("id")).unbound(),
                new TableColumn<>(table, new RawColumn<>("username")).unbound()
            ),
            List.of(
                new InsertRow(
                    new Parameter<>(1),
                    new Parameter<>("john")
                )
            )
        );
        MatcherAssert.assertThat(
            query.sql(),
            Matchers.equalTo("INSERT INTO users (id, username) VALUES (?, ?)")
        );
    }

    @Test
    void testSqlWithEmptyColumns() {
        Query query = new InsertQuery(
            table,
            List.of(),
            List.of(
                new InsertRow(
                    new Parameter<>(1),
                    new Parameter<>("john")
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
            table,
            List.of(
                new TableColumn<>(table, new RawColumn<>("id")).unbound(),
                new TableColumn<>(table, new RawColumn<>("username")).unbound()
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
