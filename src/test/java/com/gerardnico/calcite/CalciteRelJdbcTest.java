package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.JdbcStore;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.tools.RelBuilder;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalciteRelJdbcTest {

    @Test
    public void relToSqlTest() {

        // Create a data store wrapper object
        JdbcStore jdbcStore = JdbcStore.createDefault();

        // Build the schema
        try {
            Connection connection = jdbcStore.getConnection();
            connection.createStatement().execute("CREATE TABLE TEST (TEST_VALUE INT)");
            String insertStatement = "INSERT INTO TEST (TEST_VALUE) VALUES(?)";
            for (int i = 0; i < 20; i++) {
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
                preparedStatement.setInt(1, i);
                preparedStatement.execute();
            }
            System.out.println("Records inserted");
            System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Get the builder
        RelBuilder builder = jdbcStore.getRelBuilder();

        // Build a relational expression and execute
        RelNode relNode = builder.scan("TEST")
                .project(builder.field("TEST_VALUE"))
                .build();
        String sql = CalciteRel.fromRelNodeToSql(relNode,jdbcStore.getDialect());
        System.out.println(sql);
        System.out.println("Execute the relational expression gives a pointer exception");
        // Null pointer exception: CalciteRel.executeAndPrint(relNode);

        // Project Fetch
        int offset = 0;
        int fetch = 2;
        relNode = builder.scan("TEST")
                .project(builder.field("TEST_VALUE"))
                .sortLimit(offset, fetch, builder.field("TEST_VALUE"))
                .build();
        sql = CalciteRel.fromRelNodeToSql(relNode,jdbcStore.getDialect());
        System.out.println(sql);
        System.out.println("Execute the SQL");
        jdbcStore.executeAndPrintQuery(sql);


    }
}
