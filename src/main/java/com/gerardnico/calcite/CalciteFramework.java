package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.SchemaBuilder;
import com.gerardnico.calcite.schema.hr.HrSchema;
import com.gerardnico.calcite.schema.orderEntry.OrderEntrySchema;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.Contexts;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Programs;
import org.apache.calcite.tools.RuleSets;

import javax.sql.DataSource;
import java.util.List;

/**
 * {@link Frameworks Frameworks} is the second entry point of calcite after JDBC
 */
public class CalciteFramework {


    private static FrameworkConfig createFrameworkConfig(SchemaPlus schema) {

        return Frameworks.newConfigBuilder()
                .parserConfig(CalciteSqlParser.createMySqlConfig())
                .defaultSchema(schema)
                .typeSystem(CalciteRelDataType.getDefaultSystem())
                .costFactory(null)
                .ruleSets(RuleSets.ofList())
                .traitDefs(CalciteRelTrait.getRelTraits())
                .context(Contexts.EMPTY_CONTEXT) // Context provides a way to store data within the planner session that can be accessed in planner rules.
                .build();

    }

    public static FrameworkConfig getDefaultConfig(){
        return Frameworks.newConfigBuilder().build();
    }

    /**
     * Get a default config from a data store
     * @param dataStore
     * @return
     */
    public static FrameworkConfig getNewConfig(DataSource dataStore){
        SchemaPlus schemaPlus = CalciteSchema.getCurrentSchema(dataStore);
        return Frameworks.newConfigBuilder()
                .defaultSchema(schemaPlus)
                .parserConfig(CalciteSqlParser.getDefault())
                .build();
    }


    /**
     * Creates a config based on the SCOTT schema, with tables EMP and DEPT.
     * @return
     */
    public static FrameworkConfig configScottSchemaBased() {
        return Frameworks.newConfigBuilder()
                .defaultSchema(SchemaBuilder.getSchema(SchemaBuilder.SchemaSpec.SCOTT_WITH_TEMPORAL))
                .build();
    }

    public static FrameworkConfig configOrderEntrySchemaBased() {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        ReflectiveSchema schema = new ReflectiveSchema(new OrderEntrySchema());
        SchemaPlus orderEntry = rootSchema.add("OE", schema);
        return Frameworks.newConfigBuilder()
                .parserConfig(
                        SqlParser.configBuilder()
                        .setCaseSensitive(false)
                        .build())
                .defaultSchema(orderEntry)
                .traitDefs((List<RelTraitDef>) null)
                .programs(Programs.heuristicJoinOrder(Programs.RULE_SET, true, 2))
                .build();
    }

    /**
     * A config based on the HR schema
     * @return
     */
    public static FrameworkConfig configHrSchemaBased() {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        ReflectiveSchema schema = new ReflectiveSchema(new HrSchema());
        SchemaPlus hr = rootSchema.add("HR", schema);

        return Frameworks.newConfigBuilder()
                .parserConfig(CalciteSqlParser.getInsensitiveConfig())
                .defaultSchema(hr)
                .build();
    }

}
