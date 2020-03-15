package com.gerardnico.calcite;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Write a sql from a sqlNode
 */
public class CalciteSqlWriter {

    /**
     * The config
     */
    private SqlWriterConfig config = SqlPrettyWriter.config();

    /**
     * The tree representation of the sql
     */
    private SqlNode tree;

    public CalciteSqlWriter(SqlNode sqlNode) {
        this.tree = sqlNode;
    }

    public static CalciteSqlWriter fromSqlNode(SqlNode sqlNode) {
        return new CalciteSqlWriter(sqlNode);
    }

    /**
     * Print the tree
     */
    void print() {
        System.out.println(new SqlPrettyWriter(config).format(tree));
    }

    /**
     * @param transform - a lambda expression to change the writer config
     * @return Example: c->c.withLineFolding(SqlWriterConfig.LineFolding.STEP)
     */
    public CalciteSqlWriter withWriterConfig(UnaryOperator<SqlWriterConfig> transform) {
        Objects.requireNonNull(transform);
        transform.apply(config);
        return this;
    }
}
