package com.gerardnico.calcite;

import org.apache.calcite.prepare.Prepare;

/**
 * Static info on catalog
 */
public class Catalogs {


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

}
