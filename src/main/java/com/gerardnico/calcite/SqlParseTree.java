package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.*;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.config.NullCollation;
import org.apache.calcite.plan.*;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;

import java.util.Objects;
import java.util.Properties;
import java.util.function.UnaryOperator;

public class SqlParseTree {

    /**
     * The original sql
     */
    private final String sql;

    /**
     * The tree representation of the sql
     */
    private SqlNode tree;

    /**
     * The config
     */
    private SqlWriterConfig config = SqlPrettyWriter.config();

    /**
     * What is this ?
     */
    private RelDataTypeFactory typeFactory;
    private RelOptPlanner planner;
    private SqlOperatorTable sqlOperatorTable;

    /**
     * Constructor
     *
     * @param sql
     */
    public SqlParseTree(String sql) {

        try {
            this.sql = sql;
            tree = org.apache.calcite.sql.parser.SqlParser.create(sql).parseQuery();
        } catch (SqlParseException e) {
            String message = "Received error while parsing SQL '" + sql + "'"
                    + "; error is:\n"
                    + e.toString();
            throw new AssertionError(message);
        }

    }

    /**
     * Parses a SQL query.
     */
    static public SqlParseTree createTreeFromSql(String sql) {
        return new SqlParseTree(sql);
    }

    /**
     * Print the tree
     */
    void print() {
        System.out.println(new SqlPrettyWriter(config).format(tree));
    }

    /**
     * @param transform - a lambda expression to change the writer config
     * @return Example: c->c.withLineFolding(SqlWriterConfig.LineFolding.STEP)
     */
    public SqlParseTree withWriterConfig(UnaryOperator<SqlWriterConfig> transform) {
        Objects.requireNonNull(transform);
        transform.apply(config);
        return this;
    }


    /**
     * From SqlToRelTestBase
     *
     * @return
     */
    public RelRoot getRelNodeFromSqlNode() {

        final SqlValidator sqlValidator = SqlValidator.createSqlValidator();
        final CalciteConnectionConfig calciteConnectionConfig = getContext().unwrap(CalciteConnectionConfig.class);
        if (calciteConnectionConfig != null) {
            sqlValidator.setDefaultNullCollation(calciteConnectionConfig.defaultNullCollation());
        }

        // SqlToRelConverter.Config localConfig = SqlToRelConverter.Config.DEFAULT;
        SqlToRelConverter.Config sqlToRelConfig = SqlToRelConverter.configBuilder()
                .withTrimUnusedFields(true)
                .withExpand(true)
                .build();

        final SqlNode validatedQuery = sqlValidator.validate(tree);
        final RexBuilder rexBuilder = new RexBuilder(typeFactory);
        final RelOptCluster cluster = RelOptCluster.create(getPlanner(), rexBuilder);
        final RelOptTable.ViewExpander viewExpander = new MockViewExpander(
                sqlValidator,
                (Prepare.CatalogReader) sqlValidator.getCatalogReader(),
                cluster,
                sqlToRelConfig);
        final SqlToRelConverter converter = new SqlToRelConverter(
                viewExpander,
                sqlValidator,
                (Prepare.CatalogReader) sqlValidator.getCatalogReader(),
                cluster,
                StandardConvertletTable.INSTANCE,
                sqlToRelConfig
        );

        RelRoot relRoot = converter.convertQuery(validatedQuery, false, true);
        assert relRoot != null;
        relRoot = relRoot
                .withRel(converter.flattenTypes(relRoot.rel, true))
                .withRel(converter.decorrelate(tree, relRoot.rel))
                .withRel(converter.trimUnusedFields(true, relRoot.rel));

        return relRoot;
    }

    /**
     * Validate the SQl against the schema
     * @return a validated sql node with a SqlValidator
     * Example: See {@link SqlValidator#createSqlValidator()}
     */
    SqlNode validate(SqlValidator sqlValidator) {
        return sqlValidator.validate(tree);
    }

    /**
     * @return a context from a connection :)
     */
    private Context getContext() {
        Properties properties = new Properties();
        properties.setProperty(
                CalciteConnectionProperty.DEFAULT_NULL_COLLATION.camelName(),
                NullCollation.LOW.name()
        );
        CalciteConnectionConfigImpl connectionConfig = new CalciteConnectionConfigImpl(properties);
        return Contexts.of(connectionConfig);
    }


    protected final RelOptPlanner getPlanner() {
        if (planner == null) {
            planner = new MockRelOptPlanner(getContext());
        }
        return planner;
    }


}
