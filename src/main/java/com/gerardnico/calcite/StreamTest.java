package com.gerardnico.calcite;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.DataContext;
import org.apache.calcite.avatica.util.DateTimeUtils;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.*;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.Iterator;
import java.util.Map;

/**
 * Extract of org.apache.calcite.test.StreamTest to get only the tables
 * and not the test
 */
public class StreamTest {
    /**
     * Base table for the Orders table. Manages the base schema used for the test tables and common
     * functions.
     */
    private abstract static class BaseOrderStreamTable implements ScannableTable {
        protected final RelProtoDataType protoRowType = a0 -> a0.builder()
                .add("ROWTIME", SqlTypeName.TIMESTAMP)
                .add("ID", SqlTypeName.INTEGER)
                .add("PRODUCT", SqlTypeName.VARCHAR, 10)
                .add("UNITS", SqlTypeName.INTEGER)
                .build();

        public RelDataType getRowType(RelDataTypeFactory typeFactory) {
            return protoRowType.apply(typeFactory);
        }

        public Statistic getStatistic() {
            return Statistics.of(100d, ImmutableList.of(),
                    RelCollations.createSingleton(0));
        }

        public Schema.TableType getJdbcTableType() {
            return Schema.TableType.TABLE;
        }

        @Override
        public boolean isRolledUp(String column) {
            return false;
        }

        @Override
        public boolean rolledUpColumnValidInsideAgg(String column,
                                                    SqlCall call, SqlNode parent, CalciteConnectionConfig config) {
            return false;
        }
    }

    /**
     * Mock table that returns a stream of orders from a fixed array.
     */
    @SuppressWarnings("UnusedDeclaration")
    public static class OrdersStreamTableFactory implements TableFactory<Table> {
        // public constructor, per factory contract
        public OrdersStreamTableFactory() {
        }

        public Table create(SchemaPlus schema, String name,
                            Map<String, Object> operand, RelDataType rowType) {
            return new OrdersTable(getRowList());
        }

        public static ImmutableList<Object[]> getRowList() {
            final Object[][] rows = {
                    {ts(10, 15, 0), 1, "paint", 10},
                    {ts(10, 24, 15), 2, "paper", 5},
                    {ts(10, 24, 45), 3, "brush", 12},
                    {ts(10, 58, 0), 4, "paint", 3},
                    {ts(11, 10, 0), 5, "paint", 3}
            };
            return ImmutableList.copyOf(rows);
        }

        private static Object ts(int h, int m, int s) {
            return DateTimeUtils.unixTimestamp(2015, 2, 15, h, m, s);
        }
    }

    /**
     * Table representing the ORDERS stream.
     */
    public static class OrdersTable extends BaseOrderStreamTable
            implements StreamableTable {
        private final ImmutableList<Object[]> rows;

        public OrdersTable(ImmutableList<Object[]> rows) {
            this.rows = rows;
        }

        public Enumerable<Object[]> scan(DataContext root) {
            return Linq4j.asEnumerable(rows);
        }

        @Override
        public Table stream() {
            return new OrdersTable(rows);
        }

        @Override
        public boolean isRolledUp(String column) {
            return false;
        }

        @Override
        public boolean rolledUpColumnValidInsideAgg(String column,
                                                    SqlCall call, SqlNode parent, CalciteConnectionConfig config) {
            return false;
        }
    }

    /**
     * Mock table that returns a stream of orders from a fixed array.
     */
    @SuppressWarnings("UnusedDeclaration")
    public static class InfiniteOrdersStreamTableFactory implements TableFactory<Table> {
        // public constructor, per factory contract
        public InfiniteOrdersStreamTableFactory() {
        }

        public Table create(SchemaPlus schema, String name,
                            Map<String, Object> operand, RelDataType rowType) {
            return new InfiniteOrdersTable();
        }
    }

