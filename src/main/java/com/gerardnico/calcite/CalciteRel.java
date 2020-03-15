package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.JdbcStore;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelRunners;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalciteRel {

    /**
     * @return a {@link RelBuilder} with a schema mapped to the SCOTT database, with tables EMP and DEPT.
     */
    public static RelBuilder getScott() {
        final FrameworkConfig config = CalciteFramework.configScottBased();
        return RelBuilder.create(config);

    }

    public static void print(RelNode relNode) {
        System.out.println(toString(relNode));
    }

    public static String toString(RelNode relNode) {
        return RelOptUtil.toString(relNode);
    }

    /**
     * Execute a relNode and return the result set
     *
     * @param relNode
     * @return
     */
    public static ResultSet executeQuery(RelNode relNode) {
        PreparedStatement run = RelRunners.run(relNode);
        try {
            return run.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@link #executeQuery(RelNode) Execute} and print the result set in one pass
     * @param relNode
     */
    public static void executeQueryAndPrint(RelNode relNode) {
        try (ResultSet resultSet = executeQuery(relNode)) {
            JdbcStore.print(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
