package com.gerardnico.calcite.mock;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;

import java.util.List;


/**
 * {@link RelOptTable.ViewExpander} implementation for testing usage.
 */
public class MockViewExpander implements RelOptTable.ViewExpander {
    private final SqlValidator validator;
    private final Prepare.CatalogReader catalogReader;
    private final RelOptCluster cluster;
    private final SqlToRelConverter.Config config;

    public MockViewExpander(
            SqlValidator validator,
            Prepare.CatalogReader catalogReader,
            RelOptCluster cluster,
            SqlToRelConverter.Config config) {
        this.validator = validator;
        this.catalogReader = catalogReader;
        this.cluster = cluster;
        this.config = config;
    }

    @Override public RelRoot expandView(RelDataType rowType, String queryString,
                                        List<String> schemaPath, List<String> viewPath) {
        try {
            SqlNode parsedNode = SqlParser.create(queryString).parseStmt();
            SqlNode validatedNode = validator.validate(parsedNode);
            SqlToRelConverter converter = new SqlToRelConverter(
                    this, validator, catalogReader, cluster,
                    StandardConvertletTable.INSTANCE, config);
            return converter.convertQuery(validatedNode, false, true);
        } catch (SqlParseException e) {
            throw new RuntimeException("Error happened while expanding view.", e);
        }
    }
}
