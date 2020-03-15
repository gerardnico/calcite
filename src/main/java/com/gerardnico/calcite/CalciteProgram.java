package com.gerardnico.calcite;

import org.apache.calcite.tools.Program;
import org.apache.calcite.tools.Programs;

public class CalciteProgram {


    public static Program createHeuristicJoinOrderProgram() {
        return Programs.heuristicJoinOrder(
                Programs.RULE_SET,
                true,
                2);
    }
}
