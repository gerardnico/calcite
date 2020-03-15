package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.JdbcStore;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.dialect.*;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;

import java.util.ArrayList;
import java.util.List;

public class CalciteSql {

    /**
     * Transform a SQL Node to SQL back
     *
     * @param sqlNode
     * @return
     */
    public static String fromSqlNodeToSql(SqlNode sqlNode) {
        SqlPrettyWriter sqlWriter = new SqlPrettyWriter();
        sqlNode.unparse(sqlWriter, 0, 0);
        return sqlWriter.toString();
    }


    /**
     * After having create a {@link RelNode regular expression} with for instance the  {@link JdbcStore#getRelBuilder() builder},
     * you can transform it into sql
     *
     * @param relNode
     * @param sqlDialect - A sql dialect (one of {@link #getDialects()})
     * @return the sql representation of the relNode
     */
    static public String fromRelNodeToSql(RelNode relNode, SqlDialect sqlDialect) {
        SqlPrettyWriter sqlWriter = new SqlPrettyWriter();
        RelToSqlConverter relToSqlConverter = new RelToSqlConverter(sqlDialect);
        SqlSelect sqlSelect = relToSqlConverter.visitChild(0, relNode).asSelect();
        return sqlWriter.format(sqlSelect);
    }

    /**
     *
     * @param relNode
     * @return a sql in the ANSI dialect
     */
    static public String fromRelNodeToSql(RelNode relNode) {
        SqlDialect dialect = AnsiSqlDialect.DEFAULT;
        return fromRelNodeToSql(relNode,dialect);
    }

    /**
     * Get dialects as demo
     * All dialects are at {@link org.apache.calcite.sql.dialect}
     */
    static public List<SqlDialect> getDialects(){
        List<SqlDialect> sqlDialects = new ArrayList<>();
        sqlDialects.add(AnsiSqlDialect.DEFAULT);
        sqlDialects.add(SnowflakeSqlDialect.DEFAULT);
        sqlDialects.add(AccessSqlDialect.DEFAULT);
        sqlDialects.add(BigQuerySqlDialect.DEFAULT);
        sqlDialects.add(CalciteSqlDialect.DEFAULT);
        sqlDialects.add(DerbySqlDialect.DEFAULT);
        return sqlDialects;
    }
}

