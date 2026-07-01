package com.querybricks.example;

import com.querybricks.column.ColumnsSelection;
import com.querybricks.condition.Equals;
import com.querybricks.condition.Exists;
import com.querybricks.condition.GreaterThan;
import com.querybricks.expression.Parameter;
import com.querybricks.query.Query;
import com.querybricks.query.SelectQuery;
import com.querybricks.table.FilteredTable;
import com.querybricks.table.SubqueryTable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class ExistsQueryExampleTest {
    private final UsersTable users = new DbUsersTable("users");
    private final OrdersTable orders = new DbOrdersTable("orders");

    private final Query filtered = new SelectQuery(
        new ColumnsSelection(this.orders.userId(), this.orders.amount()),
        new FilteredTable<>(
            this.orders,
            new GreaterThan(this.orders.amount(), new Parameter<>(1000))
        )
    );

    private final SubqueryTable<OrdersTable> subquery = new SubqueryTable<>(
        this.orders,
        this.filtered
    );

    private final Query existsSubquery = new SelectQuery(
        new ColumnsSelection(new Parameter<>(1)),
        new FilteredTable<>(
            this.subquery,
            new Equals(this.subquery.origin().userId(), this.users.id())
        )
    );

    private final Query query = new SelectQuery(
        new ColumnsSelection(this.users.username()),
        new FilteredTable<>(
            this.users,
            new Exists(this.existsSubquery)
        )
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.query.sql(),
            Matchers.equalTo(
                "SELECT users.username FROM users "
                + "WHERE EXISTS ("
                +    "SELECT ? FROM ("
                +        "SELECT orders.user_id, orders.amount FROM orders "
                +        "WHERE orders.amount > ?"
                +    ") "
                +    "WHERE orders.user_id = users.id"
                + ")")
        );
    }
}
