package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockJdbcCatalogReader;
import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import com.gerardnico.calcite.schema.SchemaBuilder;
import com.gerardnico.calcite.schema.hr.HrSchema;
import com.gerardnico.calcite.schema.orderEntry.OrderEntrySchema;
import org.apache.calcite.adapter.clone.CloneSchema;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Programs;

import java.util.List;

public class CalciteFramework {


    private static FrameworkConfig createFrameworkConfig(SchemaPlus schema) {

        return Frameworks.newConfigBuilder()
                .parserConfig(CalciteSqlParser.createMySqlConfig())
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

    public static FrameworkConfig configOrderEntrySchemaBased() {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        ReflectiveSchema schema = new ReflectiveSchema(new OrderEntrySchema());
        rootSchema.add("OE", schema);
        SchemaPlus orderEntry = rootSchema.getSubSchema("OE");
        return Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.Config.DEFAULT)
                .defaultSchema(orderEntry)
                .traitDefs((List<RelTraitDef>) null)
                .programs(Programs.heuristicJoinOrder(Programs.RULE_SET, true, 2))
                .build();
    }
}
