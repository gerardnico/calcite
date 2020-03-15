package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.SchemaBuilder;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Programs;

import java.util.List;

public class CalciteFramework {


    private static FrameworkConfig createFrameworkConfig(SchemaPlus schema) {

        return Frameworks.newConfigBuilder()
                .parserConfig(CalciteSqlParser.createConfig())
                //.ruleSets(CalciteRulesSets.getDefault())
                .defaultSchema(schema)
                .build();

    }

    /**
     * Creates a config based on the SCOTT schema, with tables EMP and DEPT.
     * @return
     */
    public static FrameworkConfig configScottBased() {
        return Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.Config.DEFAULT)
                .defaultSchema(SchemaBuilder.getSchema(SchemaBuilder.SchemaSpec.SCOTT_WITH_TEMPORAL))
                .traitDefs((List<RelTraitDef>) null)
                .programs(Programs.heuristicJoinOrder(Programs.RULE_SET, true, 2))
                .build();
    }

}
