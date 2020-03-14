package com.gerardnico.calcite.hr;

import com.gerardnico.calcite.Smalls;
import com.google.common.collect.ImmutableList;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.TranslatableTable;

import java.util.Arrays;
import java.util.Collections;

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
            new HrEmployee(100, 10, "Bill", 10000, 1000),
            new HrEmployee(200, 20, "Eric", 8000, 500),
            new HrEmployee(150, 10, "Sebastian", 7000, null),
            new HrEmployee(110, 10, "Theodore", 11500, 250),
    };
    public final HrDepartment[] depts = {
            new HrDepartment(10, "Sales", Arrays.asList(emps[0], emps[2]),
                    new Location(-122, 38)),
            new HrDepartment(30, "Marketing", ImmutableList.of(), new Location(0, 52)),
            new HrDepartment(40, "HR", Collections.singletonList(emps[1]), null),
    };
    public final HrDependent[] hrDependents = {
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

}
