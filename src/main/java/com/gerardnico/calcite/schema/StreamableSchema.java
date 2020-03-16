package com.gerardnico.calcite.schema;

import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.Frameworks;

/**
 * Adapted from <a href="https://mingminxu.com/tag/sql-parser/">QUERY EXPLAIN WITH CALCITE</a>
 */
public class StreamableSchema {

    static public SchemaPlus getSchema() {

        final SchemaPlus rootSchema = Frameworks.createRootSchema(true);

        rootSchema.add("LISTING_DETAILS", new ScannableTableListingDetails());
        rootSchema.add("ORDER_DETAILS", new StreamableTableOrderDetails());
        return rootSchema;
    }

}
