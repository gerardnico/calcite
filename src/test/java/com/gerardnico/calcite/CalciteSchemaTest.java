package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.queries.HrFoodmartQuery;
import org.apache.calcite.jdbc.CalciteConnection;
import org.junit.Test;

public class CalciteSchemaTest {

    /**
     * Example of using Calcite via JDBC.
     *
     * <p>Schema is specified programmatically via reflection on class.</p>
     * <p>
     * Copy of <a href=https://github.com/apache/calcite/blob/master/core/src/test/java/org/apache/calcite/examples/foodmart/java/JdbcExample.java>JdbcExample</a>
     */
    @Test
    public void reflectiveWithCode() {

        CalciteConnection calciteConnection = CalciteConnections.getConnectionWithoutModel();
        CalciteSchema.addReflectiveSchemaToConnection(calciteConnection);
        HrFoodmartQuery.executeSampleQuery(calciteConnection);

    }

    @Test
    public void reflectiveWithModel() {

        CalciteConnection calciteConnection = CalciteSchema.addReflectiveSchemaToConnectionViaModel();
        HrFoodmartQuery.executeSampleQuery(calciteConnection);

    }

}
