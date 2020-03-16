package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockRelOptPlanner;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.logical.LogicalIntersect;
import org.apache.calcite.rel.logical.LogicalUnion;
import org.apache.calcite.rel.rules.*;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;

/**
 *
 */
public class CalcitePlanner {

    /**
     * See org.apache.calcite.test.HepPlannerTest
     * @return
     */
    static public HepPlanner createHepPlanner(){
        HepProgram hepProgram = HepProgram.builder().build();
        HepPlanner hepPlanner = new HepPlanner(hepProgram);
        hepPlanner.addRule(JoinToMultiJoinRule.INSTANCE);
        hepPlanner.addRule(LoptOptimizeJoinRule.INSTANCE);
        hepPlanner.addRule(MultiJoinOptimizeBushyRule.INSTANCE);
        hepPlanner.addRule(JoinPushThroughJoinRule.LEFT);
        hepPlanner.addRule(new CoerceInputsRule(LogicalUnion.class, false,RelFactories.LOGICAL_BUILDER));
        hepPlanner.addRule(new CoerceInputsRule(LogicalIntersect.class, false,RelFactories.LOGICAL_BUILDER));
        return hepPlanner;
    }

    static public final RelOptPlanner getMockRelOptPlanner() {

        return new MockRelOptPlanner(CalciteConnections.getContext());

    }

    static public final Planner getPlannerFromFrameworkConfig(FrameworkConfig frameworkConfig){
        return Frameworks.getPlanner(frameworkConfig);
    }

}
