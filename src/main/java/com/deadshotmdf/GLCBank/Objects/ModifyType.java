package com.deadshotmdf.GLCBank.Objects;

public enum ModifyType{

    ADD,
    REMOVE,
    SET;

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
