package com.gerardnico.calcite;

import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.RelBuilder;
import org.junit.Test;
import  org.apache.calcite.rel.rules.FilterJoinRule;
/**
 * Example fount at <a href="https://github.com/michaelmior/calcite-notebooks/blob/master/query-optimization.ipynb">query-optimization.ipynb</a>
 */
public class CalcitePlannerHepTest {

    @Test
    public void basePlannerTest() {

        /**
         * Create a {@link RelBuilder} with a Hr reflective database
         */
        FrameworkConfig config = CalciteFramework.configHrSchemaBased();
        RelBuilder relBuilder = RelBuilder.create(config);

        /**
         * Use the {@link RelBuilder} to create an relational expression tree
         * equivalent to the below SQL
         *
         * select * from emps inner join depts on deptno
         * where empid = 100
         */
        RelNode relNode = relBuilder
                .scan("emps")
                .scan("depts")
                .join(JoinRelType.INNER, "deptno")
                .filter(relBuilder.equals(relBuilder.field("empid"), relBuilder.literal(100)))
                .build();

        /**
         * Print the tree
         */
        System.out.println("----------------------------");
        System.out.println("Build relational expression");
        System.out.println("----------------------------");
        CalciteRel.explain(relNode);
        /**
         * 3:LogicalFilter(condition=[=($0, 100)])
         *   2:LogicalJoin(condition=[=($1, $5)], joinType=[inner])
         *     0:LogicalTableScan(table=[[hrAndFoodmart, emps]])
         *     1:LogicalTableScan(table=[[hrAndFoodmart, depts]])
         */

        /**
         * Optimize: Push the filter (Filter Early) is implemented by the {@link FilterJoinRule}
         *
         */
        HepProgram hepProgram = HepProgram.builder()
                .addRuleInstance(FilterJoinRule.FILTER_ON_JOIN)
                .build();

        HepPlanner hepPlanner = new HepPlanner(hepProgram);
        hepPlanner.setRoot(relNode);
        RelNode bestRelNode = hepPlanner.findBestExp();
        System.out.println("----------------------------");
        System.out.println("Best relational expression");
        System.out.println("----------------------------");
        CalciteRel.explain(bestRelNode);

    }
}
