package com.gerardnico.calcite;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.adapter.clone.CloneSchema;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.materialize.Lattice;
import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.runtime.GeoFunctions;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TableFunction;
import org.apache.calcite.schema.Wrapper;
import org.apache.calcite.schema.impl.*;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.type.SqlTypeName;

import javax.sql.DataSource;

/**
 * Extract of org.apache.calcite.test.CalciteAssert to get only the schema
 */
public class CalciteAssert {

    /**
     * Which database to use for tests that require a JDBC data source.
     *
     * @see CalciteSystemProperty#TEST_DB
     **/
    public static final RelBuilderExample.DatabaseInstance DB =
            RelBuilderExample.DatabaseInstance.valueOf(CalciteSystemProperty.TEST_DB.value());

    public static SchemaPlus addSchema(SchemaPlus rootSchema, SchemaSpec schema) {
        final SchemaPlus foodmart;
        final SchemaPlus jdbcScott;
        final SchemaPlus scott;
        final ConnectionSpec cs;
        final DataSource dataSource;
        switch (schema) {
            case REFLECTIVE_FOODMART:
                return rootSchema.add(schema.schemaName,
                        new ReflectiveSchema(new JdbcTest.FoodmartSchema()));
            case JDBC_SCOTT:
                cs = RelBuilderExample.DatabaseInstance.HSQLDB.scott;
                dataSource = JdbcSchema.dataSource(cs.url, cs.driver, cs.username,
                        cs.password);
                return rootSchema.add(schema.schemaName,
                        JdbcSchema.create(rootSchema, schema.schemaName, dataSource,
                                cs.catalog, cs.schema));
            case JDBC_FOODMART:
                cs = DB.foodmart;
                dataSource =
                        JdbcSchema.dataSource(cs.url, cs.driver, cs.username, cs.password);
                return rootSchema.add(schema.schemaName,
                        JdbcSchema.create(rootSchema, schema.schemaName, dataSource,
                                cs.catalog, cs.schema));
            case JDBC_FOODMART_WITH_LATTICE:
                foodmart = addSchemaIfNotExists(rootSchema, SchemaSpec.JDBC_FOODMART);
                foodmart.add(schema.schemaName,
                        Lattice.create(foodmart.unwrap(CalciteSchema.class),
                                "select 1 from \"foodmart\".\"sales_fact_1997\" as s\n"
                                        + "join \"foodmart\".\"time_by_day\" as t using (\"time_id\")\n"
                                        + "join \"foodmart\".\"customer\" as c using (\"customer_id\")\n"
                                        + "join \"foodmart\".\"product\" as p using (\"product_id\")\n"
                                        + "join \"foodmart\".\"product_class\" as pc on p.\"product_class_id\" = pc.\"product_class_id\"",
                                true));
                return foodmart;
            case SCOTT:
                jdbcScott = addSchemaIfNotExists(rootSchema, SchemaSpec.JDBC_SCOTT);
                return rootSchema.add(schema.schemaName, new CloneSchema(jdbcScott));
            case SCOTT_WITH_TEMPORAL:
                scott = addSchemaIfNotExists(rootSchema, SchemaSpec.SCOTT);
                scott.add("products_temporal", new StreamTest.ProductsTemporalTable());
                scott.add("orders",
                        new StreamTest.OrdersHistoryTable(
                                StreamTest.OrdersStreamTableFactory.getRowList()));
                return scott;
            case CLONE_FOODMART:
                foodmart = addSchemaIfNotExists(rootSchema, SchemaSpec.JDBC_FOODMART);
                return rootSchema.add("foodmart2", new CloneSchema(foodmart));
            case GEO:
                ModelHandler.addFunctions(rootSchema, null, ImmutableList.of(),
                        GeoFunctions.class.getName(), "*", true);
                final SchemaPlus s =
                        rootSchema.add(schema.schemaName, new AbstractSchema());
                ModelHandler.addFunctions(s, "countries", ImmutableList.of(),
                        CountriesTableFunction.class.getName(), null, false);
                final String sql = "select * from table(\"countries\"(true))";
                final ViewTableMacro viewMacro = ViewTable.viewMacro(rootSchema, sql,
                        ImmutableList.of("GEO"), ImmutableList.of(), false);
                s.add("countries", viewMacro);
                return s;
            case HR:
                return rootSchema.add(schema.schemaName,
                        new ReflectiveSchema(new JdbcTest.HrSchema()));
            case LINGUAL:
                return rootSchema.add(schema.schemaName,
                        new ReflectiveSchema(new JdbcTest.LingualSchema()));
            case BLANK:
                return rootSchema.add(schema.schemaName, new AbstractSchema());
            case ORINOCO:
                final SchemaPlus orinoco =
                        rootSchema.add(schema.schemaName, new AbstractSchema());
                orinoco.add("ORDERS",
                        new StreamTest.OrdersHistoryTable(
                                StreamTest.OrdersStreamTableFactory.getRowList()));
                return orinoco;
            case POST:
                final SchemaPlus post =
                        rootSchema.add(schema.schemaName, new AbstractSchema());
                post.add("EMP",
                        ViewTable.viewMacro(post,
                                "select * from (values\n"
                                        + "    ('Jane', 10, 'F'),\n"
                                        + "    ('Bob', 10, 'M'),\n"
                                        + "    ('Eric', 20, 'M'),\n"
                                        + "    ('Susan', 30, 'F'),\n"
                                        + "    ('Alice', 30, 'F'),\n"
                                        + "    ('Adam', 50, 'M'),\n"
                                        + "    ('Eve', 50, 'F'),\n"
                                        + "    ('Grace', 60, 'F'),\n"
                                        + "    ('Wilma', cast(null as integer), 'F'))\n"
                                        + "  as t(ename, deptno, gender)",
                                ImmutableList.of(), ImmutableList.of("POST", "EMP"),
                                null));
                post.add("DEPT",
                        ViewTable.viewMacro(post,
                                "select * from (values\n"
                                        + "    (10, 'Sales'),\n"
                                        + "    (20, 'Marketing'),\n"
                                        + "    (30, 'Engineering'),\n"
                                        + "    (40, 'Empty')) as t(deptno, dname)",
                                ImmutableList.of(), ImmutableList.of("POST", "DEPT"),
                                null));
                post.add("DEPT30",
                        ViewTable.viewMacro(post,
                                "select * from dept where deptno = 30",
                                ImmutableList.of("POST"), ImmutableList.of("POST", "DEPT30"),
                                null));
                post.add("EMPS",
                        ViewTable.viewMacro(post,
                                "select * from (values\n"
                                        + "    (100, 'Fred',  10, CAST(NULL AS CHAR(1)), CAST(NULL AS VARCHAR(20)), 40,               25, TRUE,    FALSE, DATE '1996-08-03'),\n"
                                        + "    (110, 'Eric',  20, 'M',                   'San Francisco',           3,                80, UNKNOWN, FALSE, DATE '2001-01-01'),\n"
                                        + "    (110, 'John',  40, 'M',                   'Vancouver',               2, CAST(NULL AS INT), FALSE,   TRUE,  DATE '2002-05-03'),\n"
                                        + "    (120, 'Wilma', 20, 'F',                   CAST(NULL AS VARCHAR(20)), 1,                 5, UNKNOWN, TRUE,  DATE '2005-09-07'),\n"
                                        + "    (130, 'Alice', 40, 'F',                   'Vancouver',               2, CAST(NULL AS INT), FALSE,   TRUE,  DATE '2007-01-01'))\n"
                                        + " as t(empno, name, deptno, gender, city, empid, age, slacker, manager, joinedat)",
                                ImmutableList.of(), ImmutableList.of("POST", "EMPS"),
                                null));
                post.add("TICKER",
                        ViewTable.viewMacro(post,
                                "select * from (values\n"
                                        + "    ('ACME', '2017-12-01', 12),\n"
                                        + "    ('ACME', '2017-12-02', 17),\n"
                                        + "    ('ACME', '2017-12-03', 19),\n"
                                        + "    ('ACME', '2017-12-04', 21),\n"
                                        + "    ('ACME', '2017-12-05', 25),\n"
                                        + "    ('ACME', '2017-12-06', 12),\n"
                                        + "    ('ACME', '2017-12-07', 15),\n"
                                        + "    ('ACME', '2017-12-08', 20),\n"
                                        + "    ('ACME', '2017-12-09', 24),\n"
                                        + "    ('ACME', '2017-12-10', 25),\n"
                                        + "    ('ACME', '2017-12-11', 19),\n"
                                        + "    ('ACME', '2017-12-12', 15),\n"
                                        + "    ('ACME', '2017-12-13', 25),\n"
                                        + "    ('ACME', '2017-12-14', 25),\n"
                                        + "    ('ACME', '2017-12-15', 14),\n"
                                        + "    ('ACME', '2017-12-16', 12),\n"
                                        + "    ('ACME', '2017-12-17', 14),\n"
                                        + "    ('ACME', '2017-12-18', 24),\n"
                                        + "    ('ACME', '2017-12-19', 23),\n"
                                        + "    ('ACME', '2017-12-20', 22))\n"
                                        + " as t(SYMBOL, tstamp, price)",
                                ImmutableList.<String>of(), ImmutableList.of("POST", "TICKER"),
                                null));
                return post;
            case FAKE_FOODMART:
                // Similar to FOODMART, but not based on JdbcSchema.
                // Contains 2 tables that do not extend JdbcTable.
                // They redirect requests for SqlDialect and DataSource to the real JDBC
                // FOODMART, and this allows statistics queries to be executed.
                foodmart = addSchemaIfNotExists(rootSchema, SchemaSpec.JDBC_FOODMART);
                final Wrapper salesTable = (Wrapper) foodmart.getTable("sales_fact_1997");
                SchemaPlus fake =
                        rootSchema.add(schema.schemaName, new AbstractSchema());
                fake.add("time_by_day", new AbstractTable() {
                    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
                        return typeFactory.builder()
                                .add("time_id", SqlTypeName.INTEGER)
                                .add("the_year", SqlTypeName.INTEGER)
                                .build();
                    }

                    public <C> C unwrap(Class<C> aClass) {
                        if (aClass.isAssignableFrom(SqlDialect.class)
                                || aClass.isAssignableFrom(DataSource.class)) {
                            return salesTable.unwrap(aClass);
                        }
                        return super.unwrap(aClass);
                    }
                });
                fake.add("sales_fact_1997", new AbstractTable() {
                    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
                        return typeFactory.builder()
                                .add("time_id", SqlTypeName.INTEGER)
                                .add("customer_id", SqlTypeName.INTEGER)
                                .build();
                    }

                    public <C> C unwrap(Class<C> aClass) {
                        if (aClass.isAssignableFrom(SqlDialect.class)
                                || aClass.isAssignableFrom(DataSource.class)) {
                            return salesTable.unwrap(aClass);
                        }
                        return super.unwrap(aClass);
                    }
                });
                return fake;
            case AUX:
                SchemaPlus aux =
                        rootSchema.add(schema.schemaName, new AbstractSchema());
                TableFunction tableFunction =
                        TableFunctionImpl.create(Smalls.SimpleTableFunction.class, "eval");
                aux.add("TBLFUN", tableFunction);
                final String simpleSql = "select *\n"
                        + "from (values\n"
                        + "    ('ABC', 1),\n"
                        + "    ('DEF', 2),\n"
                        + "    ('GHI', 3))\n"
                        + "  as t(strcol, intcol)";
                aux.add("SIMPLETABLE",
                        ViewTable.viewMacro(aux, simpleSql, ImmutableList.of(),
                                ImmutableList.of("AUX", "SIMPLETABLE"), null));
                final String lateralSql = "SELECT *\n"
                        + "FROM AUX.SIMPLETABLE ST\n"
                        + "CROSS JOIN LATERAL TABLE(AUX.TBLFUN(ST.INTCOL))";
                aux.add("VIEWLATERAL",
                        ViewTable.viewMacro(aux, lateralSql, ImmutableList.of(),
                                ImmutableList.of("AUX", "VIEWLATERAL"), null));
                return aux;
            case BOOKSTORE:
                return rootSchema.add(schema.schemaName,
                        new ReflectiveSchema(new BookstoreSchema()));
            default:
                throw new AssertionError("unknown schema " + schema);
        }
    }

    /** Specification for common test schemas. */
    public enum SchemaSpec {
        REFLECTIVE_FOODMART("foodmart"),
        FAKE_FOODMART("foodmart"),
        JDBC_FOODMART("foodmart"),
        CLONE_FOODMART("foodmart2"),
        JDBC_FOODMART_WITH_LATTICE("lattice"),
        GEO("GEO"),
        HR("hr"),
        JDBC_SCOTT("JDBC_SCOTT"),
        SCOTT("scott"),
        SCOTT_WITH_TEMPORAL("scott_temporal"),
        BLANK("BLANK"),
        LINGUAL("SALES"),
        POST("POST"),
        ORINOCO("ORINOCO"),
        AUX("AUX"),
        BOOKSTORE("bookstore");

        /** The name of the schema that is usually created from this specification.
         * (Names are not unique, and you can use another name if you wish.) */
        public final String schemaName;

        SchemaSpec(String schemaName) {
            this.schemaName = schemaName;
        }
    }

    private static SchemaPlus addSchemaIfNotExists(SchemaPlus rootSchema,
                                                   SchemaSpec schemaSpec) {
        final SchemaPlus schema = rootSchema.getSubSchema(schemaSpec.schemaName);
        if (schema != null) {
            return schema;
        }
        return addSchema(rootSchema, schemaSpec);
    }


}
