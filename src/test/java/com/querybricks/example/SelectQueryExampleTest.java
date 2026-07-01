package com.querybricks.example;

import com.querybricks.column.ColumnsSelection;
import com.querybricks.condition.Equals;
import com.querybricks.expression.Parameter;
import com.querybricks.query.Query;
import com.querybricks.query.SelectQuery;
import com.querybricks.table.FilteredTable;
import com.querybricks.table.InnerJoin;
import com.querybricks.table.JoinedTable;
import com.querybricks.table.LimitedTable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

final class SelectQueryExampleTest {
    private final UsersTable users = new DbUsersTable("users");
    private final OrdersTable orders = new DbOrdersTable("orders");

    private final JoinedTable<UsersTable, OrdersTable> joined = new JoinedTable<>(
        this.users,
        this.orders,
        new InnerJoin(new Equals(this.users.id(), this.orders.userId()))
    );

    private final FilteredTable<JoinedTable<UsersTable, OrdersTable>> filtered = new FilteredTable<>(
        this.joined,
        new Equals(this.joined.left().status(), new Parameter<>("active"))
    );

    private final LimitedTable<FilteredTable<JoinedTable<UsersTable, OrdersTable>>> limited = new LimitedTable<>(
        this.filtered,
        10
    );

    private final Query query = new SelectQuery(
        new ColumnsSelection(
            this.limited.origin().origin().left().id(),
            this.limited.origin().origin().left().username(),
            this.limited.origin().origin().right().amount()
        ),
        this.limited
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.query.sql(),
            Matchers.equalTo(
                "SELECT users.id, users.username, orders.amount FROM users "
                + "INNER JOIN orders ON users.id = orders.user_id "
                + "WHERE users.status = ? LIMIT ?"
            )
        );
    }
}
