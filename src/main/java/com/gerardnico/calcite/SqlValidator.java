package com.gerardnico.calcite;

import com.gerardnico.calcite.mock.MockCatalogReader;
import com.gerardnico.calcite.mock.MockCatalogReaderSimple;
import com.gerardnico.calcite.mock.MockSqlOperatorTable;
import org.apache.calcite.prepare.Prepare;
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
class SqlValidator extends SqlValidatorImpl {

    SqlValidator(
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

    static public SqlValidator createSqlValidator() {

        RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        MockCatalogReader catalogReader = new MockCatalogReaderSimple(typeFactory, true).init();
        MockSqlOperatorTable sqlOperatorTable = new MockSqlOperatorTable(SqlStdOperatorTable.instance());
        MockSqlOperatorTable.addRamp(sqlOperatorTable);

        SqlValidator sqlValidator = new SqlValidator(
                sqlOperatorTable,
                catalogReader,
                typeFactory,
                SqlConformanceEnum.DEFAULT);

        sqlValidator.setEnableTypeCoercion(true);

        return sqlValidator;
    }

    @Override
    public SqlValidatorCatalogReader getCatalogReader() {
        return super.getCatalogReader();
    }



}
