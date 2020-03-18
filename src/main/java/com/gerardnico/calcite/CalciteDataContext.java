package com.gerardnico.calcite;

import org.apache.calcite.DataContext;
import org.apache.calcite.schema.SchemaPlus;

/**
 * DataContext is a runtime context allowing access to the tables in a database.
 */
public class CalciteDataContext {

    static SchemaPlus getRootSchema(DataContext dataContext) {
        return dataContext.getRootSchema();
    }

}
