package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.SchemaReflective;
import com.gerardnico.calcite.schema.queries.HrFoodmartQuery;
import org.apache.calcite.jdbc.CalciteConnection;
import org.junit.Test;

public class CalciteSchemaReflectiveTest {

    @Test
    public void reflectiveWithCode() {
        SchemaReflective schemaReflective = new SchemaReflective();

        CalciteConnection calciteConnection = schemaReflective.createConnectionWithSchemaInCode();
        HrFoodmartQuery.executeSampleQuery(calciteConnection);

    }

    @Test
    public void reflectiveWithModel() {

        SchemaReflective schemaReflective = new SchemaReflective();

        CalciteConnection calciteConnection = schemaReflective.createConnectionWithModel();
        HrFoodmartQuery.executeSampleQuery(calciteConnection);

    }
}
