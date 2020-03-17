package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import com.gerardnico.calcite.mock.MockJdbcCatalogReader;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.runtime.CalciteContextException;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.ValidationException;
import org.junit.Test;

public class CalciteSqlValidationTest {

    @Test
    public void parseValidationGoodTest() {
        final String sql = "select * from SALES.EMP";
        SqlNode sqlNode = CalciteSql.fromSqlToSqlNode(sql);
        CalciteSqlValidation.createCustomSqlValidator().validate(sqlNode);
    }

    @Test(expected = CalciteContextException.class)
    public void parseValidationBadTest() {
        final String sql = "select * from YOLO"; // Yolo is not a table
        SqlNode sqlNode = CalciteSql.fromSqlToSqlNode(sql);
        CalciteSqlValidation.createCustomSqlValidator().validate(sqlNode);
    }

    @Test
    public void validateFromPlanner() throws ValidationException {
        FrameworkConfig config = CalciteFramework.configScottSchemaBased();
        Planner planner = CalcitePlanner.getPlannerFromFrameworkConfig(config);
        final String sql = "select * from EMP";
        SqlNode sqlNode = null;
        try {
            sqlNode = planner.parse(sql);
        } catch (SqlParseException e) {
            throw new RuntimeException(e);
        }
        planner.validate(sqlNode);

    }

    @Test
    public void validateFromSqlValidatorUtil() throws SqlParseException {

        // A query parsed
        SqlParser sqlParser = CalciteSqlParser.create("select * from EMP");
        SqlNode sqlNode = sqlParser.parseStmt();

        // A catalog with the EMP table
        Prepare.CatalogReader catalog = CalciteCatalogs.createMock();

        // Validate with a default SQL conformance
        SqlNode sqlNodeValidated = CalciteSqlValidation.validateFromUtilValidatorPlanner(catalog,sqlNode);

        // Print
        CalciteSql.print(sqlNodeValidated);


    }
}
