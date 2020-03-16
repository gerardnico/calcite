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
        RelBuilder builder = CalciteRel.createScottBasedRelBuilder();
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
}
