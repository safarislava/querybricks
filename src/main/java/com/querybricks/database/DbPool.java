package com.querybricks.database;

import com.querybricks.query.Query;
import com.querybricks.query.ResultedQuery;

import java.util.List;

public interface DbPool {
    void execute(Query query);
    List<Row> selection(ResultedQuery query);
}
