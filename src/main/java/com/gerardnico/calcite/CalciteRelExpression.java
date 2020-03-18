package com.gerardnico.calcite;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.tools.RelBuilder;

public class CalciteRelExpression {

    /**
     * https://calcite.apache.org/docs/algebra.html#adding-a-filter-and-aggregate
     * @param builder
     * @return
     */
    public static RelNode AggregateCountSum(RelBuilder builder) {
        return  builder
                .scan("EMP")
                .aggregate(builder.groupKey("DEPTNO"),
                        builder.count(false, "C"),
                        builder.sum(false, "S", builder.field("SAL")))
                .filter(
                        builder.call(SqlStdOperatorTable.GREATER_THAN,
                                builder.field("C"),
                                builder.literal(10)))
                .build();

    }

}
