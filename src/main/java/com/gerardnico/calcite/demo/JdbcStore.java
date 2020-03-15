package com.gerardnico.calcite.demo;

import com.gerardnico.calcite.CalciteSql;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlDialectFactoryImpl;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Programs;
import org.apache.calcite.tools.RelBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Main entry for every Jdbc related operation inside a Calcite environment
 */
public class JdbcStore {


    private final String driverClassName;
    private final String userName;
    private final String url;
    private final String password;
    private DataSource dataSource;
    private static String SCHEMA_NAME = "PUBLIC";
    private SchemaPlus defaultSchema;

    /**
     * @param url
     * @param driverClassName
     * @param username
     * @param password
     */
    public JdbcStore(String url, String driverClassName,
                     String username, String password) {
        this.url = (url != null ? url : "jdbc:h2:mem:test");
        this.driverClassName = (driverClassName != null ? driverClassName : "org.h2.Driver");
        this.userName = username;
        this.password = password;
    }

    public static JdbcStore createDefault() {
        return new JdbcStore(null, null, null, null);
    }

    DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = JdbcSchema.dataSource(url, driverClassName, userName, password);
        }
        return dataSource;
    }


    public RelBuilder getRelBuilder() {
        return RelBuilder.create(
                Frameworks.newConfigBuilder()
                        .parserConfig(SqlParser.Config.DEFAULT)
                        .defaultSchema(getOrCreateDefaultSchema())
                        .traitDefs((List<RelTraitDef>) null)
                        .programs(Programs.heuristicJoinOrder(Programs.RULE_SET, true, 2))
                        .build()
        );
    }

    /**
     * Create the calcite schema from the data source
     *
     * @return
     */
    private SchemaPlus getOrCreateDefaultSchema() {
        if (defaultSchema == null) {
            SchemaPlus rootSchema = Frameworks.createRootSchema(true);
            defaultSchema = rootSchema.add(
                    SCHEMA_NAME,
                    JdbcSchema.create(rootSchema, null, dataSource, null, null)
            );
        }
        return defaultSchema;
    }

    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * After having create a {@link RelNode regular expression} with the  {@link #getRelBuilder() builder},
     * you can transform it into sql
     *
     * @param relNode
     * @return the sql representation of the relNode
     */
    public String translateRelNodeToSql(RelNode relNode) {

        try {
            SqlDialect dialect = SqlDialectFactoryImpl.INSTANCE.create(getConnection().getMetaData());
            return CalciteSql.fromRelNodeToSql(relNode, dialect);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Execute a print a sql query
     *
     * @param sqlQuery
     */
    public void executeAndPrintQuery(String sqlQuery) {
        try (ResultSet resultSet = getConnection().createStatement().executeQuery(sqlQuery)) {
            print(resultSet);
        } catch (SQLException e) {
            System.out.println("FAILED! - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * An utility function to print a resultSet
     *
     * @param resultSet
     */
    public static void print(ResultSet resultSet) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    stringBuilder
                            .append(resultSet.getObject(i))
                            .append(",");
                }
                stringBuilder.append("\n");
            }
            System.out.println();
            System.out.println("Result:");
            System.out.println(stringBuilder.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * An utility Function that:
     * * Translates a relnode into a query via {@link #translateRelNodeToSql(RelNode)}
     * * Execute it and print it via {@link #executeAndPrintQuery(String)}
     *
     * @param relNode
     */
    public void translateRelNodeToSqlExecuteAndPrint(RelNode relNode) {
        // Translate it to SQL
        String sql = translateRelNodeToSql(relNode);
        System.out.println("The SQL generated was:" + sql);
        System.out.println();
        // Execute
        executeAndPrintQuery(sql);
    }
}
