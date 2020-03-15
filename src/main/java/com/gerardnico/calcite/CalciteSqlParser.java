package com.gerardnico.calcite;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.parser.SqlParser;

public class CalciteSqlParser {

    public static SqlParser.Config createConfig() {
        return SqlParser.configBuilder()
                .setCaseSensitive(false)
                .setLex(Lex.MYSQL)
                .build();
    }

}
