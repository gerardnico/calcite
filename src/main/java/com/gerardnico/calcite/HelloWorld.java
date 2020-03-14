package com.gerardnico.calcite;

import com.gerardnico.calcite.hr.HrSchema;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;

import java.sql.*;

/**
 * Code was adapted from <a href=https://calcite.apache.org/docs/index.html>Background</a>
 */
public class HelloWorld {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // Create the connection
        try (
                CalciteConnection calciteConnection = CalciteConnections.getConnectionWithoutModel();
        ) {

            // Calcite - Add the Hr schema to the connection
            addHrSchema(calciteConnection);

            // Jdbc basic query
            try (
                    Statement statement = calciteConnection.createStatement();
                    ResultSet resultSet = statement.executeQuery(
                            "select d.deptno, min(e.empid)\n"
                                    + "from hr.emps as e\n"
                                    + "join hr.depts as d\n"
                                    + "  on e.deptno = d.deptno\n"
                                    + "group by d.deptno\n"
                                    + "having count(*) > 1");
            ) {
                JdbcStore.print(resultSet);
            }
        }
    }

    /**
     * Add the {@link HrSchema}
     *
     * @param connection
     */
    private static void addHrSchema(CalciteConnection connection) {

        SchemaPlus rootSchema = RootSchema.getFromCalciteConnection(connection);
        Schema schema = new ReflectiveSchema(new HrSchema());
        rootSchema.add("hr", schema);

    }
}
