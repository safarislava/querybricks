package com.querybricks.example;

import com.querybricks.expression.Now;
import com.querybricks.expression.Parameter;
import com.querybricks.query.InsertQuery;
import com.querybricks.query.InsertRow;
import com.querybricks.query.Query;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.List;

final class InsertQueryExampleTest {
    private final UsersTable users = new DbUsersTable("users");

    private final Query query = new InsertQuery(
        this.users,
        List.of(
            this.users.id().unbound(),
            this.users.username().unbound(),
            this.users.status().unbound(),
            this.users.createdAt().unbound()
        ),
        List.of(
            new InsertRow(
                new Parameter<>(1),
                new Parameter<>("john"),
                new Parameter<>("active"),
                new Now()
            )
        )
    );

    @Test
    void testSql() {
        MatcherAssert.assertThat(
            this.query.sql(),
            Matchers.equalTo("INSERT INTO users (id, username, status, created_at) VALUES (?, ?, ?, NOW())")
        );
    }
}
