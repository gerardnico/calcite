package com.gerardnico.calcite.schema.hr;


public class HrLocation {
    public final int x;
    public final int y;

    public HrLocation(int x, int y) {
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
                || obj instanceof HrLocation
                && x == ((HrLocation) obj).x
                && y == ((HrLocation) obj).y;
    }
}
