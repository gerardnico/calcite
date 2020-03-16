package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.RelBuilderExample;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.tools.RelBuilder;
import org.junit.Test;

public class CalciteRelExpressionTest {

    @Test
    public void relExpression() {
        RelBuilderExample.main(null);
    }

    /**
     * Example modified of <a href="https://calcite.apache.org/docs/algebra.html#push-and-pop">Push and pop</a>
     * The first join has not the good key
     *
     * There is also example code at <a href="https://github.com/apache/calcite/blob/master/core/src/test/java/org/apache/calcite/examples/RelBuilderExample.java#L147">example4</a>
     */
    @Test
    public void pushAndPopBushyJoinTest() {
        RelBuilder builder = CalciteRel.createOrderEntryBasedRelBuilder();
        final RelNode left = builder
                .scan("CUSTOMERS")
                .scan("ORDERS")
                    .join(JoinRelType.INNER, "CUSTOMER_ID")
                .build();

        System.out.println("Print the Customers/Orders SQL");
        String sql = CalciteRel.fromRelNodeToSql(left, CalciteSqlDialect.getDialect(CalciteSqlDialect.DIALECT.ANSI));
        System.out.println(sql);

        System.out.println("Execute the relational expression");
        CalciteRel.executeAndPrint(left);

        final RelNode right = builder
                .scan("ORDER_ITEMS")
                .scan("PRODUCTS")
                .join(JoinRelType.INNER, "PRODUCT_ID")
                .build();

        System.out.println("Print the OrderItems/PRODUCTS SQL");
        sql = CalciteRel.fromRelNodeToSql(right, CalciteSqlDialect.getDialect(CalciteSqlDialect.DIALECT.ANSI));
        System.out.println(sql);

        System.out.println("Execute the relational expression");
        CalciteRel.executeAndPrint(right);

        System.out.println("bushy join");
        final RelNode relNode = builder
                .push(left)
                .push(right)
                .join(JoinRelType.INNER, "ORDER_ID")
                .build();
        System.out.println("Print the relational expression");
        CalciteRel.print(relNode);
        System.out.println();

        System.out.println("Print the SQL");
        sql = CalciteRel.fromRelNodeToSql(relNode, CalciteSqlDialect.getDialect(CalciteSqlDialect.DIALECT.ANSI));
        System.out.println(sql);

        System.out.println("Execute the relational expression");
        CalciteRel.executeAndPrint(relNode);
    }

    @Test
    public void recursiveQueryTest() {
        RelBuilder builder = CalciteRel.createHrScottBasedRelBuilder();
        final RelNode relNode = builder
                .values(new String[]{"i"}, 1)
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
        System.out.println("Print the relational expression");
        CalciteRel.print(relNode);
        System.out.println();

        System.out.println("Print the SQL is not working because (org.apache.calcite.rel.logical.LogicalRepeatUnion) is not implemented");
        // CalciteRel.fromRelNodeToSql(relNode, CalciteSqlDialect.getDialect(CalciteSqlDialect.DIALECT.ANSI));

        System.out.println("Execute the relational expression");
        CalciteRel.executeAndPrint(relNode);
    }

    /**
     * Example from the doc <a href="https://calcite.apache.org/docs/algebra.html#adding-a-filter-and-aggregate">Filter and Aggregate</a>
     */
    @Test
    public void filterAndAggregateTest() {
        RelBuilder builder = CalciteRel.createHrScottBasedRelBuilder();
        final RelNode relNode = builder
                .scan("EMP")
                .aggregate(builder.groupKey("DEPTNO"),
                        builder.count(false, "C"),
                        builder.sum(false, "S", builder.field("SAL")))
                .filter(
                        builder.call(SqlStdOperatorTable.GREATER_THAN,
                                builder.field("C"),
                                builder.literal(10)))
                .build();
        System.out.println("Print the relational expression");
        CalciteRel.print(relNode);
        System.out.println();

        System.out.println("Print the SQL");
        String sql =  CalciteRel.fromRelNodeToSql(relNode, CalciteSqlDialect.getDialect(CalciteSqlDialect.DIALECT.ANSI));
        System.out.println(sql);

        System.out.println("Execute the relational expression");
        CalciteRel.executeAndPrint(relNode);
    }
}
