package com.gerardnico.calcite;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Static schema function
 */
public class CalciteSchemaStatic {

    /**
     *
     * @param calciteConnection - A {@link CalciteConnection} that you can get in {@link CalciteConnectionStatic}
     * @return
     */
    static SchemaPlus getCurrentSchema(CalciteConnection calciteConnection){
        final String schemaName;
        try {
            schemaName = calciteConnection.getSchema();
            return calciteConnection.getRootSchema().getSubSchema(schemaName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SchemaPlus getRootSchemaFromCalciteConnection(CalciteConnection calciteConnection) {
        return calciteConnection.getRootSchema();
    }

    public static SchemaPlus getRooSchemaFromSqlConnection(Connection connection) {
        try {
            CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
            return calciteConnection.getRootSchema();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
