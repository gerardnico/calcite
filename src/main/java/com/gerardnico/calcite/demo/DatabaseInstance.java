package com.gerardnico.calcite.demo;

import org.apache.calcite.config.CalciteSystemProperty;


/**
 * Information necessary to create a JDBC connection. Specify one to run
 * tests against a different database. (hsqldb is the default.)
 */
public enum DatabaseInstance {
    HSQLDB(
            new DatabaseConnectionSpec("jdbc:hsqldb:res:foodmart", "FOODMART", "FOODMART",
                    "org.hsqldb.jdbcDriver", "foodmart"),
            new DatabaseConnectionSpec("jdbc:hsqldb:res:scott", "SCOTT",
                    "TIGER", "org.hsqldb.jdbcDriver", "SCOTT")),
    H2(
            new DatabaseConnectionSpec("jdbc:h2:" + CalciteSystemProperty.TEST_DATASET_PATH.value()
                    + "/h2/target/foodmart;user=foodmart;password=foodmart",
                    "foodmart", "foodmart", "org.h2.Driver", "foodmart"), null),
    MYSQL(
            new DatabaseConnectionSpec("jdbc:mysql://localhost/foodmart", "foodmart",
                    "foodmart", "com.mysql.jdbc.Driver", "foodmart"), null),
    ORACLE(
            new DatabaseConnectionSpec("jdbc:oracle:thin:@localhost:1521:XE", "foodmart",
                    "foodmart", "oracle.jdbc.OracleDriver", "FOODMART"), null),
    POSTGRESQL(
            new DatabaseConnectionSpec(
                    "jdbc:postgresql://localhost/foodmart?user=foodmart&password=foodmart&searchpath=foodmart",
                    "foodmart", "foodmart", "org.postgresql.Driver", "foodmart"), null);

    public final DatabaseConnectionSpec foodmart;
    public final DatabaseConnectionSpec scott;

    DatabaseInstance(DatabaseConnectionSpec foodmart, DatabaseConnectionSpec scott) {
        this.foodmart = foodmart;
        this.scott = scott;
    }
}


