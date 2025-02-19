package com.xiehn.common.constants;

public enum JfTimeUnitEnums {
    Y("Y"),
    M("M"),
    D("D"),
    H("H"),
    MINUTE("MINUTE");

    private String unit;

    private JfTimeUnitEnums(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }

    public static JfTimeUnitEnums getByValue(String value) {
        JfTimeUnitEnums[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            JfTimeUnitEnums stateEnum = var1[var3];
            if (stateEnum.unit.equals(value)) {
                return stateEnum;
            }
        }

        return null;
    }
}
