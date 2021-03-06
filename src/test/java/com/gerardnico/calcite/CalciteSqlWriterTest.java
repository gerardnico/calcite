package com.gerardnico.calcite;

import org.apache.calcite.runtime.CalciteContextException;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.junit.Test;

/**
 * Parser is used to:
 * * pretty print
 * * validate
 */
public class CalciteSqlWriterTest {

    @Test
    public void prettyPrintDemoTest() {
        final String sql = "select x as a, b as b, c as c, d,"
                + " 'mixed-Case string',"
                + " unquotedCamelCaseId,"
                + " \"quoted id\" "
                + "from"
                + " (select *"
                + " from t"
                + " where x = y and a > 5"
                + " group by z, zz"
                + " window w as (partition by c),"
                + "  w1 as (partition by c,d order by a, b"
                + "   range between interval '2:2' hour to minute preceding"
                + "    and interval '1' day following)) "
                + "order by gg desc nulls last, hh asc";
        SqlNode sqlNode = CalciteSql.fromSqlToSqlNode(sql);
        SqlWriterConfig config = SqlPrettyWriter.config()
                .withLineFolding(SqlWriterConfig.LineFolding.STEP)
                .withSelectFolding(SqlWriterConfig.LineFolding.TALL)
                .withFromFolding(SqlWriterConfig.LineFolding.TALL)
                .withWhereFolding(SqlWriterConfig.LineFolding.TALL)
                .withHavingFolding(SqlWriterConfig.LineFolding.TALL)
                .withIndentation(4)
                .withClauseEndsLine(true);
        CalciteSql.print(sqlNode,config);
    }


}
