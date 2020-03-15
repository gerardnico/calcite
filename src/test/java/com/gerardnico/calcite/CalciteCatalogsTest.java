package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockCatalogReader;
import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.junit.Test;

public class CalciteCatalogsTest {


    /**
     * Show how to read a catalog
     */
    @Test
    public void catalogReaderTest() {

        RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        MockCatalogReader catalogReader = new MockCatalogReaderSimple(typeFactory, true).init();
        CalciteCatalogs.print(catalogReader);

    }
}
