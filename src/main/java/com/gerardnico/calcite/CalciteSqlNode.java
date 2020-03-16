package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockViewExpander;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;

import static com.gerardnico.calcite.CalcitePlanner.getMockRelOptPlanner;

public class CalciteSqlNode {

    /**
     * Validate the SQl Node against the schema with a planner object
     *
     * @return the validated node
     */
    public static SqlNode validate(Planner planner, SqlNode sqlNode) throws ValidationException {
        return CalciteSqlValidation.validateFromPlanner(planner, sqlNode);
    }

    /**
     * From SqlToRelRoot
     *
     * @return
     */
    public static RelRoot fromSqlNodeToRelRoot(SqlNode sqlNode) {

        final CalciteSqlValidatorCustom sqlValidator = CalciteSqlValidation.createCustomSqlValidator();

        // SqlToRelConverter.Config localConfig = SqlToRelConverter.Config.DEFAULT;
        SqlToRelConverter.Config sqlToRelConfig = SqlToRelConverter.configBuilder()
                .withTrimUnusedFields(true)
                .withExpand(true)
                .build();


        final RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        final RexBuilder rexBuilder = new RexBuilder(typeFactory);
        final RelOptCluster cluster = RelOptCluster.create(getMockRelOptPlanner(), rexBuilder);

        // Can expand a view into relational expressions.
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

        RelRoot relRoot = converter.convertQuery(sqlNode, false, true);
        assert relRoot != null;
        relRoot = relRoot
                .withRel(converter.flattenTypes(relRoot.rel, true))
                .withRel(converter.decorrelate(sqlNode, relRoot.rel))
                .withRel(converter.trimUnusedFields(true, relRoot.rel));

        return relRoot;
    }

    /**
     * From SqlToRelRoot
     *
     * @param planner - A planner utility. See {@link CalcitePlanner#getPlannerFromFrameworkConfig(FrameworkConfig)}
     * @return a relRoot
     */
    public static RelRoot fromSqlNodeToRelRootViaPlanner(Planner planner, SqlNode sqlNode) {
        try {
            return planner.rel(sqlNode);
        } catch (RelConversionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param sql
     * @return a SqlNode from a Sql
     */
    public static SqlNode fromSqlToSqlNode(String sql) {
        return CalciteSql.fromSqlToSqlNode(sql);
    }
}
