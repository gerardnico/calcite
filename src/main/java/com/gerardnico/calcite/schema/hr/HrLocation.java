package com.gerardnico.calcite.schema.hr;


import java.io.Serializable;

public class HrLocation  implements Serializable {
    public final int x;
    public final int y;
    public final int locationId;
    public final String name;

    public HrLocation(int locationId, String name, int x, int y) {

        this.locationId = locationId;
        this.name = name;
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
