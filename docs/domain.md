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

    class SelectionDbQuery~C~ {
        -columns Columns
        -table Table~C~
    }
    SelectionDbQuery ..|> Query
    SelectionDbQuery --> Columns
    SelectionDbQuery --> Table

    class SubqueryTable~C~ {
        -query Query~C~
    }
    SubqueryTable ..|> Table

    %% алиасы для таблиц и столбцов
```

# Пример запроса кода
## Схема данных

<!-- Это компросисс на который прошлось пойти ради статической типизации. -->
```java
class UsersSchema {
    final Column id = new DbColumn("id");
    final Column username = new DbColumn("username");
    final Column status = new DbColumn("status");
}

class OrdersSchema {
    final Column userId = new DbColumn("user_id");
    final Column amount = new DbColumn("amount");
}
```

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
    new Equals(joined.schema().left().status, new Literal("active"))
);

Table<JoinedScema<UsersSchema, OrdersSchema>> limited = new LimitedTable<>(filtered, 10);

Query<?> query = new SelectionDbQuery<>(
    new ColumnsSelection(
        joined.schema().left().id,
        joined.schema().left().username,
        joined.schema().right().amount
    ),
    limited
);
```

## Итоговый SQL
```sql
SELECT users.id, users.username, orders.amount
FROM users
JOIN orders ON users.id = orders.user_id
WHERE users.status = 'active'
LIMIT 10
```
