package com.gerardnico.calcite.schema;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.*;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.ImmutableBitSet;

import java.sql.Date;

public class StreamableTableOrderDetails implements StreamableTable {

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return new RelDataTypeFactory.Builder(typeFactory)
                .add("CK_TIME", typeFactory.createJavaType(Date.class))
                .add("ITEM_ID", typeFactory.createJavaType(Long.class))
                .add("ITEM_PRICE", typeFactory.createJavaType(Double.class))
                .add("BUYER_NAME", typeFactory.createJavaType(String.class))
                .add("QUANTITY", typeFactory.createJavaType(Integer.class))
                .build();
    }

    @Override
    public Statistic getStatistic() {
        RelFieldCollation.Direction dir = RelFieldCollation.Direction.ASCENDING;
        RelFieldCollation collation = new RelFieldCollation(0, dir, RelFieldCollation.NullDirection.UNSPECIFIED);
        return Statistics.of(5, ImmutableList.of(ImmutableBitSet.of(0)),
                ImmutableList.of(RelCollations.of(collation)));
    }

    @Override
    public Schema.TableType getJdbcTableType() {
        return Schema.TableType.STREAM;
    }

    @Override
    public boolean isRolledUp(String column) {
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call, SqlNode parent, CalciteConnectionConfig config) {
        return false;
    }

    public Table stream() {
        return null;
    }
}
