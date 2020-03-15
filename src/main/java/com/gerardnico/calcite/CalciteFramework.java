package com.gerardnico.calcite;

import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

public class CalciteFramework {


    private static FrameworkConfig createFrameworkConfig(SchemaPlus schema) {

        return Frameworks.newConfigBuilder()
                .parserConfig(CalciteSqlParser.createConfig())
                //.ruleSets(CalciteRulesSets.getDefault())
                .defaultSchema(schema)
                .build();

    }

}
