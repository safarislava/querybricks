```mermaid
classDiagram
    class Table~C~ {
        <<interface>>
        + schema() C
    }

    class FilterableTable~C~ {
        <<interface>>
    }
    FilterableTable ..|> Table

    class DbTable~C~ {
        -name String
        -schema C
    }
    DbTable ..|> FilterableTable
```

```mermaid
classDiagram
    class FilterableTable~C~ {
        <<interface>>
    }

    class JoinRule {
        <<interface>>
    }

    class InnerJoin
    InnerJoin ..|> JoinRule
    class LeftJoin
    LeftJoin ..|> JoinRule

    class JoinedSchema~LC, RC~ {
        + left() LC
        + right() RC
    }

    class JoinedTable~LC, RC~ {
        -left Table~LC~
        -right Table~RC~
        -rule JoinRule
    }
    JoinedTable ..|> FilterableTable
    JoinedTable --> JoinRule
    JoinedTable --> JoinedSchema
```

```mermaid
classDiagram
    class FilterableTable~C~ {
        <<interface>>
    }

    class Condition {
        <<interface>>
    }

    class Equals
    Equals ..|> Condition
    class And
    And ..|> Condition

    class ConditionFilteredTable~C~ {
        -origin FilterableTable~C~
        -condition Condition
    }
    ConditionFilteredTable ..|> FilterableTable
    ConditionFilteredTable --> Condition
```

```mermaid
classDiagram
    class Table~C~ {
        <<interface>>
    }

    class GroupedTable~C~
    GroupedTable ..|> Table

    class LimitedTable~C~
    LimitedTable ..|> Table

    class OffsetTable~C~
    OffsetTable ..|> Table

    class DistinctTable~C~
    DistinctTable ..|> Table

    %% спроектировать HAVING
```

```mermaid
classDiagram
    class Column {
        <<interface>>
    }

    class DbColumn
    DbColumn ..|> Column

    class AggregatedColumn
    AggregatedColumn ..|> Column

    class AliasedColumn {
        -origin Column
        -alias String
    }
    AliasedColumn ..|> Column
    AliasedColumn --> Column

    class Columns {
        <<interface>>
    }

    class AllColumns
    AllColumns ..|> Columns

    class ColumnsSelection {
        -columns List~Column~
    }
    ColumnsSelection ..|> Columns
    ColumnsSelection --> Column
```

```mermaid
classDiagram
    class Table~C~ {
        <<interface>>
    }

    class Columns {
        <<interface>>
    }

    class Query~C~ {
        <<interface>>
    }

    class SelectDbQuery~C~ {
        -columns Columns
        -table Table~C~
    }
    SelectDbQuery ..|> Query
    SelectDbQuery --> Columns
    SelectDbQuery --> Table

    class SubqueryTable~C~ {
        -query Query~C~
    }
    SubqueryTable ..|> Table

    %% алиасы для таблиц
```

```mermaid
classDiagram
    class Value {
        <<interface>>
    }
    
    class Literal {
        <<interface>>
    }
    Literal ..|> Value

    class StringLiteral {
        -value String
    }
    StringLiteral ..|> Literal

    class NumberLiteral {
        -value Number
    }
    NumberLiteral ..|> Literal

    class BooleanLiteral {
        -value boolean
    }
    BooleanLiteral ..|> Literal

    class NullLiteral
    NullLiteral ..|> Literal

    class FunctionCall {
        -name String
        -args List~Value~
    }
    FunctionCall ..|> Value
    FunctionCall --> Value
```

```mermaid
classDiagram
    class Table~C~ {
        <<interface>>
    }
    
    class Query~C~ {
        <<interface>>
    }

    class Value {
        <<interface>>
    }

    class Column {
        <<interface>>
    }

    class ColumnValue {
        -column Column
        -value Value
    }
    ColumnValue --> Column
    ColumnValue --> Value

    class InsertRow {
        -values List~ColumnValue~
    }
    InsertRow --> ColumnValue

    class InsertDbQuery~C~ {
        -table Table~C~
        -rows List~InsertRow~
    }
    InsertDbQuery ..|> Query
    InsertDbQuery --> Table
    InsertDbQuery --> InsertRow

```

