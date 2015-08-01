package com.silverforge.elasticsearchrawclient.elasticFacade;

import java.util.HashMap;

public enum OperationType {

    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    INDEX("INDEX");

    private static final HashMap<String, String> operationPathType = new HashMap<>();
    static {
        operationPathType.put("CREATE", "_create");
        operationPathType.put("UPDATE", "_update");
        operationPathType.put("DELETE", "");
        operationPathType.put("INDEX", "");
    }

    private String operationType;

    OperationType(String operationType) {

        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return operationType;
    }

    public String getOperationTypePath() {
        return operationPathType.get(operationType);
    }
}
