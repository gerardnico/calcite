package com.gerardnico.calcite.schema.hr;


import java.io.Serializable;
import java.util.List;

public  class HrDepartment  implements Serializable {

    public final int deptno;
    public final String name;

    @org.apache.calcite.adapter.java.Array(component = HrEmployee.class)
    public final List<HrEmployee> hrEmployees;
    private  HrLocation location = new HrLocation(0,"default",1,2);


    public HrDepartment(int deptno, String name, List<HrEmployee> hrEmployees,
                        HrLocation location) {
        this.deptno = deptno;
        this.name = name;
        this.hrEmployees = hrEmployees;
        this.location = location;
    }

    public HrDepartment(int deptno, String name, List<HrEmployee> hrEmployees) {
        this.deptno = deptno;
        this.name = name;
        this.hrEmployees = hrEmployees;
    }


    @Override
    public String toString() {
        return "Department [deptno: " + deptno + ", name: " + name
                + ", employees: " + hrEmployees + ", location: " + location + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this
                || obj instanceof HrDepartment
                && deptno == ((HrDepartment) obj).deptno;
    }
}