package it.stredox02.chattranslator.bukkit.exceptions;

public class ConfigReloadErrorException extends Exception{

    public ConfigReloadErrorException() {
        super();
    }

    public ConfigReloadErrorException(String message) {
        super(message);
    }

    public ConfigReloadErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigReloadErrorException(Throwable cause) {
        super(cause);
    }

    protected ConfigReloadErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
