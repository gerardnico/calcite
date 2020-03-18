package com.gerardnico.calcite;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.tools.RelBuilder;

/**
 * Example that uses {@link org.apache.calcite.tools.RelBuilder} with an HR schema
 * (Example: {@link CalciteRel#createHrScottBasedRelBuilder()})
 * to create various relational expressions
 *
 * <a href=https://calcite.apache.org/docs/algebra.html#api-summary>API summary</a>
 */
public class CalciteRelLogicalExpression {

    /**
     * Example from the doc <a href="https://calcite.apache.org/docs/algebra.html#adding-a-filter-and-aggregate">Filter and Aggregate</a>
     */
    public static RelNode filterAndCountSumAggregate(RelBuilder builder) {
        return builder
                .scan("EMP")
                .aggregate(builder.groupKey("DEPTNO"),
                        builder.count(false, "C"),
                        builder.sum(false, "S", builder.field("SAL")))
                .filter(
                        builder.call(SqlStdOperatorTable.GREATER_THAN,
                                builder.field("C"),
                                builder.literal(3)))
                .build();
    }

    /**
     * Example modified of <a href="https://calcite.apache.org/docs/algebra.html#push-and-pop">Push and pop</a>
     * The first join has not the good key
     * <p>
     * There is also example code at <a href="https://github.com/apache/calcite/blob/master/core/src/test/java/org/apache/calcite/examples/RelBuilderExample.java#L147">example4</a>
     * <p>
     * Sometimes the stack becomes so deeply nested it gets confusing. To keep
     * things straight, you can remove expressions from the stack. For example,
     * here we are building a bushy join:
     *
     * <blockquote><pre>
     *                join
     *              /      \
     *         join          join
     *       /      \      /      \
     * CUSTOMERS ORDERS LINE_ITEMS PRODUCTS
     * </pre></blockquote>
     *
     * <p>We build it in three stages. Store the intermediate results in variables
     * `left` and `right`, and use `push()` to put them back on the stack when it
     * is time to create the final `Join`.
     */
    public static RelNode bushyJoin(RelBuilder relBuilder) {
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
        return builder
                .push(left)
                .push(right)
                .join(JoinRelType.INNER, "ORDER_ID")
                .build();
    }

    /**
     * Creates a relational expression for a table scan.
     * It is equivalent to
     *
     * <blockquote><pre>SELECT *
     * FROM emp</pre></blockquote>
     *
     * @return
     */
    public static RelNode tableAsValues(RelBuilder builder) {
        return builder
                .values(new String[]{"a", "b"}, 1, true, null, false)
                .build();
    }


    /**
     * Creates a relational expression for a table scan.
     * It is equivalent to
     *
     * <blockquote><pre>SELECT *
     * FROM emp</pre></blockquote>
     */
    public static RelNode tableScan(RelBuilder builder) {
        return builder
                .scan("EMP")
                .build();
    }

    /**
     * Creates a relational expression for a table scan and project.
     * It is equivalent to
     *
     * <blockquote><pre>SELECT deptno, ename
     * FROM emp</pre></blockquote>
     *
     * @return
     */
    public static RelNode project(RelBuilder builder) {
        return builder
                .scan("EMP")
                .project(builder.field("DEPTNO"), builder.field("ENAME"))
                .build();
    }

    public static RelNode recursiveQuery(RelBuilder builder) {
        return builder
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
    }

}
