# Пример кода
## Схема данных

```java
interface UsersTable extends FilterableTable {
    BoundColumn<Long> id();
    BoundColumn<String> username();
    BoundColumn<String> status();
    BoundColumn<Instant> createdAt();
}

interface OrdersTable extends FilterableTable {
    BoundColumn<Long> userId();
    BoundColumn<BigDecimal> amount();
}

class DbUsersTable implements UsersTable {
    private final String name;
    DbUsersTable(String name) { this.name = name; }
    public BoundColumn<Long> id()        { return new TableColumn<>(this, new RawColumn<>("id")); }
    public BoundColumn<String> username()  { return new TableColumn<>(this, new RawColumn<>("username")); }
    public BoundColumn<String> status()    { return new TableColumn<>(this, new RawColumn<>("status")); }
    public BoundColumn<Instant> createdAt() { return new TableColumn<>(this, new RawColumn<>("created_at")); }

    @Override
    public List<BoundColumn<?>> columns() {
        return List.of(id(), username(), status(), createdAt());
    }
}

class DbOrdersTable implements OrdersTable {
    private final String name;
    DbOrdersTable(String name) { this.name = name; }
    public BoundColumn<Long> userId() { return new TableColumn<>(this, new RawColumn<>("user_id")); }
    public BoundColumn<BigDecimal> amount() { return new TableColumn<>(this, new RawColumn<>("amount")); }

    @Override
    public List<BoundColumn<?>> columns() {
        return List.of(userId(), amount());
    }
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
    new Equals(joined.left().status(), new Parameter<>("active"))
);

var limited = new LimitedTable<>(filtered, 10);

BoundColumn<Long>       id       = limited.origin().origin().left().id();
BoundColumn<String>     username = limited.origin().origin().left().username();
BoundColumn<BigDecimal> amount   = limited.origin().origin().right().amount();

ResultedQuery query = new SelectQuery(
    new ColumnsSelection(id, username, amount),
    limited
);

DbPool database = new DataSourcePool(dataSource);
List<Row> result = database.selection(query);
for (Row row : result) {
    Long userId = row.value(id);
    String name = row.value(username);
    BigDecimal total = row.value(amount);
}
```

### Итоговый SQL
```sql
SELECT users.id, users.username, orders.amount FROM users INNER JOIN orders ON users.id = orders.user_id WHERE users.status = ? LIMIT ?
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

BoundColumn<String> status      = grouped.origin().left().status();
Column<BigDecimal>  totalAmount = new Sum<>(grouped.origin().right().amount());

ResultedQuery query = new SelectQuery(
    new ColumnsSelection(status, totalAmount),
    grouped
);

DbPool database = new DataSourcePool(dataSource);
List<Row> result = database.selection(query);
for (Row row : result) {
    String userStatus = row.value(status);
    BigDecimal total = row.value(totalAmount);
}
```

### Итоговый SQL
```sql
SELECT users.status, SUM(orders.amount) FROM users INNER JOIN orders ON users.id = orders.user_id GROUP BY users.status
```

## Запрос INSERT

```java
UsersTable users = new DbUsersTable("users");

Query insert = new InsertQuery(
    users,
    List.of(users.id().unbound(), users.username().unbound(), users.status().unbound(), users.createdAt().unbound()),
    List.of(
        new InsertRow(
            new Parameter<>(1),
            new Parameter<>("john"),
            new Parameter<>("active"),
            new Now()
        )
    )
);

DbPool database = new DataSourcePool(dataSource);
database.execute(insert);
```

### Итоговый SQL
```sql
INSERT INTO users (id, username, status, created_at) VALUES (?, ?, ?, NOW())
```

## Запрос UPDATE

```java
UsersTable users = new DbUsersTable("users");

Query update = new UpdateQuery(
    users,
    List.of(
        new ColumnAssignment(users.status(), new Parameter<>("inactive"))
    ),
    new Equals(users.id(), new Parameter<>(1))
);

DbPool database = new DataSourcePool(dataSource);
database.execute(update);
```

### Итоговый SQL
```sql
UPDATE users SET status = ? WHERE id = ?
```

## Запрос DELETE

```java
UsersTable users = new DbUsersTable("users");

Query delete = new DeleteQuery(
    users,
    new Equals(users.id(), new Parameter<>(1))
);

DbPool database = new DataSourcePool(dataSource);
database.execute(delete);
```

### Итоговый SQL
```sql
DELETE FROM users WHERE id = ?
```

## Подзапросы

```java
UsersTable users = new DbUsersTable("users");
OrdersTable orders = new DbOrdersTable("orders");

Query filteredQuery = new SelectQuery(
    new ColumnsSelection(orders.userId(), orders.amount()),
    new FilteredTable<>(
        orders,
        new GreaterThan(orders.amount(), new Parameter<>(1000))
    )
);

SubqueryTable<OrdersTable> subquery = new SubqueryTable<>(orders, filteredQuery);

Query existsSubquery = new SelectQuery(
    new ColumnsSelection(new Parameter<>(1)),
    new FilteredTable<>(
        subquery,
        new Equals(subquery.origin().userId(), users.id())
    )
);

ResultedQuery query = new SelectQuery(
    new ColumnsSelection(users.username()),
    new FilteredTable<>(
        users,
        new Exists(existsSubquery)
    )
);

DbPool database = new DataSourcePool(dataSource);
database.selection(query);
```

### Итоговый SQL
```sql
SELECT username FROM users WHERE EXISTS (SELECT ? FROM (SELECT orders.user_id, orders.amount FROM orders WHERE orders.amount > ?) WHERE orders.user_id = users.id)
```

## Выполнение запросов

```java
import com.querybricks.database.DbPool;
import com.querybricks.database.DataSourcePool;
import org.h2.jdbcx.JdbcDataSource;

JdbcDataSource dataSource = new JdbcDataSource();
dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
dataSource.setUser("sa");
dataSource.setPassword("");

DbPool database = new DataSourcePool(dataSource);
```

### Выполнение запросов на чтение

```java
ResultedQuery select = new SelectQuery(
    new ColumnsSelection(users.id(), users.username()),
    users
);

List<Row> rows = database.selection(select);
for (Row row : rows) {
    Long id = row.value(users.id());
    String name = row.value(users.username());
    System.out.println("User: " + id + " - " + name);
}
```

### Выполнение запросов на изменение 

Для изменения данных используется метод `execute()`, принимающий обычный `Query`:

```java
Query insert = new InsertQuery(
    users,
    List.of(users.username().unbound()),
    List.of(new InsertRow(new Parameter<>("alice")))
);
database.execute(insert);

Query update = new UpdateQuery(
    users,
    List.of(new ColumnAssignment(users.status(), new Parameter<>("active"))),
    new Equals(users.username(), new Parameter<>("alice"))
);
database.execute(update);

Query delete = new DeleteQuery(
    users,
    new Equals(users.username(), new Parameter<>("alice"))
);
database.execute(delete);
```
