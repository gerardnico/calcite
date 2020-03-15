package com.gerardnico.calcite;

import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.RelBuilder;

public class CalciteRel {

    /**
     *
     * @return a {@link RelBuilder} with a schema mapped to the SCOTT database, with tables EMP and DEPT.
     */
    public static RelBuilder getScott() {
        final FrameworkConfig config = CalciteFramework.configScottBased();
        return RelBuilder.create(config);

    }

}
