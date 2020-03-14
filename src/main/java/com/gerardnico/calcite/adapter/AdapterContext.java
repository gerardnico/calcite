package com.gerardnico.calcite.adapter;

import com.gerardnico.calcite.SchemaBuilder;
import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.RelRunner;

import java.util.List;

/**
 *
 * New adapters can be created by implementing {@link org.apache.calcite.jdbc.CalcitePrepare#convert(org.apache.calcite.jdbc.CalcitePrepare.Context, java.lang.String)}
 */
public class AdapterContext implements CalcitePrepare.Context {
    @Override
    public JavaTypeFactory getTypeFactory() {
        // adapter implementation
        return null;
    }

    @Override
    public CalciteSchema getRootSchema() {
        // adapter implementation
        final SchemaPlus jdbcFoodMart =  SchemaBuilder.get()
                .addSchema(SchemaBuilder.SchemaSpec.JDBC_FOODMART)
                .getSchema();
        return jdbcFoodMart.unwrap(CalciteSchema.class);
    }

    @Override
    public CalciteSchema getMutableRootSchema() {
        return null;
    }

    @Override
    public List<String> getDefaultSchemaPath() {
        return null;
    }

    @Override
    public CalciteConnectionConfig config() {
        return null;
    }

    @Override
    public CalcitePrepare.SparkHandler spark() {
        return null;
    }

    @Override
    public DataContext getDataContext() {
        return null;
    }

    @Override
    public List<String> getObjectPath() {
        return null;
    }

    @Override
    public RelRunner getRelRunner() {
        return null;
    }
}
