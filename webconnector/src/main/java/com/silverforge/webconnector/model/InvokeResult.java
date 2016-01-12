package com.silverforge.webconnector.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class InvokeResult {

    @Getter
    @Setter
    private boolean isSuccess;

    @Getter
    @Setter
    private String statusCode;

    @Getter
    private List<Exception> aggregatedExceptions = new ArrayList<>();

    public void addExceptionToResult(Exception ex) {
        aggregatedExceptions.add(ex);
    }
}
