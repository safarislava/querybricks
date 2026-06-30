# Доменная модель

## Иерархия основных таблиц

```mermaid
classDiagram
    class Table {
        <<interface>>
    }

    class FilterableTable {
        <<interface>>
    }
    FilterableTable ..|> Table

    class HavableTable {
        <<interface>>
    }
    HavableTable ..|> Table

    class WrappedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }
    WrappedTable ..|> Table
    WrappedTable ..> Table

    class BinaryTable~L, R~ {
        <<interface>>
        + left() L
        + right() R
    }
    BinaryTable ..> Table
```

## Соединение таблиц

```mermaid
classDiagram
    class FilterableTable {
        <<interface>>
    }

    class JoinRule {
        <<interface>>
        + sql(Table) String
    }
    
    class Condition {
        <<interface>>
    }
    InnerJoin --> Condition
    LeftJoin --> Condition

    class InnerJoin {
        -condition Condition
    }
    InnerJoin ..|> JoinRule

    class LeftJoin {
        -condition Condition
    }
    LeftJoin ..|> JoinRule

    class BinaryTable~L, R~ {
        <<interface>>
        + left() L
        + right() R
    }

    class JoinedTable~L, R~ {
        -left L
        -right R
        -joinRule JoinRule
    }
    JoinedTable ..|> FilterableTable
    JoinedTable ..|> BinaryTable~L, R~
    JoinedTable --> JoinRule

    class Table {
        <<interface>>
    }
    JoinedTable --> Table
    BinaryTable ..> Table
```

## Фильтрация и подзапросы

```mermaid
classDiagram
    class Table {
        <<interface>>
    }
    
    class Query {
        <<interface>>
    }

    class FilterableTable {
        <<interface>>
    }
    FilterableTable ..|> Table

    class Condition {
        <<interface>>
    }

    class WrappedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }
    WrappedTable ..|> Table

    class FilteredTable~T extends FilterableTable~ {
        -table T
        -condition Condition
    }
    FilteredTable ..|> WrappedTable~T~
    FilteredTable --> Condition

    class SubqueryTable~T extends FilterableTable~ {
        -table T
        -query Query
    }
    SubqueryTable --> Query
    SubqueryTable ..|> WrappedTable~T~
    SubqueryTable ..|> FilterableTable
```

## Группировка

```mermaid
classDiagram
    class HavableTable {
        <<interface>>
    }

    class WrappedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }

    class GroupedTable~T extends Table~ {
        -table T
        -columns Columns
    }
    GroupedTable ..|> HavableTable
    GroupedTable ..|> WrappedTable~T~

    class HavingTable~T extends HavableTable~ {
        -table T
        -condition Condition
    }
    HavingTable ..|> WrappedTable~T~
```

## Модификаторы

```mermaid
classDiagram
    class WrappedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }

    class LimitedTable~T extends Table~ {
        -origin T
        -limit int
    }
    LimitedTable ..|> WrappedTable~T~

    class OffsetTable~T extends Table~ {
        -origin T
        -offset int
    }
    OffsetTable ..|> WrappedTable~T~

    class DistinctTable~T extends Table~ {
        -origin T
    }
    DistinctTable ..|> WrappedTable~T~
```

## Колонки и их выборка

```mermaid
classDiagram
    class Expression {
        <<interface>>
    }

    class Column~T~ {
        <<interface>>
    }
    Column ..|> Expression

    class UnboundColumn~T~ {
        <<interface>>
    }
    UnboundColumn ..|> Column~T~

    class BoundColumn~T~ {
        <<interface>>
        +unbound() UnboundColumn~T~
    }
    BoundColumn ..|> Column~T~

    class RawColumn~T~ {
        -name String
    }
    RawColumn ..|> UnboundColumn~T~

    class Table {
        <<interface>>
    }

    class TableColumn~T~ {
        -table Table
        -column UnboundColumn~T~
    }
    TableColumn ..|> BoundColumn~T~
    TableColumn --> UnboundColumn
    TableColumn --> Table

    class AggregatedColumn~T~ {
        <<interface>>
    }
    AggregatedColumn ..|> Column~T~

    class Sum~T~ {
        -column Column~T~
    }
    Sum ..|> AggregatedColumn~T~

    class Count~T~ {
        -column Column~T~
    }
    Count ..|> AggregatedColumn~T~

    class Avg~T~ {
        -column Column~T~
    }
    Avg ..|> AggregatedColumn~T~

    class Columns {
        <<interface>>
    }

    class AllColumns
    AllColumns ..|> Columns

    class ColumnsSelection {
        -columns List~Column~
    }
    ColumnsSelection ..|> Columns
```

