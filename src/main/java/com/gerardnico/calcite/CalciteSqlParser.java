package com.gerardnico.calcite;

import org.apache.calcite.config.Lex;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;

public class CalciteSqlParser {

    /**
     * The parser config set the Lexical configuration
     * Ie
     *   * how identifiers are quoted,
     *   * whether they are converted to upper or lower
     *
     * This is the default and it can also be set on a {@link CalciteConnectionStatic#getConnectionWithoutModel connection level}
     *
     * @return a parser config with a MySql lexicon
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
     * Create a default parser
     * @param sql
     * @return
     */
    public static SqlParser create(String sql) {

        return SqlParser.create(sql);
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
