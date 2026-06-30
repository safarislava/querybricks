# Пример кода
## Схема данных

```java
interface UsersTable extends FilterableTable {
    BindedColumn<Long> id();
    BindedColumn<String> username();
    BindedColumn<String> status();
    BindedColumn<Instant> createdAt();
}

interface OrdersTable extends FilterableTable {
    BindedColumn<Long> userId();
    BindedColumn<BigDecimal> amount();
}

class DbUsersTable implements UsersTable {
    private final String name;
    DbUsersTable(String name) { this.name = name; }
    public BindedColumn<Long> id()        { return new TableColumn<>(this, new RawColumn<>("id")); }
    public BindedColumn<String> username()  { return new TableColumn<>(this, new RawColumn<>("username")); }
    public BindedColumn<String> status()    { return new TableColumn<>(this, new RawColumn<>("status")); }
    public BindedColumn<Instant> createdAt() { return new TableColumn<>(this, new RawColumn<>("created_at")); }
}

class DbOrdersTable implements OrdersTable {
    private final String name;
    DbOrdersTable(String name) { this.name = name; }
    public BindedColumn<Long> userId() { return new TableColumn<>(this, new RawColumn<>("user_id")); }
    public BindedColumn<BigDecimal> amount() { return new TableColumn<>(this, new RawColumn<>("amount")); }
}
```

## Запрос SELECT
```java
UsersTable  users  = new DbUsersTable("users");
OrdersTable orders = new DbOrdersTable("orders");

var joined = new JoinedTable<>(
    users,
    orders,
    new InnerJoin(new Equals(users.id(), orders.userId()))
);

var filtered = new FilteredTable<>(
    joined,
    new Equals(joined.left().status(), new StringLiteral("active"))
);

var limited = new LimitedTable<>(filtered, 10);

Column<Long>       id       = limited.origin().origin().left().id();
Column<String>     username = limited.origin().origin().left().username();
Column<BigDecimal> amount   = limited.origin().origin().right().amount();

Query query = new SelectQuery(
    new ColumnsSelection(id, username, amount),
    limited
);

Rows result = database.selection(query);
for (Row row : result.list()) {
    Long userId = row.value(id);
    String name = row.value(username);
    BigDecimal total = row.value(amount);
}
```

### Итоговый SQL
```sql
SELECT users.id, users.username, orders.amount AS total
FROM users
JOIN orders ON users.id = orders.user_id
WHERE users.status = 'active'
LIMIT 10
```

## Запрос SELECT c агрегирующей функцией

```java
UsersTable  users  = new DbUsersTable("users");
OrdersTable orders = new DbOrdersTable("orders");

var joined = new JoinedTable<>(
    users,
    orders,
    new InnerJoin(new Equals(users.id(), orders.userId()))
);

var grouped = new GroupedTable<>(joined, new ColumnsSelection(joined.left().status()));

Column<String>     status      = grouped.origin().left().status();
Column<BigDecimal> totalAmount = new Sum<>(grouped.origin().right().amount());

Query query = new SelectQuery(
    new ColumnsSelection(status, totalAmount),
    grouped
);

Rows result = database.selection(query);
for (Row row : result.list()) {
    String userStatus = row.value(status);
    BigDecimal total = row.value(totalAmount);
}
```

### Итоговый SQL
```sql
SELECT users.status, SUM(orders.amount)
FROM users
JOIN orders ON users.id = orders.user_id
GROUP BY users.status
```

## Запрос INSERT

```java
UsersTable users = new DbUsersTable("users");

Query insert = new InsertQuery(
    users,
    List.of(users.id().unbound(), users.username().unbound(), users.status().unbound(), users.createdAt().unbound()),
    List.of(
        new InsertRow(
            new NumberLiteral(1),
            new StringLiteral("john"),
            new StringLiteral("active"),
            new Now()
        )
    )
);

database.execute(insert.sql());
```

### Итоговый SQL
```sql
INSERT INTO users (id, username, status, created_at) VALUES (1, 'john', 'active', NOW())
```

## Запрос UPDATE

```java
UsersTable users = new DbUsersTable("users");

Query update = new UpdateQuery(
    users,
    List.of(
        new ColumnExpression(users.status(), new StringLiteral("inactive"))
    ),
    new Equals(users.id(), new NumberLiteral(1))
);

database.execute(update.sql());
```

### Итоговый SQL
```sql
UPDATE users SET status = 'inactive' WHERE id = 1
```

## Запрос DELETE

```java
UsersTable users = new DbUsersTable("users");

Query delete = new DeleteQuery(
    users,
    new Equals(users.id(), new NumberLiteral(1))
);

database.execute(delete.sql());
```

### Итоговый SQL
```sql
DELETE FROM users WHERE id = 1
```

## Подзапросы

```java
UsersTable users = new DbUsersTable("users");
OrdersTable orders = new DbOrdersTable("orders");

Query filteredQuery = new SelectQuery(
    new ColumnsSelection(orders.userId(), orders.amount()),
    new FilteredTable<>(
        orders,
        new GreaterThan(orders.amount(), new NumberLiteral(1000))
    )
);

SubqueryTable<OrdersTable> subquery = new SubqueryTable<>(orders, filteredQuery);

Query existsSubquery = new SelectQuery(
    new ColumnsSelection(new NumberLiteral(1)),
    new FilteredTable<>(
        subquery,
        new Equals(subquery.origin().userId(), users.id())
    )
);

Query query = new SelectQuery(
    new ColumnsSelection(users.username()),
    new FilteredTable<>(
        users,
        new Exists(existsSubquery)
    )
);

database.selection(query);
```

### Итоговый SQL
```sql
SELECT username 
FROM users 
WHERE EXISTS (
    SELECT 1 
    FROM (
        SELECT orders.user_id, orders.amount 
        FROM orders 
        WHERE orders.amount > 1000
    ) 
    WHERE orders.user_id = users.id
)
```
