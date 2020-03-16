package com.gerardnico.calcite;


import org.apache.calcite.sql.*;
import org.apache.calcite.sql.util.SqlBasicVisitor;

public class CalciteSqlVisitor extends SqlBasicVisitor<Void> {

    @Override
    public Void visit(SqlLiteral literal) {
        System.out.println("Literal: " + literal);
        System.out.println("-------------------------------------");
        return super.visit(literal);
    }

    @Override
    public Void visit(SqlCall call) {
        System.out.println("SqlCall: " + call);
        System.out.println("-------------------------------------");
        return super.visit(call);
    }

    @Override
    public Void visit(SqlIdentifier id) {
        System.out.println("Identifier: " + id);
        System.out.println("-------------------------------------");
        return super.visit(id);
    }

    @Override
    public Void visit(SqlNodeList nodeList) {
        if (nodeList.size()!=0) {
            System.out.println("NodeList: " + nodeList);
            System.out.println("-------------------------------------");
        }
        return super.visit(nodeList);
    }

    @Override
    public Void visit(SqlDataTypeSpec type) {
        System.out.println("DataType: " + type);
        System.out.println("-------------------------------------");
        return super.visit(type);
    }
}

