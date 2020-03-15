package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.JdbcStore;
import com.gerardnico.calcite.demo.RelBuilderExample;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelRunners;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelExpressionTest {

    @Test
    public void relExpression() {
        RelBuilderExample.main(null);
    }

    /**
     * Print a sequence of 1 to 10
     *
     * WITH RECURSIVE aux(i) AS (
     *   VALUES (1)
     *   UNION ALL
     *   SELECT i+1 FROM aux WHERE i < 10
     * )
     * SELECT * FROM aux
     *
     * https://calcite.apache.org/docs/algebra.html#recursive-queries
     *
     */
    @Test
    public void recursiveQueryTest() throws SQLException {
        RelBuilder builder = CalciteRel.getScott();
        final RelNode relNode = builder
                .values(new String[] { "i" }, 1)
                .transientScan("aux")
                .filter(
                        builder.call(
                                SqlStdOperatorTable.LESS_THAN,
                                builder.field(0),
                                builder.literal(10)))
                .project(
                        builder.call(
                                SqlStdOperatorTable.PLUS,
                                builder.field(0),
                                builder.literal(1)))
                .repeatUnion("aux", true)
                .build();
        CalciteRel.print(relNode);
        CalciteRel.executeQueryAndPrint(relNode);
    }
}
