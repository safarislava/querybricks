# QueryBricks

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](#)
[![Java Version](https://img.shields.io/badge/java-21-blue.svg)](#)
[![Paradigm](https://img.shields.io/badge/paradigm-elegant_objects-orange.svg)](#)

**QueryBricks** is a declarative, object-oriented SQL query builder for Java, designed strictly following the **Elegant Objects (EO)** paradigm.

---

## Key Features

* **Strict OOP / Elegant Objects**:
  * **Zero Nulls**: No method ever accepts or returns `null`.
  * **No Getters/Setters**: Objects do not expose their internal state. You interact with objects exclusively through their behavior.
  * **True Immutability**: All objects are completely immutable once created.
  * **No Static Methods**: All behavior is polymorphic and resides in instance methods.
* **Declarative**: Queries are built a composition tree of objects.
* **Dynamic Construction**: Queries are built dynamically, allows to specify existing one.
* **Lazy SQL Generation**: The SQL string is constructed lazily and generated only on-demand.
* **Type-Safe**: Typed interfaces protect against database type mismatches.

---

## Installation

To use **QueryBricks** in your project, add the following dependency configuration:

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("com.querybricks:querybricks:0.1.0")
}
```

### Maven
```xml
<dependency>
    <groupId>com.querybricks</groupId>
    <artifactId>querybricks</artifactId>
    <version>0.1.0</version>
</dependency>
```

---

## Examples of Usage

```java
UsersTable  users  = new DbUsersTable("users");
OrdersTable orders = new DbOrdersTable("orders");

var query = new SelectQuery(
    new ColumnsSelection(
        users.id(),
        users.username(),
        orders.amount()
    ),
    new LimitedTable<>(
        new FilteredTable<>(
            new JoinedTable<>(
                users,
                orders,
                new InnerJoin(new Equals(users.id(), orders.userId()))
            ),
            new Equals(users.status(), new StringLiteral("active"))
        ),
        10
    )
);

System.out.println(query.sql());
```
**Output:**
```sql
SELECT users.id, users.username, orders.amount FROM users INNER JOIN orders ON users.id = orders.user_id WHERE users.status = 'active' LIMIT 10
```
