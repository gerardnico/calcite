package com.gerardnico.calcite;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlDialectFactoryImpl;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Programs;
import org.apache.calcite.tools.RelBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Jdbc connection and method inside a Calcite environment
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

    static JdbcStore createDefault() {
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
     * Transform a regular expression into sql
     *
     * @param relNode
     * @return
     */
    public String relNodeToSql(RelNode relNode) {
        try {
            SqlDialect dialect = SqlDialectFactoryImpl.INSTANCE.create(getConnection().getMetaData());
            SqlPrettyWriter sqlWriter = new SqlPrettyWriter();
            RelToSqlConverter relToSqlConverter = new RelToSqlConverter(dialect);
            SqlSelect sqlSelect = relToSqlConverter.visitChild(0, relNode).asSelect();
            return sqlWriter.format(sqlSelect);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeAndPrintQuery(String sql) {
        try (ResultSet resultSet = getConnection().createStatement().executeQuery(sql)) {
            print(resultSet);
        } catch (SQLException e) {
            System.out.println("FAILED! - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void print(ResultSet resultSet) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    stringBuilder
                            .append(resultSet.getObject(i))
                            .append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *   * Translate a relnode into a query
     *   * Execute it
     *   * And print it
     * @param relNode
     */
    public void executeRelNodeAndPrint(RelNode relNode) {
        // Translate it to SQL
        String sql = relNodeToSql(relNode);
        System.out.println("The SQL generated was:"+sql);
        System.out.println();
        // Execute
        executeAndPrintQuery(sql);
    }
}
