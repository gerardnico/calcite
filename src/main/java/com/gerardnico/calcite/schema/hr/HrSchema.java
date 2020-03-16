package com.gerardnico.calcite.schema.hr;

import com.gerardnico.calcite.mock.Smalls;
import com.google.common.collect.ImmutableList;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.TranslatableTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;

/**
 * Object that will be used via reflection to create the "hr" schema.
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

public class HrSchema {

    @Override
    public String toString() {
        return "HrSchema";
    }

    public final HrEmployee[] emps = {
            new HrEmployee(1, 10, "Bill", 10000, 1000),
            new HrEmployee(2, 20, "Eric", 8000, 500),
            new HrEmployee(3, 10, "Sebastian", 7000, null),
            new HrEmployee(4, 10, "Theodore", 11500, 250),
            new HrEmployee(5, 10, "Marjorie", 10000, 1000),
            new HrEmployee(6, 20, "Guy", 8000, 500),
            new HrEmployee(7, 10, "Dieudonne", 7000, null),
            new HrEmployee(8, 10, "Haroun", 11500, 250),
            new HrEmployee(9, 10, "Sarah", 10000, 1000),
            new HrEmployee(10, 20, "Gabriel", 8000, 500),
            new HrEmployee(11, 10, "Pierre", 7000, null),
            new HrEmployee(12, 10, "Paul", 11500, 250),
            new HrEmployee(13, 10, "Jacques", 100, 1000),
            new HrEmployee(14, 20, "Khawla", 8000, 500),
            new HrEmployee(15, 10, "Brielle", 7000, null),
            new HrEmployee(16, 10, "Hyuna", 11500, 250),
            new HrEmployee(17, 10, "Ahmed", 10000, 1000),
            new HrEmployee(18, 20, "Lara", 8000, 500),
            new HrEmployee(19, 10, "Capucine", 7000, null),
            new HrEmployee(20, 10, "Michelle", 11500, 250),
            new HrEmployee(21, 10, "Cerise", 10000, 1000),
            new HrEmployee(22, 80, "Travis", 8000, 500),
            new HrEmployee(23, 10, "Taylor", 7000, null),
            new HrEmployee(24, 10, "Seohyun", 11500, 250),
            new HrEmployee(25, 70, "Helen", 10000, 1000),
            new HrEmployee(26, 50, "Patric", 8000, 500),
            new HrEmployee(27, 10, "Clara", 7000, null),
            new HrEmployee(28, 10, "Catherine", 11500, 250),
            new HrEmployee(29, 10, "Anibal", 10000, 1000),
            new HrEmployee(30, 30, "Ursula", 8000, 500),
            new HrEmployee(31, 10, "Arturito", 7000, null),
            new HrEmployee(32, 70, "Diane", 11500, 250),
            new HrEmployee(33, 10, "Phoebe", 10000, 1000),
            new HrEmployee(34, 20, "Maria", 8000, 500),
            new HrEmployee(35, 10, "Edouard", 7000, null),
            new HrEmployee(36, 110, "Isabelle", 11500, 250),
            new HrEmployee(37, 120, "Olivier", 10000, 1000),
            new HrEmployee(38, 20, "Yann", 8000, 500),
            new HrEmployee(39, 60, "Ralf", 7000, null),
            new HrEmployee(40, 60, "Emmanuel", 11500, 250),
            new HrEmployee(41, 10, "Berenice", 10000, 1000),
            new HrEmployee(42, 20, "Kylie", 8000, 500),
            new HrEmployee(43, 80, "Natacha", 7000, null),
            new HrEmployee(44, 100, "Henri", 11500, 250),
            new HrEmployee(45, 90, "Pascal", 10000, 1000),
            new HrEmployee(46, 90, "Sabrina", 8000, 500),
            new HrEmployee(47, 8, "Riyad", 7000, null),
            new HrEmployee(48, 5, "Andy", 11500, 250),
            new HrEmployee(100, 10, "Bill", 10000, 1000),
            new HrEmployee(200, 20, "Eric", 8000, 500),
            new HrEmployee(150, 10, "Sebastian", 7000, null),
            new HrEmployee(110, 10, "Theodore", 11500, 250),
    };
    public final HrDepartment[] depts = {
            new HrDepartment(10, "Sales", Arrays.asList(emps[0], emps[2]),null),
            new HrDepartment(20, "Marketing", ImmutableList.of(), null),
            new HrDepartment(30, "HR", Collections.singletonList(emps[1]), null),
            new HrDepartment(40, "Administration", Arrays.asList(emps[0], emps[2]),
                    null),
            new HrDepartment(50, "Design", ImmutableList.of(), null),
            new HrDepartment(60, "IT", Collections.singletonList(emps[1]), null),
            new HrDepartment(70, "Production", Arrays.asList(emps[0], emps[2]),
                    null),
            new HrDepartment(80, "Finance", ImmutableList.of(), null),
            new HrDepartment(90, "Accounting", Collections.singletonList(emps[1]), null),
            new HrDepartment(100, "Research", Arrays.asList(emps[0], emps[2]),
                    null),
            new HrDepartment(110, "Maintenance", ImmutableList.of(), null),
            new HrDepartment(120, "Client Support", Collections.singletonList(emps[1]), null),
    };

    public final HrDependent[] dependents = {
            new HrDependent(10, "Michael"),
            new HrDependent(10, "Jane"),
    };

    public final HrDependent[] locations = {
            new HrDependent(10, "San Francisco"),
            new HrDependent(20, "San Diego"),
    };

    public QueryableTable foo(int count) {
        return Smalls.generateStrings(count);
    }

    public TranslatableTable view(String s) {
        return Smalls.view(s);
    }

    public static HrSchema createHrSchema() {
        return new HrSchema();
    }
}
