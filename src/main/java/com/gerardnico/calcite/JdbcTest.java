package com.gerardnico.calcite;

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


    /**
     * A schema that contains two tables by reflection.
     *
     * <p>Here is the SQL to create equivalent tables in Oracle:
     *
     * <blockquote>
     * <pre>
     * CREATE TABLE "emps" (
     *   "empid" INTEGER NOT NULL,
     *   "deptno" INTEGER NOT NULL,
     *   "name" VARCHAR2(10) NOT NULL,
     *   "salary" NUMBER(6, 2) NOT NULL,
     *   "commission" INTEGER);
     * INSERT INTO "emps" VALUES (100, 10, 'Bill', 10000, 1000);
     * INSERT INTO "emps" VALUES (200, 20, 'Eric', 8000, 500);
     * INSERT INTO "emps" VALUES (150, 10, 'Sebastian', 7000, null);
     * INSERT INTO "emps" VALUES (110, 10, 'Theodore', 11500, 250);
     *
     * CREATE TABLE "depts" (
     *   "deptno" INTEGER NOT NULL,
     *   "name" VARCHAR2(10) NOT NULL,
     *   "employees" ARRAY OF "Employee",
     *   "location" "Location");
     * INSERT INTO "depts" VALUES (10, 'Sales', null, (-122, 38));
     * INSERT INTO "depts" VALUES (30, 'Marketing', null, (0, 52));
     * INSERT INTO "depts" VALUES (40, 'HR', null, null);
     * </pre>
     * </blockquote>
     */
    public static class HrSchema {
        @Override
        public String toString() {
            return "HrSchema";
        }

        public final Employee[] emps = {
                new Employee(100, 10, "Bill", 10000, 1000),
                new Employee(200, 20, "Eric", 8000, 500),
                new Employee(150, 10, "Sebastian", 7000, null),
                new Employee(110, 10, "Theodore", 11500, 250),
        };
        public final Department[] depts = {
                new Department(10, "Sales", Arrays.asList(emps[0], emps[2]),
                        new Location(-122, 38)),
                new Department(30, "Marketing", ImmutableList.of(), new Location(0, 52)),
                new Department(40, "HR", Collections.singletonList(emps[1]), null),
        };
        public final Dependent[] dependents = {
                new Dependent(10, "Michael"),
                new Dependent(10, "Jane"),
        };
        public final Dependent[] locations = {
                new Dependent(10, "San Francisco"),
                new Dependent(20, "San Diego"),
        };

        public QueryableTable foo(int count) {
            return Smalls.generateStrings(count);
        }

        public TranslatableTable view(String s) {
            return Smalls.view(s);
        }
    }

    public static class Employee {
        public final int empid;
        public final int deptno;
        public final String name;
        public final float salary;
        public final Integer commission;

        public Employee(int empid, int deptno, String name, float salary,
                        Integer commission) {
            this.empid = empid;
            this.deptno = deptno;
            this.name = name;
            this.salary = salary;
            this.commission = commission;
        }

        @Override
        public String toString() {
            return "Employee [empid: " + empid + ", deptno: " + deptno
                    + ", name: " + name + "]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this
                    || obj instanceof Employee
                    && empid == ((Employee) obj).empid;
        }
    }

    public static class Dependent {
        public final int empid;
        public final String name;

        public Dependent(int empid, String name) {
            this.empid = empid;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Dependent [empid: " + empid + ", name: " + name + "]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this
                    || obj instanceof Dependent
                    && empid == ((Dependent) obj).empid
                    && Objects.equals(name, ((Dependent) obj).name);
        }
    }

    public static class Department {
        public final int deptno;
        public final String name;

        @org.apache.calcite.adapter.java.Array(component = Employee.class)
        public final List<Employee> employees;
        public final Location location;

        public Department(int deptno, String name, List<Employee> employees,
                          Location location) {
            this.deptno = deptno;
            this.name = name;
            this.employees = employees;
            this.location = location;
        }

        @Override
        public String toString() {
            return "Department [deptno: " + deptno + ", name: " + name
                    + ", employees: " + employees + ", location: " + location + "]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this
                    || obj instanceof Department
                    && deptno == ((Department) obj).deptno;
        }
    }

    public static class Location {
        public final int x;
        public final int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Location [x: " + x + ", y: " + y + "]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this
                    || obj instanceof Location
                    && x == ((Location) obj).x
                    && y == ((Location) obj).y;
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
