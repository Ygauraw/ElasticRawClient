package com.silverforge.elasticsearchrawclient.exceptions;

import com.silverforge.elasticsearchrawclient.utils.StringUtils;

public class MandatoryParametersAreMissingException extends Exception {
    private final String[] parameters;

    public MandatoryParametersAreMissingException(String... parameters) {
        this.parameters = parameters.clone();
    }

    @Override
    public String getMessage() {
        String list = StringUtils.makeCommaSeparatedList(parameters);
        return String.format("The following parameters are missing : %s", list);
    }
}