## Рендеринг в SQL

```mermaid
classDiagram
    class QueryPart {
        <<interface>>
        + sql() String
    }

    class Table {
        <<interface>>
    }
    Table ..|> QueryPart

    class Expression {
        <<interface>>
    }
    Expression ..|> QueryPart

    class Condition {
        <<interface>>
    }
    Condition ..|> QueryPart

    class Columns {
        <<interface>>
    }
    Columns ..|> QueryPart


    class Query {
        <<interface>>
    }
    Query ..|> QueryPart
```

## Выражения

```mermaid
classDiagram
    class Expression {
        <<interface>>
    }

    class Literal {
        <<interface>>
    }
    Literal ..|> Expression

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

    class Function {
        <<interface>>
    }
    Function ..|> Expression

    class Now
    Now ..|> Function

    class BinaryOperator {
        <<interface>>
    }
    BinaryOperator ..|> Expression

    class Addition {
        -left Expression
        -right Expression
    }
    Addition ..|> BinaryOperator
    Addition --> Expression

    class Subtraction {
        -left Expression
        -right Expression
    }
    Subtraction ..|> BinaryOperator
    Subtraction --> Expression
```

## Структура запроса SELECT

```mermaid
classDiagram
    class Table {
        <<interface>>
    }

    class Query {
        <<interface>>
    }

    class Columns {
        <<interface>>
    }

    class SelectQuery {
        -columns Columns
        -table Table
    }
    SelectQuery ..|> Query
```

## Структура запроса INSERT

```mermaid
classDiagram
    class Table {
        <<interface>>
    }
    
    class Query {
        <<interface>>
    }

    class Expression {
        <<interface>>
    }

    class UnboundColumn {
        <<interface>>
    }

    class InsertRow {
        -values List~Expression~
    }
    InsertRow --> Expression

    class InsertQuery {
        -table Table
        -columns List~UnboundColumn~
        -rows List~InsertRow~
    }
    InsertQuery ..|> Query
    InsertQuery --> Table
    InsertQuery --> UnboundColumn
    InsertQuery --> InsertRow
```

## Структура запроса UPDATE

```mermaid
classDiagram
    class Table {
        <<interface>>
    }

    class Query {
        <<interface>>
    }

    class Condition {
        <<interface>>
    }

    class ColumnExpression {
        -column Column
        -value Expression
    }

    class UpdateQuery {
        -table Table
        -assignments List~ColumnExpression~
        -condition Condition
    }
    UpdateQuery ..|> Query
    UpdateQuery --> Table
    UpdateQuery --> ColumnExpression
    UpdateQuery --> Condition
```

## Структура запроса DELETE

```mermaid
classDiagram
    class Table {
        <<interface>>
    }

    class Query {
        <<interface>>
    }

    class Condition {
        <<interface>>
    }

    class DeleteQuery {
        -table Table
        -condition Condition
    }
    DeleteQuery ..|> Query
    DeleteQuery --> Table
    DeleteQuery --> Condition
```

## Выполнение запроса и интерпретация результата

```mermaid
classDiagram
    class Query {
        <<interface>>
    }

    class Database {
        <<interface>>
        + execute(sql) void
        + query(query) Rows
    }

    class Rows {
        <<interface>>
        + list() List~Row~
    }

    class Column~T~ {
        <<interface>>
    }

    class Row {
        <<interface>>
        + value(column) T
    }

    Database ..> Query
    Database ..> Rows
    Rows o-- Row 
    Row ..> Column
```
