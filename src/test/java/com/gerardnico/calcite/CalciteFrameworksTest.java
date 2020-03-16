package com.gerardnico.calcite;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;
import org.junit.Test;

public class CalciteFrameworksTest {


    @Test
    public void parseValidateAndLogicalPlanTest() throws SqlParseException, RelConversionException, ValidationException {
        FrameworkConfig config = CalciteFramework.configScottSchemaBased();
        Planner planner = CalcitePlanner.getPlannerFromFrameworkConfig(config);
        SqlNode sqlNode = planner.parse("select count(1) from emp");
        SqlNode sqlNodeValidated = planner.validate(sqlNode);
        RelRoot relNode = planner.rel(sqlNodeValidated);
        RelNode logicalPlanRelNode = relNode.project();
        CalciteRel.print(logicalPlanRelNode);
        CalciteRel.executeAndPrint(logicalPlanRelNode);
    }
}