```mermaid
classDiagram
    class ColumnAssignment {
        <<interface>>
    }
    
    class ColumnValue {
        -column Column
        -value Value
    }
    ColumnValue ..|> ColumnAssignment
    
    class ColumnExpression {
        -column Column
        -expression Expression
    }
    ColumnExpression ..|> ColumnAssignment
```

```mermaid
classDiagram
    class Table~C~ {
        <<interface>>
    }

    class Query~C~ {
        <<interface>>
    }

    class Condition {
        <<interface>>
    }

    class ColumnAssignment {
        <<interface>>
    }

    class UpdateDbQuery~C~ {
        -table Table~C~
        -assignments List~ColumnAssignment~
        -condition Condition
    }
    UpdateDbQuery ..|> Query
    UpdateDbQuery --> Table
    UpdateDbQuery --> ColumnAssignment
    UpdateDbQuery --> Condition
```
    
```mermaid
classDiagram
    class Table~C~ {
        <<interface>>
    }

    class Query~C~ {
        <<interface>>
    }

    class Condition {
        <<interface>>
    }

    class DeleteDbQuery~C~ {
        -table Table~C~
        -condition Condition
    }
    DeleteDbQuery ..|> Query
    DeleteDbQuery --> Table
    DeleteDbQuery --> Condition
```

# Пример запроса кода
## Схема данных

<!-- Это компросисс на который прошлось пойти ради статической типизации. -->
```java
class UsersSchema {
    final Column id        = new DbColumn("id");
    final Column username  = new DbColumn("username");
    final Column status    = new DbColumn("status");
    final Column createdAt = new DbColumn("created_at");
}

class OrdersSchema {
    final Column userId = new DbColumn("user_id");
    final Column amount = new DbColumn("amount");
}
```

## Select query
```java
Table<UsersSchema>  users  = new DbTable<>("users",  new UsersSchema());
Table<OrdersSchema> orders = new DbTable<>("orders", new OrdersSchema());

Table<JoinedScema<UsersSchema, OrdersSchema>> joined = new JoinedTable<>(
    users,
    orders,
    new InnerJoin(users.schema().id, orders.schema().userId)
);

Table<JoinedScema<UsersSchema, OrdersSchema>> filtered = new ConditionFiltedTable<>(
    joined,
    new Equals(joined.schema().left().status, new StringLiteral("active"))
);

Table<JoinedScema<UsersSchema, OrdersSchema>> limited = new LimitedTable<>(filtered, 10);

Query<?> query = new SelectDbQuery<>(
    new ColumnsSelection(
        joined.schema().left().id,
        joined.schema().left().username,
        new AliasedColumn(joined.schema().right().amount, "total")
    ),
    limited
);
```

### Итоговый SQL
```sql
SELECT users.id, users.username, orders.amount AS total
FROM users
JOIN orders ON users.id = orders.user_id
WHERE users.status = 'active'
LIMIT 10
```

## Insert query

```java
DbTable<UsersSchema> users = new DbTable<>("users", new UsersSchema());

Query<?> insert = new InsertDbQuery<>(
    users,
    List.of(
        new InsertRow(
            new ColumnValue(users.schema().id,        new NumberLiteral(1)),
            new ColumnValue(users.schema().username,  new StringLiteral("john")),
            new ColumnValue(users.schema().status,    new StringLiteral("active")),
            new ColumnValue(users.schema().createdAt, new FunctionCall("NOW"))
        )
    )
);
```

### Итоговый SQL
```sql
INSERT INTO users (id, username, status, created_at) VALUES (1, 'john', 'active', NOW())
```

## Update query

```java
DbTable<UsersSchema> users = new DbTable<>("users", new UsersSchema());

Query<?> update = new UpdateDbQuery<>(
    users,
    List.of(
        new ColumnValue(users.schema().status, new StringLiteral("inactive"))
    ),
    new Equals(users.schema().id, new NumberLiteral(1))
);
```

### Итоговый SQL
```sql
UPDATE users SET status = 'inactive' WHERE id = 1
```

## Delete query

```java
DbTable<UsersSchema> users = new DbTable<>("users", new UsersSchema());

Query<?> delete = new DeleteDbQuery<>(
    users,
    new Equals(users.schema().id, new NumberLiteral(1))
);
```

### Итоговый SQL
```sql
DELETE FROM users WHERE id = 1
```
