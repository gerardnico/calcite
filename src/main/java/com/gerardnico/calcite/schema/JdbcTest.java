package com.gerardnico.calcite.schema;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.TranslatableTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Extract of org.apache.calcite.test.JdbcTest
 * only the schema
 */
public class JdbcTest {

    public static class FoodmartSchema {
        public final SalesFact[] sales_fact_1997 = {
                new SalesFact(100, 10),
                new SalesFact(150, 20),
        };
    }

    public static class SalesFact {
        public final int cust_id;
        public final int prod_id;

        public SalesFact(int cust_id, int prod_id) {
            this.cust_id = cust_id;
            this.prod_id = prod_id;
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this
                    || obj instanceof SalesFact
                    && cust_id == ((SalesFact) obj).cust_id
                    && prod_id == ((SalesFact) obj).prod_id;
        }
    }





    public static class LingualSchema {
        public final LingualEmp[] EMPS = {
                new LingualEmp(1, 10),
                new LingualEmp(2, 30)
        };
    }
    public static class LingualEmp {
        public final int EMPNO;
        public final int DEPTNO;

        public LingualEmp(int EMPNO, int DEPTNO) {
            this.EMPNO = EMPNO;
            this.DEPTNO = DEPTNO;
        }

        @Override public boolean equals(Object obj) {
            return obj == this
                    || obj instanceof LingualEmp
                    && EMPNO == ((LingualEmp) obj).EMPNO;
        }
    }

}
