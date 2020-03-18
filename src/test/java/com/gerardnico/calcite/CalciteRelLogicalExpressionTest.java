package com.gerardnico.calcite;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.tools.RelBuilder;
import org.junit.Test;

public class CalciteRelLogicalExpressionTest {

    @Test
    public void tablesAsValuesTest() {
        RelBuilder builder = CalciteRel.createOrderEntryBasedRelBuilder();
        RelNode relNode = CalciteRelLogicalExpression.tableAsValues(builder);
        CalciteRel.showExplainSqlAndRun(relNode);
    }

    @Test
    public void tableScanTest() {
        RelBuilder builder = CalciteRel.createHrScottBasedRelBuilder();
        RelNode relNode = CalciteRelLogicalExpression.tableScan(builder);
        CalciteRel.showExplainSqlAndRun(relNode);
    }

    /**
     * Example modified of <a href="https://calcite.apache.org/docs/algebra.html#push-and-pop">Push and pop</a>
     * The first join has not the good key
     * <p>
     * There is also example code at <a href="https://github.com/apache/calcite/blob/master/core/src/test/java/org/apache/calcite/examples/RelBuilderExample.java#L147">example4</a>
     */
    @Test
    public void pushAndPopBushyJoinTest() {
        RelBuilder builder = CalciteRel.createOrderEntryBasedRelBuilder();
        RelNode relNode = CalciteRelLogicalExpression.bushyJoin(builder);
        CalciteRel.showExplainSqlAndRun(relNode);
    }

    @Test
    public void recursiveQueryTest() {
        RelBuilder builder = CalciteRel.createHrScottBasedRelBuilder();
        RelNode relNode = CalciteRelLogicalExpression.recursiveQuery(builder);
        CalciteRel.explain(relNode);
        // To print the Sql, an operator should be implemented
        CalciteRel.executeAndPrint(relNode);
    }


    @Test
    public void filterAndAggregateTest() {
        RelBuilder relBuilder = CalciteRel.createHrScottBasedRelBuilder();
        RelNode relNode = CalciteRelLogicalExpression.filterAndCountSumAggregate(relBuilder);
        CalciteRel.showExplainSqlAndRun(relNode);
    }

    @Test
    public void projectTest() {
        RelBuilder relBuilder = CalciteRel.createHrScottBasedRelBuilder();
        RelNode relNode = CalciteRelLogicalExpression.project(relBuilder);
        CalciteRel.showExplainSqlAndRun(relNode);
    }

}
