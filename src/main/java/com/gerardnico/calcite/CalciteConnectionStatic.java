package com.gerardnico.calcite;

import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.config.NullCollation;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.plan.Context;
import org.apache.calcite.plan.Contexts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Static method on {@link org.apache.calcite.jdbc.CalciteConnection}
 */
public class CalciteConnectionStatic {

    /**
     *
     * @param connection
     * @return a {@link CalciteConnection}
     */
    public static CalciteConnection unwrap(Connection connection) {
        try {
            return connection.unwrap(CalciteConnection.class);
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    /**
     * Return a connection without any model
     * @return
     */
    public static CalciteConnection getConnectionWithoutModel() {
        try {
            Class.forName("org.apache.calcite.jdbc.Driver");
            Properties info = new Properties();
            /**
             * The lex property gives a lot of default lexical property
             * {@link CalciteConnectionProperty.LEX}
             */
            info.setProperty("lex", "JAVA");
            /**
             * You can define your with extra connection properties
             */
            info.setProperty(CalciteConnectionProperty.CASE_SENSITIVE.name(),"false");
            return  unwrap(DriverManager.getConnection("jdbc:calcite:", info));
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param modelPath - the path de json model file
     * @return a connection configured with the model file
     */
    public static CalciteConnection getConnectionWithModel(String modelPath) {
        return CalciteModel.getConnectionFromModel(modelPath);
    }

    /**
     * @return a context from a connection :)
     */
    public static Context getContext() {
        Properties properties = new Properties();
        properties.setProperty(
                CalciteConnectionProperty.DEFAULT_NULL_COLLATION.camelName(),
                NullCollation.LOW.name()
        );
        CalciteConnectionConfigImpl connectionConfig = new CalciteConnectionConfigImpl(properties);
        return Contexts.of(connectionConfig);
    }
}
