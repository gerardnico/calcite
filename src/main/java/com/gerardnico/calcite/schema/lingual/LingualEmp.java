package com.gerardnico.calcite.schema.lingual;

public class LingualEmp {

    public final int EMPNO;
    public final int DEPTNO;

    public LingualEmp(int EMPNO, int DEPTNO) {
        this.EMPNO = EMPNO;
        this.DEPTNO = DEPTNO;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this
                || obj instanceof LingualEmp
                && EMPNO == ((LingualEmp) obj).EMPNO;
    }

}
