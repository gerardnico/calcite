package com.gerardnico.calcite;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.RelBuilder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalciteJdbc {


    /**
     *
     * @param dataSource
     * @return the calcite schema representation of the Jdbc schema
     */
    public static SchemaPlus getSchema(DataSource dataSource) {
        try {
            SchemaPlus rootSchema = Frameworks.createRootSchema(true);
            String schemaName = dataSource.getConnection().getSchema();
            return rootSchema.add(
                    schemaName,
                    JdbcSchema.create(rootSchema, null, dataSource, null, null)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a RelBuilder based on the schema of a data store
     * @param dataSource
     * @return
     */
    public static RelBuilder getBuilder(DataSource dataSource){
        return CalciteRel.createDataStoreBasedRelBuilder(dataSource);
    }


    /**
     * An utility function to print a resultSet
     *
     * @param resultSet
     */
    public static void printResultSet(ResultSet resultSet) {
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
}
