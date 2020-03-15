package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockCatalogReader;
import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import com.gerardnico.calcite.mock.MockSqlOperatorTable;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.validate.SqlConformance;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidatorCatalogReader;
import org.apache.calcite.sql.validate.SqlValidatorImpl;

/**
 * A validator is an object that will validate a {@link org.apache.calcite.rel.RelRoot}
 * against the schema (do we have the table, ...)
 *
 * Factory method is {@link #createSqlValidator()}
 *
 * This class that originates from the FarragoTestValidator
 */
public class CalciteSqlValidator extends SqlValidatorImpl {

    CalciteSqlValidator(
            SqlOperatorTable sqlOperatorTable,
            SqlValidatorCatalogReader catalogReader,
            RelDataTypeFactory typeFactory,
            SqlConformance conformance) {
        super(sqlOperatorTable, catalogReader, typeFactory, conformance);
    }

    @Override
    public boolean shouldExpandIdentifiers() {
        return true;
    }

    /**
     * Sample code that create a sql validator
     * @return
     */
    static public CalciteSqlValidator createSqlValidator() {

        RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        MockCatalogReader catalogReader = new MockCatalogReaderSimple(typeFactory, true).init();
        MockSqlOperatorTable sqlOperatorTable = new MockSqlOperatorTable(SqlStdOperatorTable.instance());
        MockSqlOperatorTable.addRamp(sqlOperatorTable);

        CalciteSqlValidator sqlValidator = new CalciteSqlValidator(
                sqlOperatorTable,
                catalogReader,
                typeFactory,
                SqlConformanceEnum.DEFAULT);

        sqlValidator.setEnableTypeCoercion(true);

        final CalciteConnectionConfig calciteConnectionConfig = CalciteConnectionStatic.getContext().unwrap(CalciteConnectionConfig.class);
        if (calciteConnectionConfig != null) {
            sqlValidator.setDefaultNullCollation(calciteConnectionConfig.defaultNullCollation());
        }

        return sqlValidator;
    }

    @Override
    public SqlValidatorCatalogReader getCatalogReader() {
        return super.getCatalogReader();
    }



}
