package com.silverforge.elasticsearchrawclient.exceptions;

public class MandatoryParametersAreMissingException extends Exception {
    private final String[] parameters;

    public MandatoryParametersAreMissingException(String... parameters) {
        this.parameters = parameters.clone();
    }

    @Override
    public String getMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("The following parameters are missing : ");

        for (String param : parameters) {
            messageBuilder.append(param).append(" ");
        }

        return messageBuilder.toString();
    }
}
