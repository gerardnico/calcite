package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import com.gerardnico.calcite.mock.MockJdbcCatalogReader;
import com.gerardnico.calcite.mock.MockSqlOperatorTable;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.validate.*;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.ValidationException;

public class CalciteSqlValidation {


    /**
     *
     * @param planner
     * @param sqlNode
     * @return the validated node
     */
    static SqlNode validateFromPlanner(Planner planner, SqlNode sqlNode) {
        try {
            return planner.validate(sqlNode);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param sqlNode
     * @return the validated node
     */
    static SqlNode validateFromCustomValidator(SqlNode sqlNode)  {
        return createCustomSqlValidator().validate(sqlNode);
    }

    /**
     * Sample code that returns a custom sql validator
     * @return
     */
    static public CalciteSqlValidatorCustom createCustomSqlValidator() {

        RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        MockJdbcCatalogReader catalogReader = new MockCatalogReaderSimple(typeFactory, true).init();
        MockSqlOperatorTable sqlOperatorTable = new MockSqlOperatorTable(SqlStdOperatorTable.instance());
        MockSqlOperatorTable.addRamp(sqlOperatorTable);

        CalciteSqlValidatorCustom sqlValidator = new CalciteSqlValidatorCustom(
                sqlOperatorTable,
                catalogReader,
                typeFactory,
                SqlConformanceEnum.DEFAULT);

        sqlValidator.setEnableTypeCoercion(true);

        final CalciteConnectionConfig calciteConnectionConfig = CalciteConnections.getContext().unwrap(CalciteConnectionConfig.class);
        if (calciteConnectionConfig != null) {
            sqlValidator.setDefaultNullCollation(calciteConnectionConfig.defaultNullCollation());
        }

        return sqlValidator;
    }

    /**
     * Build a validator with:
     *   * the {@link MockCatalogReaderSimple}
     *   * a {@link SqlConformanceEnum#DEFAULT}
     *   * and
     * @param sqlNode
     * @return the validated node
     */
    static SqlNode validateFromUtilValidatorPlanner(Prepare.CatalogReader catalogReader, SqlNode sqlNode) {

        SqlStdOperatorTable operatorTable = SqlStdOperatorTable.instance();
        SqlValidatorWithHints sqlValidator = SqlValidatorUtil.newValidator(
                operatorTable,
                catalogReader,
                catalogReader.getTypeFactory(),
                SqlConformanceEnum.DEFAULT
        );
        return sqlValidator.validate(sqlNode);

    }
}
