package com.gerardnico.calcite;

import org.junit.Test;

/**
 * A catalog in Calcite is a JDBC catalog
 * they returns {@link org.apache.calcite.adapter.jdbc.JdbcSchema}
 */
public class CalciteCatalogsTest {


    /**
     * Show how to read and print catalog
     */
    @Test
    public void catalogReaderTest() {

        CalciteCatalogs.printMock();

    }
}
