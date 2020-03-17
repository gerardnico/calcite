package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.type.RelDataTypeFactory;

/**
 * Static info on catalog
 */
public class CalciteCatalogs {


    /**
     * Print info in a catalogReader
     * @param catalogReader
     */
    static public void print(Prepare.CatalogReader catalogReader) {
        System.out.println("Schemas:");
        catalogReader.getSchemaPaths().forEach(s -> System.out.println("  * Schema Paths: " + String.join(",", s)));
        System.out.println();
        System.out.println("Root Schema: " + catalogReader.getRootSchema().name);
        System.out.println("  Sub Schema: ");
        catalogReader.getRootSchema().getSubSchemaMap()
                .values()
                .forEach(s -> {
                    System.out.println("  * " + s.name);
                    s.getTableNames().forEach(t->System.out.println("     * "+t));
                });
    }

    /**
     *
     * @return a mock catalog with a list of schema:
     *   * order entry
     *   * hr
     *  To see the schema, you can {@link #printMock() print it}
     */
    public static Prepare.CatalogReader createMock() {
        RelDataTypeFactory sqlTypeFactory = CalciteRelDataType.createSqlTypeFactory();
        return new MockCatalogReaderSimple(sqlTypeFactory, true).init();
    }

    public static void printMock() {
        print(createMock());
    }
}
