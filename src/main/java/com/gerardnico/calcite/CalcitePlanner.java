package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockRelOptPlanner;
import org.apache.calcite.interpreter.InterpretableConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.logical.LogicalIntersect;
import org.apache.calcite.rel.logical.LogicalUnion;
import org.apache.calcite.rel.rules.*;
import org.apache.calcite.tools.*;

/**
 *
 */
public class CalcitePlanner {

    /**
     * See org.apache.calcite.test.HepPlannerTest
     *
     * @return
     */
    static public HepPlanner createHepPlanner() {
        HepProgram hepProgram = HepProgram.builder().build();
        HepPlanner hepPlanner = new HepPlanner(hepProgram);
        hepPlanner.addRule(JoinToMultiJoinRule.INSTANCE);
        hepPlanner.addRule(LoptOptimizeJoinRule.INSTANCE);
        hepPlanner.addRule(MultiJoinOptimizeBushyRule.INSTANCE);
        hepPlanner.addRule(JoinPushThroughJoinRule.LEFT);
        hepPlanner.addRule(new CoerceInputsRule(LogicalUnion.class, false, RelFactories.LOGICAL_BUILDER));
        hepPlanner.addRule(new CoerceInputsRule(LogicalIntersect.class, false, RelFactories.LOGICAL_BUILDER));
        return hepPlanner;
    }

    static public final RelOptPlanner getMockRelOptPlanner() {

        return new MockRelOptPlanner(CalciteConnections.getContext());

    }

    static public final Planner getPlannerFromFrameworkConfig(FrameworkConfig frameworkConfig) {
        return Frameworks.getPlanner(frameworkConfig);
    }

    public static VolcanoPlanner createVolcanoPlanner() {
        return new VolcanoPlanner();
    }

    /**
     * A sample code to show how you can optimize with Volcano
     * @param relRoot
     * @return
     */
    public static RelNode optimizeWithVolcano(RelRoot relRoot) {

        // Create the volcano planner
        VolcanoPlanner volcanoPlanner = createVolcanoPlanner();

        // A set of rules to apply
        Program program = Programs.ofRules(
                FilterProjectTransposeRule.INSTANCE,
                ProjectMergeRule.INSTANCE,
                FilterMergeRule.INSTANCE,
                LoptOptimizeJoinRule.INSTANCE
        );

        // Create the desired output traits
        // No idea what I'm doing
        RelTraitSet relTraits = volcanoPlanner.emptyTraitSet()
                .replace(Convention.NONE)
                .replace(InterpretableConvention.INSTANCE);

        return program.run(volcanoPlanner,relRoot.rel,relTraits, null,null);

    }
}
