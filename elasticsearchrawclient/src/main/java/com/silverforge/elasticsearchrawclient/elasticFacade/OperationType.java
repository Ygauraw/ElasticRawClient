package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.HashMap;

/**
 * Types of the ElasticSearch operations
 */
public enum OperationType {

    NONE("NONE") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {
            throw new UnsupportedOperationException();
        }
    },

    CREATE("CREATE") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {
            if (!TextUtils.isEmpty(indexName)
                    && !TextUtils.isEmpty(typeName)
                    && !TextUtils.isEmpty(documentId)
                    && !TextUtils.isEmpty(bulkItemEntityJson)) {

                String createActionJson
                    = template
                        .replace("{{INDEX}}", indexName)
                        .replace("{{TYPE}}", typeName)
                        .replace("{{ID}}", documentId);

                bodyBuilder
                    .append(createActionJson)
                    .append(bulkItemEntityJson)
                    .append(StringUtils.LINE_SEPARATOR);
            }
        }
    },

    UPDATE("UPDATE") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {

            if (!TextUtils.isEmpty(indexName)
                    && !TextUtils.isEmpty(typeName)
                    && !TextUtils.isEmpty(documentId)
                    && !TextUtils.isEmpty(bulkItemEntityJson)) {

                String updateActionJson
                    = template
                        .replace("{{INDEX}}", indexName)
                        .replace("{{TYPE}}", typeName)
                        .replace("{{ID}}", documentId);

                bodyBuilder
                    .append(updateActionJson)
                    .append("{\"doc\":")
                    .append(bulkItemEntityJson)
                    .append("}")
                    .append(StringUtils.LINE_SEPARATOR);
            }
        }
    },

    DELETE("DELETE") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {

            if (!TextUtils.isEmpty(indexName)
                    && !TextUtils.isEmpty(typeName)
                    && !TextUtils.isEmpty(documentId)) {

                String deleteActionJson
                    = template
                        .replace("{{INDEX}}", indexName)
                        .replace("{{TYPE}}", typeName)
                        .replace("{{ID}}", documentId);

                bodyBuilder.append(deleteActionJson);
            }
        }
    },

    INDEX("INDEX") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {

            if (!TextUtils.isEmpty(indexName)
                    && !TextUtils.isEmpty(typeName)
                    && !TextUtils.isEmpty(bulkItemEntityJson)) {

                String indexActionJson
                    = template
                        .replace("{{INDEX}}", indexName)
                        .replace("{{TYPE}}", typeName);

                bodyBuilder
                    .append(indexActionJson)
                    .append(bulkItemEntityJson)
                    .append(StringUtils.LINE_SEPARATOR);
            }
        }
    },

    QUERY("QUERY") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {

            throw new UnsupportedOperationException();
        }
    },

    SEARCH("SEARCH") {
        public void prepareTemplateForAction(String template,
                                             StringBuilder bodyBuilder,
                                             String indexName,
                                             String typeName,
                                             String documentId,
                                             String bulkItemEntityJson) {

            throw new UnsupportedOperationException();
        }
    };

    private static final HashMap<String, String> operationPathType = new HashMap<>();
    static {
        operationPathType.put("CREATE", "_create");
        operationPathType.put("UPDATE", "_update");
        operationPathType.put("DELETE", "");
        operationPathType.put("INDEX", "");
        operationPathType.put("QUERY", "_query");
        operationPathType.put("SEARCH", "_search");
    }

    private String operationType;

    OperationType(String operationType) {

        this.operationType = operationType;
    }

    /**
     * Override of the toString
     * @return the operationtype, e.g.: "CREATE", "UPDATE", "DELETE", "INDEX", "QUERY", "SEARCH"
     */
    @Override
    public String toString() {
        return operationType;
    }

    /**
     * Retrieves with the operation path
     * @return the operation type path, e.g.: "_create", "_update", "_query", "_search"
     */
    public String getOperationTypePath() {
        return operationPathType.get(operationType);
    }

    /**
     * Retrieves the Operation type from "create", "update", "delete", "index"
     * @param bulktype the type of the result of bulk response
     * @return operationType
     */
    public static OperationType getOperationType(String bulktype) {
        switch (bulktype.toLowerCase()) {
            case "create":
                return CREATE;
            case "update":
                return UPDATE;
            case "delete":
                return DELETE;
            case "index":
                return INDEX;
            default:
                return NONE;
        }
    }

    public abstract void prepareTemplateForAction(String template,
                                                  StringBuilder bodyBuilder,
                                                  String indexName,
                                                  String typeName,
                                                  String documentId,
                                                  String bulkItemEntityJson);
}
