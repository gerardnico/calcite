package com.gerardnico.calcite.demo;

import com.gerardnico.calcite.CalciteJdbc;
import com.gerardnico.calcite.CalciteRel;
import com.gerardnico.calcite.CalciteSql;
import com.gerardnico.calcite.CalciteSqlDialect;
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
        return CalciteRel.createDataStoreBasedRelBuilder(dataSource);
    }

    /**
     * Create the calcite schema from the data source
     *
     * @return
     */
    private SchemaPlus getOrCreateDefaultSchema() {
        if (defaultSchema == null) {
            defaultSchema = CalciteJdbc.getSchema(dataSource);
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
     * Execute a print a sql query
     *
     * @param sqlQuery
     */
    public void executeAndPrintQuery(String sqlQuery) {
        try (ResultSet resultSet = getConnection().createStatement().executeQuery(sqlQuery)) {
            CalciteJdbc.printResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("FAILED! - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return the dialect for this connection
     */
    public SqlDialect getDialect() {
        return CalciteSqlDialect.getDialectFromSqlConnection(getConnection());
    }




}
