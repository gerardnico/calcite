package com.gerardnico.calcite;

import org.junit.Test;

public class SqlParserTest {

    @Test
    public void sqlBase() {
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
        SqlParseTree
                .createTreeFromSql(sql)
                .print();
    }
}
