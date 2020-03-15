package com.gerardnico.calcite;

import org.apache.calcite.runtime.CalciteContextException;
import org.apache.calcite.sql.SqlNode;
import org.junit.Test;

public class CalciteSqlValidationTest {

    @Test
    public void parseValidationGoodTest() {
        final String sql = "select * from SALES.EMP";
        SqlNode sqlNode = CalciteSql.fromSqlToSqlNode(sql);
        CalciteSqlValidator.createSqlValidator().validate(sqlNode);
    }

    @Test(expected = CalciteContextException.class)
    public void parseValidationBadTest() {
        final String sql = "select * from YOLO"; // Yolo is not a table
        SqlNode sqlNode = CalciteSql.fromSqlToSqlNode(sql);
        CalciteSqlValidator.createSqlValidator().validate(sqlNode);
    }
}
