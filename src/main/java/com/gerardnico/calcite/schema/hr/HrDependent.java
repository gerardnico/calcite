package com.gerardnico.calcite.schema.hr;

import java.io.Serializable;
import java.util.Objects;

public class HrDependent   implements Serializable  {
    public final int empid;
    public final String name;

    public HrDependent(int empid, String name) {
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
                || obj instanceof HrDependent
                && empid == ((HrDependent) obj).empid
                && Objects.equals(name, ((HrDependent) obj).name);
    }
}
