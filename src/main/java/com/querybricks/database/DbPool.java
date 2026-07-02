package com.querybricks.database;

import com.querybricks.query.Query;
import com.querybricks.query.ResultedQuery;

import java.util.List;

/**
 * A database connection pool interface for executing queries and fetching results.
 */
public interface DbPool {
    /**
     * Executes a query that does not return results (e.g., INSERT, UPDATE, DELETE).
     *
     * @param query the query to execute
     */
    void execute(Query query);

    /**
     * Executes a query that returns a list of result rows (e.g., SELECT).
     *
     * @param query the query to execute
     * @return a list of result rows
     */
    List<Row> selection(ResultedQuery query);
}
