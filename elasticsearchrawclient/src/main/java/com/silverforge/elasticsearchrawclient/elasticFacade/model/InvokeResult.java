package com.silverforge.elasticsearchrawclient.elasticFacade.model;

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
    @Setter
    private String result;

    @Getter
    private List<Exception> aggregatedExceptions = new ArrayList<>();

    public InvokeResult() {
    }

    public void addExceptionToResult(Exception ex) {
        aggregatedExceptions.add(ex);
    }
}
