package com.gerardnico.calcite;

import com.gerardnico.calcite.demo.HelloWorld;
import org.junit.Test;

import java.sql.SQLException;

public class HelloWorldTest {

    @Test
    public void helloWorldTest() throws SQLException, ClassNotFoundException {
        String[] args = {};
        HelloWorld.main(args);
    }
}
