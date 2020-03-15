package com.gerardnico.calcite;

import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.junit.Ignore;
import org.junit.Test;

public class CalcitePlannerTest {

    @Test
    @Ignore
    public void basePlannerTest() {
        HepPlanner helpPlanner = CalcitePlanner.createPlanner();
        final String sql = "(select name from dept union select ename from emp)\n"
                + "intersect (select fname from customer.contact)";
        final RelRoot root = CalciteSql.fromSqlToRelNode(sql);
        //final RelNode relInitial = root.rel;
    }
}
