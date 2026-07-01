package com.querybricks;

import com.querybricks.column.Avg;
import com.querybricks.column.ColumnsSelection;
import com.querybricks.column.Count;
import com.querybricks.column.RawColumn;
import com.querybricks.column.Sum;
import com.querybricks.column.TableColumn;
import com.querybricks.condition.And;
import com.querybricks.condition.Equals;
import com.querybricks.condition.Exists;
import com.querybricks.condition.GreaterThan;
import com.querybricks.expression.Addition;
import com.querybricks.expression.NullParameter;
import com.querybricks.expression.Parameter;
import com.querybricks.expression.Subtraction;
import com.querybricks.query.ColumnAssignment;
import com.querybricks.query.DeleteQuery;
import com.querybricks.query.InsertQuery;
import com.querybricks.query.InsertRow;
import com.querybricks.query.SelectQuery;
import com.querybricks.query.UpdateQuery;
import com.querybricks.table.DistinctTable;
import com.querybricks.table.FakeFilterableTable;
import com.querybricks.table.FilteredTable;
import com.querybricks.table.GroupedTable;
import com.querybricks.table.HavingTable;
import com.querybricks.table.InnerJoin;
import com.querybricks.table.JoinedTable;
import com.querybricks.table.LeftJoin;
import com.querybricks.table.LimitedTable;
import com.querybricks.table.OffsetTable;
import com.querybricks.table.RightJoin;
import com.querybricks.table.SubqueryTable;
import com.querybricks.table.Table;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
final class QueryBindingTest {
    @Test
    void testComplexQueryBinding() {
        FakeFilterableTable users = new FakeFilterableTable("users");
        FakeFilterableTable orders = new FakeFilterableTable("orders");

        JoinedTable joined = new JoinedTable(
            users,
            orders,
            new InnerJoin(
                new And(
                    new Equals(
                        new TableColumn(users, new RawColumn("id")),
                        new TableColumn(orders, new RawColumn("user_id"))
                    ),
                    new Equals(new TableColumn(orders, new RawColumn("status")), new Parameter("completed"))
                )
            )
        );

        FilteredTable filtered = new FilteredTable(
            joined,
            new And(
                new GreaterThan(new TableColumn(orders, new RawColumn("amount")), new Parameter(500)),
                new Equals(new TableColumn(users, new RawColumn("role")), new Parameter("admin"))
            )
        );

        GroupedTable grouped = new GroupedTable(
            filtered,
            new ColumnsSelection(new TableColumn(users, new RawColumn("country")))
        );
        Table having = new HavingTable(
            grouped,
            new GreaterThan(new Sum(new TableColumn(orders, new RawColumn("amount"))), new Parameter(10000))
        );
        DistinctTable distinct = new DistinctTable(having);
        LimitedTable limited = new LimitedTable(distinct, 10);
        OffsetTable offset = new OffsetTable(limited, 5);

        SelectQuery query = new SelectQuery(
            new ColumnsSelection(
                new TableColumn(users, new RawColumn("name")),
                new Avg(new TableColumn(orders, new RawColumn("amount"))),
                new Count(new TableColumn(orders, new RawColumn("id"))),
                new Parameter(1)
            ),
            offset
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        query.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains(
                1,
                "completed",
                500,
                "admin",
                10000,
                10,
                5
            )
        );
    }

    @Test
    void testInsertQueryBinding() {
        Table users = new FakeFilterableTable("users");
        InsertQuery query = new InsertQuery(
            users,
            List.of(new RawColumn("id"), new RawColumn("name")),
            List.of(
                new InsertRow(new Parameter(1), new Parameter("alice")),
                new InsertRow(new Parameter(2), new Parameter("bob"))
            )
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        query.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains(1, "alice", 2, "bob")
        );
    }

    @Test
    void testUpdateQueryBinding() {
        Table users = new FakeFilterableTable("users");
        UpdateQuery query = new UpdateQuery(
            users,
            List.of(
                new ColumnAssignment(new TableColumn(users, new RawColumn("status")), new Parameter("active")),
                new ColumnAssignment(new TableColumn(users, new RawColumn("score")), new Parameter(100))
            ),
            new Equals(new TableColumn(users, new RawColumn("id")), new Parameter(1))
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        query.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains("active", 100, 1)
        );
    }

    @Test
    void testDeleteQueryBinding() {
        Table users = new FakeFilterableTable("users");
        DeleteQuery query = new DeleteQuery(
            users,
            new Equals(new TableColumn(users, new RawColumn("id")), new Parameter(42))
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        query.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains(42)
        );
    }

    @Test
    void testSubqueryAndExistsBinding() {
        FakeFilterableTable users = new FakeFilterableTable("users");
        FakeFilterableTable orders = new FakeFilterableTable("orders");

        SelectQuery sub = new SelectQuery(
            new ColumnsSelection(new TableColumn(orders, new RawColumn("id"))),
            new FilteredTable(orders, new Equals(new TableColumn(orders, new RawColumn("amount")), new Parameter(250)))
        );

        SubqueryTable subTable = new SubqueryTable(orders, sub);

        SelectQuery main = new SelectQuery(
            new ColumnsSelection(new TableColumn(users, new RawColumn("name"))),
            new FilteredTable(
                users,
                new Exists(
                    new SelectQuery(
                        new ColumnsSelection(new NullParameter()),
                        new FilteredTable(
                            subTable,
                            new Equals(
                                new TableColumn(subTable, new RawColumn("user_id")),
                                new TableColumn(users, new RawColumn("id"))
                            )
                        )
                    )
                )
            )
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        main.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains(250)
        );
    }

    @Test
    void testLeftAndRightJoinBinding() {
        FakeFilterableTable users = new FakeFilterableTable("users");
        FakeFilterableTable orders = new FakeFilterableTable("orders");

        JoinedTable leftJoined = new JoinedTable(
            users,
            orders,
            new LeftJoin(new Equals(new TableColumn(orders, new RawColumn("status")), new Parameter("failed")))
        );

        JoinedTable rightJoined = new JoinedTable(
            leftJoined,
            users,
            new RightJoin(new Equals(new TableColumn(users, new RawColumn("type")), new Parameter("premium")))
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        rightJoined.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains("failed", "premium")
        );
    }

    @Test
    void testBinaryOperatorsBinding() {
        FakeFilterableTable users = new FakeFilterableTable("users");
        SelectQuery query = new SelectQuery(
            new ColumnsSelection(new TableColumn(users, new RawColumn("id"))),
            new FilteredTable(
                users,
                new Equals(
                    new TableColumn(users, new RawColumn("score")),
                    new Addition(new Parameter(10), new Subtraction(new Parameter(5), new Parameter(2)))
                )
            )
        );

        List<Object> values = new ArrayList<>();
        Bindings bindings = new Bindings() {
            @Override
            public Bindings with(Object value) {
                values.add(value);
                return this;
            }
        };

        query.bind(bindings);

        MatcherAssert.assertThat(
            values,
            Matchers.contains(10, 5, 2)
        );
    }
}
