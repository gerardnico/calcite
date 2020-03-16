package com.gerardnico.calcite;

import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.RelCollationTraitDef;

import java.util.ArrayList;
import java.util.List;

public class CalciteRelTrait {


    public static List<RelTraitDef> getRelTraits(){
        final List<RelTraitDef> traitDefs = new ArrayList<RelTraitDef>();
        traitDefs.add(ConventionTraitDef.INSTANCE);
        traitDefs.add(RelCollationTraitDef.INSTANCE);
        return traitDefs;
    }

}
