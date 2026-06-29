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

    class WrapedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }
    WrapedTable ..|> Table
    WrapedTable ..> Table

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
        -on Condition
    }
    InnerJoin ..|> JoinRule

    class LeftJoin {
        -on Condition
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
        -rule JoinRule
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

    class WrapedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }
    WrapedTable ..|> Table

    class FilteredTable~T extends FilterableTable~ {
        -origin T
        -condition Condition
    }
    FilteredTable ..|> WrapedTable~T~
    FilteredTable --> Condition

    class SubqueryTable~T extends FilterableTable~ {
        -origin T
        -query Query
    }
    SubqueryTable --> Query
    SubqueryTable ..|> WrapedTable~T~
    SubqueryTable ..|> FilterableTable
```

## Группировка

```mermaid
classDiagram
    class HavableTable {
        <<interface>>
    }

    class WrapedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }

    class GroupedTable~T extends Table~ {
        -origin T
        -by List~Column~
    }
    GroupedTable ..|> HavableTable
    GroupedTable ..|> WrapedTable~T~

    class HavingTable~T extends HavableTable~ {
        -origin T
        -condition Condition
    }
    HavingTable ..|> WrapedTable~T~
```

## Модификаторы

```mermaid
classDiagram
    class WrapedTable~T extends Table~ {
        <<interface>>
        + origin() T
    }

    class LimitedTable~T extends Table~ {
        -origin T
        -limit int
    }
    LimitedTable ..|> WrapedTable~T~

    class OffsetTable~T extends Table~ {
        -origin T
        -offset int
    }
    OffsetTable ..|> WrapedTable~T~

    class DistinctTable~T extends Table~ {
        -origin T
    }
    DistinctTable ..|> WrapedTable~T~
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

    class DbColumn~T~ {
        -name String
    }
    DbColumn ..|> Column~T~

    class Table {
        <<interface>>
    }

    class TableColumn~T~ {
        -table Table
        -column Column~T~
    }
    TableColumn ..|> Column~T~
    TableColumn --> Column
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

    class AliasedColumn~T~ {
        -origin Column~T~
        -alias String
    }
    AliasedColumn ..|> Column~T~

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

    class Column {
        <<interface>>
    }

    class ColumnExpression {
        -column Column
        -value Expression
    }
    ColumnExpression --> Column
    ColumnExpression --> Expression

    class InsertRow {
        -values List~ColumnExpression~
    }
    InsertRow --> ColumnExpression

    class InsertQuery {
        -table Table
        -rows List~InsertRow~
    }
    InsertQuery ..|> Query
    InsertQuery --> Table
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
