package it.stredox02.chattranslator.common.config;

import org.jetbrains.annotations.NotNull;

public interface ConfigManager {

    int getInt(@NotNull String fileName, @NotNull String path);

    String getString(@NotNull String fileName, @NotNull String path);

    boolean getBoolean(@NotNull String fileName, @NotNull String path);

}
