package com.silverforge.elasticsearchrawclient.elasticFacade.operations;

import android.content.Context;
import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;
import com.silverforge.elasticsearchrawclient.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;
import com.silverforge.webconnector.definitions.Connectable;

public abstract class Operations {
    protected Context context;
    protected Connectable connector;
    protected ElasticSettings elasticSettings;

    public Operations(Connectable connector, ElasticSettings elasticSettings) {
        this.connector = connector;
        this.elasticSettings = elasticSettings;
        context = ElasticClientApp.getAppContext();
    }

    /**
     * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
     * @param operationType the type of the operation
     * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
     * @throws IndexCannotBeNullException
     * @throws TypeCannotBeNullException
     * @see com.silverforge.elasticsearchrawclient.elasticFacade.OperationType
     */
    protected String getOperationPath(OperationType operationType)
            throws IndexCannotBeNullException, TypeCannotBeNullException {
        return getOperationPath(null, operationType);
    }

    /**
     * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
     * @param id the id of the document. could be <strong>null</strong>
     * @param operationType the type of the operation
     * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
     * @throws IndexCannotBeNullException
     * @see com.silverforge.elasticsearchrawclient.elasticFacade.OperationType
     */
    protected String getOperationPath(String id, OperationType operationType)
            throws IndexCannotBeNullException, TypeCannotBeNullException {

        return getOperationPath(elasticSettings.getIndices(), elasticSettings.getTypes(), id, operationType);
    }

    /**
     * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
     * @param index the index name (Elastic)
     * @param type the type name (Elastic)
     * @param operationType the type of the operation
     * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
     * @throws IndexCannotBeNullException
     * @throws TypeCannotBeNullException
     */
    protected String getOperationPath(String index, String type, OperationType operationType)
            throws IndexCannotBeNullException, TypeCannotBeNullException {

        String[] indexParam = null;
        String[] typeParam = null;

        if (!TextUtils.isEmpty(index))
            indexParam = new String[] {index};

        if (!TextUtils.isEmpty(type))
            typeParam = new String[] {type};

        return getOperationPath(indexParam, typeParam, null, operationType);
    }

    /**
     * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
     * @param index the index name (Elastic)
     * @param type the type name (Elastic)
     * @param id the id of the document (Elastic)
     * @param operationType the type of the operation
     * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
     * @throws IndexCannotBeNullException
     * @throws TypeCannotBeNullException
     */
    protected String getOperationPath(String index, String type, String id,OperationType operationType)
            throws IndexCannotBeNullException, TypeCannotBeNullException {

        String[] indexParam = null;
        String[] typeParam = null;

        if (!TextUtils.isEmpty(index))
            indexParam = new String[] {index};

        if (!TextUtils.isEmpty(type))
            typeParam = new String[] {type};

        return getOperationPath(indexParam, typeParam, id, operationType);
    }

    /**
     * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
     * @param indices list of indices (Elastic)
     * @param types list of types (Elastic)
     * @param operationType the type of the operation
     * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
     * @throws IndexCannotBeNullException
     * @throws TypeCannotBeNullException
     */
    protected String getOperationPath(String[] indices, String[] types, OperationType operationType)
            throws IndexCannotBeNullException, TypeCannotBeNullException {

        return getOperationPath(indices, types, null, operationType);
    }

    /**
     * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
     * @param indices list of indices (Elastic)
     * @param types list of types (Elastic)
     * @param id the id of the document (Elastic)
     * @param operationType the type of the operation
     * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
     * @throws IndexCannotBeNullException
     * @throws TypeCannotBeNullException
     */
    protected String getOperationPath(String[] indices, String[] types, String id, OperationType operationType)
            throws IndexCannotBeNullException, TypeCannotBeNullException {

        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append("/");
        switch (operationType) {
            case CREATE:
            case DELETE:
            case UPDATE:

                if (indices == null || indices.length == 0)
                    throw new IndexCannotBeNullException();

                String index = indices[0];
                pathBuilder.append(index).append("/");

                if (types == null || types.length == 0)
                    throw new TypeCannotBeNullException();

                String type = types[0];
                pathBuilder.append(type);

                if (!TextUtils.isEmpty(id))	{
                    pathBuilder.append("/").append(id);

                    String operationPath = operationType.getOperationTypePath();
                    if (!TextUtils.isEmpty(operationPath))
                        pathBuilder.append("/").append(operationPath);
                }

                break;
            case QUERY:
            case SEARCH:
                if (indices == null || indices.length == 0)
                    pathBuilder.append("_all");
                else
                    pathBuilder.append(StringUtils.makeCommaSeparatedList(indices));

                if (types != null && types.length > 0)
                    pathBuilder.append("/").append(StringUtils.makeCommaSeparatedList(types));

                String operationPath = operationType.getOperationTypePath();
                if (!TextUtils.isEmpty(operationPath))
                    pathBuilder.append("/").append(operationPath);

                break;
        }

        return pathBuilder.toString();
    }
}
