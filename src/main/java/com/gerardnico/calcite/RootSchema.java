package com.gerardnico.calcite;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.Frameworks;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * RootSchema statics method
 */
public class RootSchema {

    public static SchemaPlus getFromCalciteConnection(CalciteConnection calciteConnection) {
        return calciteConnection.getRootSchema();
    }

    public static SchemaPlus getFromSqlConnection(Connection connection) {
        try {
            CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
            return calciteConnection.getRootSchema();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return a root schema from {@link Frameworks#createRootSchema(boolean)}
     */
    public static SchemaPlus createFromFrameworks(){
        return Frameworks.createRootSchema(true);
    }

}
