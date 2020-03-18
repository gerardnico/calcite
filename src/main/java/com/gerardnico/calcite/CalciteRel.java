package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.JdbcStore;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.RelWriter;
import org.apache.calcite.rel.externalize.RelWriterImpl;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlExplainLevel;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.*;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalciteRel {

    /**
     * @return a {@link RelBuilder} with a schema mapped to the SCOTT database, with tables EMP and DEPT.
     */
    public static RelBuilder createHrScottBasedRelBuilder() {
        final FrameworkConfig config = CalciteFramework.configScottSchemaBased();
        return RelBuilder.create(config);
    }


    /**
     * Create a RelBuilder based on the JDBC data source
     *
     * @param dataSource
     * @return
     */
    public static RelBuilder createDataStoreBasedRelBuilder(DataSource dataSource) {
        SchemaPlus schemaPlus = CalciteJdbc.getSchema(dataSource);
        FrameworkConfig config = Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.Config.DEFAULT)
                .defaultSchema(schemaPlus)
                .programs(CalciteProgram.createHeuristicJoinOrderProgram())
                .build();
        return RelBuilder.create(config);
    }

    /**
     * Print a relational expression (ie sane as {@link #explain(RelNode)}
     *
     * @param relNode
     */
    public static void print(RelNode relNode) {
        System.out.println(toString(relNode));
    }

    /**
     *
     * Explain is just a {@link #print(RelNode)}
     * @param relNode
     */
    public static void explain(RelNode relNode) {
        RelWriter rw = new RelWriterImpl(new PrintWriter(System.out, true));
        relNode.explain(rw);
    }

    /**
     *
     * Explain is just a {@link #print(RelNode)}
     * @param relNode
     */
    public static void explainAll(RelNode relNode) {
        System.out.println(RelOptUtil.toString(relNode, SqlExplainLevel.ALL_ATTRIBUTES));
    }


    /**
     * @param relNode
     * @return the string representation of a relNode
     */
    public static String toString(RelNode relNode) {
        return RelOptUtil.toString(relNode);
    }



    /**
     * Execute a relNode and return the result set
     *
     * @param relNode
     * @return
     */
    public static ResultSet executeQuery(RelNode relNode) {
        return executeQuery(relNode,null);
    }

    public static ResultSet executeQuery(RelNode relNode, SchemaPlus defaultSchema) {
        PreparedStatement run = CalciteRelRunners.run(relNode, defaultSchema);
        try {
            return run.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@link #executeQuery(RelNode) Execute} and print the result set in one pass
     *
     * @param relNode
     */
    public static void executeAndPrint(RelNode relNode) {
        executeAndPrint(relNode,null);
    }

    /**
     * After having create a {@link RelNode regular expression} with a builder.
     * Example: {@link JdbcStore#getRelBuilder() builder},
     * <p>
     * You can transform it into sql
     *
     * @param relNode
     * @param sqlDialect - the dialect
     * @return the sql representation of the relNode
     */
    public static String fromRelNodeToSql(RelNode relNode, SqlDialect sqlDialect) {
        return CalciteSql.fromRelNodeToSql(relNode, sqlDialect);
    }


    public static RelBuilder createOrderEntryBasedRelBuilder() {
        final FrameworkConfig config = CalciteFramework.configOrderEntrySchemaBased();
        return RelBuilder.create(config);
    }

    /**
     *
     * @param relRoot
     * @return a logical plan
     */
    public static RelNode getLogicalPlan(RelRoot relRoot){
        return relRoot.project();
    }

    public static void executeAndPrint(RelNode relNode, SchemaPlus defaultSchema) {

            try (ResultSet resultSet = executeQuery(relNode, defaultSchema)) {
                CalciteJdbc.printResultSet(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


    }
}
