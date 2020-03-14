package com.gerardnico.calcite;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class SqlParseTree {

    /**
     * The tree
     */
    private SqlNode tree;

    /**
     * The config
     */
    private SqlWriterConfig config;

    public SqlParseTree(String sql) {

        try {
            tree = org.apache.calcite.sql.parser.SqlParser.create(sql).parseQuery();
        } catch (SqlParseException e) {
            String message = "Received error while parsing SQL '" + sql + "'"
                    + "; error is:\n"
                    + e.toString();
            throw new AssertionError(message);
        }
        config = SqlPrettyWriter.config();

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
        System.out.println(new SqlPrettyWriter(config).format(tree));
    }

    /**
     *
     * @param transform - a lambda expression to change the writer config
     * @return
     *
     * Example: c->c.withLineFolding(SqlWriterConfig.LineFolding.STEP)
     */
    public SqlParseTree withWriterConfig(UnaryOperator<SqlWriterConfig> transform) {
        Objects.requireNonNull(transform);
        transform.apply(config);
        return this;
    }

}
