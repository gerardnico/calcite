package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.foodmart.FoodmartSchema;
import com.gerardnico.calcite.schema.hr.HrSchema;
import com.gerardnico.calcite.schema.queries.HrFoodmartQuery;
import org.apache.calcite.adapter.java.ReflectiveSchema;
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

    /**
     * Add the HR and Foodmart Schema
     * @param calciteConnection - a calcite connection (Example {@link CalciteConnectionStatic#getConnectionWithoutModel()}
     * @return - the same connection
     */
    public static void addReflectiveSchemaToConnection(CalciteConnection calciteConnection) {

        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        rootSchema.add("hr", new ReflectiveSchema(new HrSchema()));
        rootSchema.add("foodmart", new ReflectiveSchema(new FoodmartSchema()));

    }


    public static CalciteConnection addReflectiveSchemaToConnectionViaModel() {
        return CalciteConnectionStatic.getConnectionWithModel("src/main/resources/hrAndFoodmart/hrAndFoodmart.json");
    }
}
