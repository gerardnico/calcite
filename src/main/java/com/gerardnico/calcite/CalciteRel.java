package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.JdbcStore;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.*;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CalciteRel {

    /**
     * @return a {@link RelBuilder} with a schema mapped to the SCOTT database, with tables EMP and DEPT.
     */
    public static RelBuilder createHrScottBasedRelBuilder() {
        final FrameworkConfig config = CalciteFramework.configScottBased();
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
     * Print a relational expression
     *
     * @param relNode
     */
    public static void print(RelNode relNode) {
        System.out.println(toString(relNode));
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
        PreparedStatement run = RelRunners.run(relNode);
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
        try (ResultSet resultSet = executeQuery(relNode)) {
            CalciteJdbc.printResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
