package com.gerardnico.calcite;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;

public class CalciteSqlSelect {

    static public void printInfo(SqlNode sqlNode) {
        if (sqlNode instanceof SqlSelect) {
            SqlSelect select = (SqlSelect) sqlNode;

            System.out.println("Select list is: " + select.getSelectList());
            System.out.println("From clause is: " + select.getFrom());
            System.out.println("Where clause is: " + select.getWhere());
            System.out.println("Group clause is: " + select.getGroup());

        } else {
            throw new RuntimeException("This is not a select statement. The class of this SqlNode is " + sqlNode.getClass().toString());
        }

    }
}
