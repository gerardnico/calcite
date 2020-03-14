package com.gerardnico.calcite.adpater;

import com.gerardnico.calcite.adapter.AdapterContext;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.Driver;
import org.apache.calcite.prepare.CalcitePrepareImpl;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Not really up to date information
 * <a href=https://calcite.apache.org/docs/howto.html#testing-adapter-in-java>Testing adapter in Java</a>
 */
public class AdapterContextTest {
    @Test
    public void testSelectAllFromTable() throws SQLException {
        AdapterContext ctx = new AdapterContext();
        List<String> defaultSchemaPath = ctx.getDefaultSchemaPath();
        CalciteSchema rootSchema = ctx.getRootSchema();

        String sql = "SELECT * FROM TABLENAME";
        CalcitePrepare.Query query = CalcitePrepare.Query.of(sql);
        Class elementType = Object[].class;
        //CalcitePrepare.CalciteSignature<Object> prepared = new CalcitePrepareImpl().prepareSql(ctx, query, elementType, -1);

        Driver driver = new Driver();
        Connection connection = driver.connect("url",null);
//        connection.create
//        prepared.enumerable(ctx);
//        Object enumerable = prepared.getExecutable();
        // etc.
    }
}
