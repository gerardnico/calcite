package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockViewExpander;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;

import static com.gerardnico.calcite.CalcitePlanner.getRelOptPlanner;

public class CalciteSqlNode {

    /**
     * Validate the SQl Node against the schema
     * @return a validated sql node with a SqlValidator
     * Example: See {@link CalciteSqlValidator#createSqlValidator()}
     */
    public static SqlNode validate(CalciteSqlValidator sqlValidator, SqlNode sqlNode) {
        return sqlValidator.validate(sqlNode);
    }

    /**
     * From SqlToRel
     *
     * @return
     */
    public static RelRoot fromSqlNodeToRelNode(SqlNode sqlNode) {

        final CalciteSqlValidator sqlValidator = CalciteSqlValidator.createSqlValidator();

        // SqlToRelConverter.Config localConfig = SqlToRelConverter.Config.DEFAULT;
        SqlToRelConverter.Config sqlToRelConfig = SqlToRelConverter.configBuilder()
                .withTrimUnusedFields(true)
                .withExpand(true)
                .build();


        final RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        final RexBuilder rexBuilder = new RexBuilder(typeFactory);
        final RelOptCluster cluster = RelOptCluster.create(getRelOptPlanner(), rexBuilder);

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
     *
     * @param sql
     * @return a SqlNode from a Sql
     */
    public static SqlNode fromSqlToSqlNode(String sql){
        return CalciteSql.fromSqlToSqlNode(sql);
    }
}
