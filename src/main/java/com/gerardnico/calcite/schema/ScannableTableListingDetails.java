package com.gerardnico.calcite.schema;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.DataContext;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;


public class ScannableTableListingDetails implements ScannableTable {

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return null;
    }

    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return new RelDataTypeFactory.Builder(typeFactory)
                .add("LIST_ID", typeFactory.createJavaType(Long.class))
                .add("ITEM_PRICE", typeFactory.createJavaType(Double.class))
                .add("SELLER_NAME", typeFactory.createJavaType(String.class))
                .add("AVAIL_QUANTITY", typeFactory.createJavaType(Integer.class))
                .build();
    }

    public Statistic getStatistic() {
        return Statistics.of(100, ImmutableList.of());
    }

    public Schema.TableType getJdbcTableType() {
        return Schema.TableType.TABLE;
    }

    @Override
    public boolean isRolledUp(String column) {
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call, SqlNode parent, CalciteConnectionConfig config) {
        return false;
    }

}
