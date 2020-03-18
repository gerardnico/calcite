package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.foodmart.FoodmartSchema;
import com.gerardnico.calcite.schema.hr.HrSchema;
import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Static schema function
 */
public class CalciteSchemaStatic {

    /**
     *
     * @param calciteConnection - A {@link CalciteConnection} that you can get in {@link CalciteConnections}
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

    /**
     *
     * @param dataSource
     * @return the current schema of a data source
     */
    static SchemaPlus getCurrentSchema(DataSource dataSource){

        return CalciteJdbc.getSchema(dataSource);
    }

    public static SchemaPlus getRootSchemaFromCalciteConnection(CalciteConnection calciteConnection) {
        return calciteConnection.getRootSchema();
    }

    public static SchemaPlus getRootSchemaFromSqlConnection(Connection connection) {
        try {
            CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
            return calciteConnection.getRootSchema();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add the HR and Foodmart Schema
     * @param calciteConnection - a calcite connection (Example {@link CalciteConnections#getConnectionWithoutModel()}
     * @return - the same connection
     */
    public static void addReflectiveSchemaToConnection(CalciteConnection calciteConnection) {

        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        rootSchema.add("hr", new ReflectiveSchema(new HrSchema()));
        rootSchema.add("foodmart", new ReflectiveSchema(new FoodmartSchema()));

    }


    public static CalciteConnection addReflectiveSchemaToConnectionViaModel() {
        return CalciteConnections.getConnectionWithModel("src/main/resources/hrAndFoodmart/hrAndFoodmart.json");
    }

    /**
     * Get a root schema from a data context
     * @return
     */
    public static SchemaPlus getRootSchemaFromDataContext(DataContext dataContext){
        return dataContext.getRootSchema();
    }
}
