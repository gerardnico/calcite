package com.gerardnico.calcite;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.Frameworks;

import javax.sql.DataSource;
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
}
