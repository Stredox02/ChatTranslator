package it.stredox02.chattranslator.common.consts;

import org.jetbrains.annotations.Nullable;

public enum StorageType {

    MYSQL;

    public static StorageType parse(@Nullable String string) {
        for (StorageType value : values()) {
            if(value.name().equalsIgnoreCase(string)){
                return value;
            }
        }
        return null;
    }

}
