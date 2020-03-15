package com.gerardnico.calcite;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;

public class CalciteSqlParser {

    /**
     *
     * @return a parser with a MySql lexicon
     */
    public static SqlParser.Config createMySqlConfig() {
        return SqlParser.configBuilder()
                .setCaseSensitive(false)
                .setLex(Lex.MYSQL)
                .setConformance(SqlConformanceEnum.DEFAULT)
                .build();
    }

    /**
     * @return a default parser config
     */
    public static SqlParser.Config getDefault() {
        return SqlParser.Config.DEFAULT;
    }

    /**
     * Create a default parser with a {@link #createMySqlConfig()}
     * @param sql
     * @return
     */
    public static SqlParser create(String sql) {

        return SqlParser.create(sql, createMySqlConfig());
    }

    /**
     *
     * @param sql
     * @param config
     * @return
     */
    public static SqlParser create(String sql, SqlParser.Config config) {

        return SqlParser.create(sql, config);
    }
}
