package com.gerardnico.calcite;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;

import java.sql.Date;

public class CalciteRelDataType {


    public static RelDataTypeSystem getDefaultSystem(){
        return RelDataTypeSystem.DEFAULT;
    }

    /**
     * Example how you would define a row in a table such as {@link org.apache.calcite.schema.StreamableTable#getRowType(RelDataTypeFactory)}
     * @param typeFactory
     * @return
     */
    public static RelDataType getRowType(RelDataTypeFactory typeFactory){
        return new RelDataTypeFactory.Builder(typeFactory)
                .add("DATE_ID", typeFactory.createJavaType(Date.class))
                .add("ITEM_ID", typeFactory.createJavaType(Long.class))
                .add("ITEM_PRICE", typeFactory.createJavaType(Double.class))
                .add("BUYER_NAME", typeFactory.createJavaType(String.class))
                .add("QUANTITY", typeFactory.createJavaType(Integer.class))
                .build();
    }

    /**
     * Sql Type factory
     * @return
     */
    public static RelDataTypeFactory createSqlTypeFactory() {
        return new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
    }
}
