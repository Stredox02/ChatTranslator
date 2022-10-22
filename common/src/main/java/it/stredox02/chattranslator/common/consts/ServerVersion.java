package it.stredox02.chattranslator.common.consts;

import org.jetbrains.annotations.NotNull;

public enum ServerVersion {

    v1_8_R3,
    v1_19_R1;

    public static ServerVersion parse(@NotNull String string){
        for (ServerVersion value : values()) {
            if(value.name().equalsIgnoreCase(string)){
                return value;
            }
        }
        return null;
    }

}
