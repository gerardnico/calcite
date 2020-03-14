package com.gerardnico.calcite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSets {
    public static void print(ResultSet resultSet) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    stringBuilder
                            .append(resultSet.getObject(i))
                            .append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