    /**
     * Table representing an infinitely larger ORDERS stream.
     */
    public static class InfiniteOrdersTable extends BaseOrderStreamTable
            implements StreamableTable {
        public Enumerable<Object[]> scan(DataContext root) {
            return Linq4j.asEnumerable(() -> new Iterator<Object[]>() {
                private final String[] items = {"paint", "paper", "brush"};
                private int counter = 0;

                public boolean hasNext() {
                    return true;
                }

                public Object[] next() {
                    final int index = counter++;
                    return new Object[]{
                            System.currentTimeMillis(), index, items[index % items.length], 10};
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            });
        }

        public Table stream() {
            return this;
        }
    }

    /**
     * Table representing the history of the ORDERS stream.
     */
    public static class OrdersHistoryTable extends BaseOrderStreamTable {
        private final ImmutableList<Object[]> rows;

        public OrdersHistoryTable(ImmutableList<Object[]> rows) {
            this.rows = rows;
        }

        public Enumerable<Object[]> scan(DataContext root) {
            return Linq4j.asEnumerable(rows);
        }
    }

    /**
     * Mocks a simple relation to use for stream joining test.
     */
    public static class ProductsTableFactory implements TableFactory<Table> {
        public Table create(SchemaPlus schema, String name,
                            Map<String, Object> operand, RelDataType rowType) {
            final Object[][] rows = {
                    {"paint", 1},
                    {"paper", 0},
                    {"brush", 1}
            };
            return new ProductsTable(ImmutableList.copyOf(rows));
        }
    }

    /**
     * Table representing the PRODUCTS relation.
     */
    public static class ProductsTable implements ScannableTable {
        private final ImmutableList<Object[]> rows;

        public ProductsTable(ImmutableList<Object[]> rows) {
            this.rows = rows;
        }

        private final RelProtoDataType protoRowType = a0 -> a0.builder()
                .add("ID", SqlTypeName.VARCHAR, 32)
                .add("SUPPLIER", SqlTypeName.INTEGER)
                .build();

        public Enumerable<Object[]> scan(DataContext root) {
            return Linq4j.asEnumerable(rows);
        }

        public RelDataType getRowType(RelDataTypeFactory typeFactory) {
            return protoRowType.apply(typeFactory);
        }

        public Statistic getStatistic() {
            return Statistics.of(200d, ImmutableList.of());
        }

        public Schema.TableType getJdbcTableType() {
            return Schema.TableType.TABLE;
        }

        @Override
        public boolean isRolledUp(String column) {
            return false;
        }

        @Override
        public boolean rolledUpColumnValidInsideAgg(String column,
                                                    SqlCall call, SqlNode parent, CalciteConnectionConfig config) {
            return false;
        }
    }

    /**
     * Table representing the PRODUCTS_TEMPORAL temporal table.
     */
    public static class ProductsTemporalTable implements TemporalTable {

        private final RelProtoDataType protoRowType = a0 -> a0.builder()
                .add("ID", SqlTypeName.VARCHAR, 32)
                .add("SUPPLIER", SqlTypeName.INTEGER)
                .add("SYS_START", SqlTypeName.TIMESTAMP)
                .add("SYS_END", SqlTypeName.TIMESTAMP)
                .build();

        @Override
        public String getSysStartFieldName() {
            return "SYS_START";
        }

        @Override
        public String getSysEndFieldName() {
            return "SYS_END";
        }

        @Override
        public RelDataType getRowType(RelDataTypeFactory typeFactory) {
            return protoRowType.apply(typeFactory);
        }

        @Override
        public Statistic getStatistic() {
            return Statistics.of(200d, ImmutableList.of());
        }

        @Override
        public Schema.TableType getJdbcTableType() {
            return Schema.TableType.TABLE;
        }

        @Override
        public boolean isRolledUp(String column) {
            return false;
        }

        @Override
        public boolean rolledUpColumnValidInsideAgg(String column,
                                                    SqlCall call, SqlNode parent, CalciteConnectionConfig config) {
            return false;
        }
    }

}
