package com.silverforge.webconnector.exceptions;

public class SettingsIsNullException extends Exception {
    @Override
    public String getMessage() {
        return "Settings cannot be null!";
    }
}
