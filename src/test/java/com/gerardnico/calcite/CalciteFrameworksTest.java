package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.hr.HrSchema;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.RelWriter;
import org.apache.calcite.rel.externalize.RelWriterImpl;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlExplainLevel;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.AnsiSqlDialect;
import org.apache.calcite.sql.dialect.OracleSqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.*;
import org.junit.Test;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalciteFrameworksTest {


    @Test
    public void parseValidateAndLogicalPlanTest() throws SqlParseException, RelConversionException, ValidationException, SQLException {

        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        ReflectiveSchema schema = new ReflectiveSchema(new HrSchema());
        SchemaPlus hr = rootSchema.add("HR", schema);
        SqlParser.Config insensitiveParser = SqlParser.configBuilder()
                .setCaseSensitive(false)
                .build();
        FrameworkConfig config = Frameworks.newConfigBuilder()
                .parserConfig(insensitiveParser)
                .defaultSchema(hr)
                .build();
        Planner planner = Frameworks.getPlanner(config);
        SqlNode sqlNode = planner.parse("select depts.name, count(emps.empid) from emps inner join depts on emps.deptno = depts.deptno group by depts.deptno, depts.name");
        System.out.println(sqlNode.toSqlString(OracleSqlDialect.DEFAULT));

        SqlNode sqlNodeValidated = planner.validate(sqlNode);
        RelRoot relRoot = planner.rel(sqlNodeValidated);

        // Logical
        RelNode relNode = relRoot.project();
        final RelWriter relWriter = new RelWriterImpl(new PrintWriter(System.out), SqlExplainLevel.ALL_ATTRIBUTES, false);
        relNode.explain(relWriter);

        PreparedStatement run = RelRunners.run(relNode);
        ResultSet resultSet = run.executeQuery();

        System.out.println("Result:");
        while (resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getObject(i)+",");
            }
            System.out.println();
        }

    }
}
