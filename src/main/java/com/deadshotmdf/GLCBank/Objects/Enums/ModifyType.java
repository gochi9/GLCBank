package com.deadshotmdf.GLCBank.Objects.Enums;

public enum ModifyType{

    ADD("DEPOSIT"),
    REMOVE("WITHDRAW"),
    SET("SET");

    private final String alias;

    ModifyType(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public static ModifyType getModifyType(String s){
        if(s == null)
            return null;

        return switch (s.toUpperCase()) {
            case "DEPOSIT", "ADD" -> ADD;
            case "REMOVE", "WITHDRAW" -> REMOVE;
            case "SET" -> SET;
            default -> null;
        };
    }

}
