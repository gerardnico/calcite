package com.gerardnico.calcite;

import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlDialectFactoryImpl;
import org.apache.calcite.sql.dialect.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An SQL dialect
 *
 */

public class CalciteSqlDialect {

    public enum DIALECT {
        ANSI,
        SNOWFLAKE,
        ACCESS,
        BIGQUERY,
        CALCITE,
        DERBY
    }

    static Map<DIALECT,SqlDialect> dialects = new HashMap<>();
    static {
        dialects.put(DIALECT.ANSI,AnsiSqlDialect.DEFAULT);
        dialects.put(DIALECT.SNOWFLAKE,SnowflakeSqlDialect.DEFAULT);
        dialects.put(DIALECT.ACCESS,AccessSqlDialect.DEFAULT);
        dialects.put(DIALECT.BIGQUERY,BigQuerySqlDialect.DEFAULT);
        dialects.put(DIALECT.CALCITE,org.apache.calcite.sql.dialect.CalciteSqlDialect.DEFAULT);
        dialects.put(DIALECT.DERBY,DerbySqlDialect.DEFAULT);
    }

    /**
     * Get dialects as demo
     * All dialects are at {@link org.apache.calcite.sql.dialect}
     */
    static public List<SqlDialect> getDialects() {

        return new ArrayList<>(dialects.values());
    }

    /**
     * Get dialects as demo
     * All dialects are at {@link org.apache.calcite.sql.dialect}
     */
    static public SqlDialect getDialect(DIALECT dialect) {
        return dialects.get(dialect);
    }

    /**
     * Get a dialect from a JDBC connection
     */
    static public SqlDialect getDialectFromSqlConnection(Connection connection) {
        try {
            return SqlDialectFactoryImpl.INSTANCE.create(connection.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


