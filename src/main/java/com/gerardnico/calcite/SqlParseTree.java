package com.gerardnico.calcite;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;

public class SqlParseTree {

    /**
     * The tree
     */
    SqlNode tree;

    public SqlParseTree(String sql) {

        try {
            tree = org.apache.calcite.sql.parser.SqlParser.create(sql).parseQuery();
        } catch (SqlParseException e) {
            String message = "Received error while parsing SQL '" + sql + "'"
                    + "; error is:\n"
                    + e.toString();
            throw new AssertionError(message);
        }

    }

    /**
     * Parses a SQL query.
     */
    static public SqlParseTree createTreeFromSql(String sql) {
        return new SqlParseTree(sql);
    }

    /**
     * Print the tree
     */
    void print() {
        final SqlWriterConfig config = SqlPrettyWriter.config()
                .withLineFolding(SqlWriterConfig.LineFolding.STEP)
                .withSelectFolding(SqlWriterConfig.LineFolding.TALL)
                .withFromFolding(SqlWriterConfig.LineFolding.TALL)
                .withWhereFolding(SqlWriterConfig.LineFolding.TALL)
                .withHavingFolding(SqlWriterConfig.LineFolding.TALL)
                .withIndentation(4)
                .withClauseEndsLine(true);
        System.out.println(new SqlPrettyWriter(config).format(tree));
    }


}
