package com.querybricks.example;

import com.querybricks.column.BoundColumn;
import com.querybricks.table.FilterableTable;

import java.math.BigDecimal;

public interface OrdersTable extends FilterableTable {
    BoundColumn<Long> userId();
    BoundColumn<BigDecimal> amount();
}
