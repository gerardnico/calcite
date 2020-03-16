package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockJdbcCatalogReader;
import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.junit.Test;

/**
 * A catalog in Calcite is a JDBC catalog
 * they returns {@link org.apache.calcite.adapter.jdbc.JdbcSchema}
 */
public class CalciteCatalogsTest {


    /**
     * Show how to read a catalog
     */
    @Test
    public void catalogReaderTest() {

        RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        MockJdbcCatalogReader catalogReader = new MockCatalogReaderSimple(typeFactory, true).init();
        CalciteCatalogs.print(catalogReader);

    }
}
