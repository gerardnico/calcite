package com.gerardnico.calcite;

import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.logical.LogicalIntersect;
import org.apache.calcite.rel.logical.LogicalUnion;
import org.apache.calcite.rel.rules.*;

/**
 * See
 * org.apache.calcite.test.HepPlannerTest
 */
public class CalcitePlanner {

    static public HepPlanner createPlanner(){
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
}
