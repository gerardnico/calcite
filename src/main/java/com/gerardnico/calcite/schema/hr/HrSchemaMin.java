package com.gerardnico.calcite.schema.hr;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;

public class HrSchemaMin {

    public final HrEmployee[] emps = {
            new HrEmployee(1, 10, "Bill", 10000, 1000),
            new HrEmployee(2, 20, "Eric", 8000, 500),
            new HrEmployee(3, 10, "Sebastian", 7000, null),
            new HrEmployee(4, 10, "Theodore", 11500, 250),
            new HrEmployee(5, 10, "Marjorie", 10000, 1000),
            new HrEmployee(6, 20, "Guy", 8000, 500),
            new HrEmployee(7, 10, "Dieudonne", 7000, null),

    };
    public final HrDepartment[] depts = {
            new HrDepartment(10, "Sales", Arrays.asList(emps[0], emps[2]),null),
            new HrDepartment(20, "Marketing", ImmutableList.of(), null),
    };

}
