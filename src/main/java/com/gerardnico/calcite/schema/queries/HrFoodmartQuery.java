package com.gerardnico.calcite.schema.queries;

import com.gerardnico.calcite.CalciteJdbc;
import com.gerardnico.calcite.schema.hr.HrSchema;
import org.apache.calcite.jdbc.CalciteConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HrFoodmartQuery {

    /**
     * Execute a sample query agains the {@link HrSchema#createHrSchema()}
     *
     * @param connection
     */
    public static void executeSampleQuery(CalciteConnection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select *\n"
                    + "from foodmart.sales_fact_1997 as s\n"
                    + "join hr.emps as e\n"
                    + "on e.empid = s.cust_id");
            CalciteJdbc.printResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
