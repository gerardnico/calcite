package com.gerardnico.calcite;

import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.rules.FilterJoinRule;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelRunners;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * Demo the filter early optimization.
 *
 * Example based
 * on <a href="https://github.com/michaelmior/calcite-notebooks/blob/master/query-optimization.ipynb">query-optimization.ipynb</a>
 *
 * The VolcanoPlanner attempts to apply a set of rules to minimize the cost of executing a query (Cost Based Optimizer)
 * VolcanoPlanner is the default planner used by Calcite.
 * Note that all the operators in the tree we constructed above are logical operators meaning
 * that they don't have a specific implementation.
 * All logical operators must be converted to physical operators before a query can actually be implemented,
 * which is required for VolcanoPlanner to provide an estimated cost.
 * Each physical operator in Calcite has a calling convention which specifies how the query will actually be executed.
 * Since we're not working with an actual database we'll use EnumerableConvention which simply implements queries over
 * collections implementing the Enumerable interface.
 *
 */
public class CalcitePlannerVolcanoTest {

    @Test
    public void basePlannerTest() throws SQLException {

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
        RelNode opTree = relBuilder
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
        CalciteRel.explain(opTree);
        /**
         * 3:LogicalFilter(condition=[=($0, 100)])
         *   2:LogicalJoin(condition=[=($1, $5)], joinType=[inner])
         *     0:LogicalTableScan(table=[[hrAndFoodmart, emps]])
         *     1:LogicalTableScan(table=[[hrAndFoodmart, depts]])
         */


        /**
         * Optimize: Push the filter (Filter Early) is implemented by the {@link FilterJoinRule}
         *
         * VolcanoPlanner has a default set of rules which includes {@link FilterJoinRule}
         */
        RelOptCluster cluster = opTree.getCluster();
        VolcanoPlanner planner = (VolcanoPlanner) cluster.getPlanner();

        RelTraitSet desiredTraits = cluster.traitSet().replace(EnumerableConvention.INSTANCE);
        RelNode newRoot = planner.changeTraits(opTree, desiredTraits);
        planner.setRoot(newRoot);

        RelNode optimized = planner.findBestExp();
        System.out.println("----------------------------");
        System.out.println("Best relational expression");
        System.out.println("----------------------------");
        CalciteRel.explain(optimized);

        System.out.println("----------------------------");
        System.out.println("Logical operators have been replaced with enumerable operators.");
        System.out.println("This means we can now execute the optimized query and get a result.");
        System.out.println("----------------------------");

//        try (ResultSet result = RelRunners.run(optimized).executeQuery()) {
//            while (result.next()) {
//                System.out.println(result.getString(1) + " " + result.getString(7));
//            }
//        }

    }
}
