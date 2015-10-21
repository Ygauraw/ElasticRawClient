package com.silverforge.elasticsearchrawclient.elasticFacade.operations;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.AddDocumentResult;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.webconnector.definitions.Connectable;
import com.silverforge.webconnector.model.InvokeStringResult;

public class DocumentOperations extends Operations {
    public DocumentOperations(Connectable connector, ElasticSettings elasticSettings) {
        super(connector, elasticSettings);
    }

    public <T> String addDocument(String id, T entity)
            throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException {

        if (entity == null)
            throw new IllegalArgumentException("entity cannot be null");

        String entityJson = ElasticClientMapper.mapToJson(entity);
        String addPath = getOperationPath(id, OperationType.CREATE);

        InvokeStringResult result = connector.post(addPath, entityJson);
        AddDocumentResult addDocumentResult
                = ElasticClientMapper.mapToEntity(result.getResult(), AddDocumentResult.class);

        return addDocumentResult.getId();
    }

    public <T> String addDocument(String index, String type, String id, T entity)
            throws IllegalArgumentException {

        if (entity == null)
            throw new IllegalArgumentException("entity cannot be null");

        if (TextUtils.isEmpty(index))
            throw new IllegalArgumentException("index cannot be null or empty");

        if (TextUtils.isEmpty(type))
            throw new IllegalArgumentException("type cannot be null or empty");

        if (TextUtils.isEmpty(id))
            throw new IllegalArgumentException("id cannot be null or empty");

        String retValue = "";
        try {
            String entityJson = ElasticClientMapper.mapToJson(entity);
            String addPath = getOperationPath(index, type, id, OperationType.CREATE);

            InvokeStringResult result = connector.post(addPath, entityJson);

            AddDocumentResult addDocumentResult
                    = ElasticClientMapper.mapToEntity(result.getResult(),
                    AddDocumentResult.class);

            retValue = addDocumentResult.getId();

        } catch	(IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }

        return retValue;
    }

    public void removeDocument(String id)
            throws IllegalArgumentException, IndexCannotBeNullException, TypeCannotBeNullException {

        if (TextUtils.isEmpty(id))
            throw new IllegalArgumentException("id cannot be null or empty");

        String deletePath = getOperationPath(id, OperationType.DELETE);
        connector.delete(deletePath);
    }

    public void removeDocument(String index, String type, String id)
            throws IllegalArgumentException {

        if (TextUtils.isEmpty(index))
            throw new IllegalArgumentException("index cannot be null or empty");

        if (TextUtils.isEmpty(type))
            throw new IllegalArgumentException("type cannot be null or empty");

        if (TextUtils.isEmpty(id))
            throw new IllegalArgumentException("id cannot be null or empty");

        try {
            String deletePath = getOperationPath(index, type, id, OperationType.DELETE);
            connector.delete(deletePath);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }
    }

    public void removeDocumentsQuery(String query) {

        try {
            String deletePath = getOperationPath(OperationType.QUERY);
            connector.delete(deletePath, query);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }
    }

    public void removeDocumentsQuery(String[] indices, String[] types, String query) {

        try {
            String deletePath = getOperationPath(indices, types, OperationType.QUERY);
            connector.delete(deletePath, query);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }
    }

    public <T> void updateDocument(String id, T entity)
            throws IndexCannotBeNullException, TypeCannotBeNullException {

        if (entity == null)
            throw new IllegalArgumentException("entity cannot be null");

        String entityJson = ElasticClientMapper.mapToJson(entity);
        String updatePath = getOperationPath(id, OperationType.UPDATE);

        String updateTemplate
                = StreamUtils.getRawContent(context, R.raw.update_template);
        String data = updateTemplate.replace("{{ENTITYJSON}}", entityJson);

        connector.post(updatePath, data);
    }

    public <T> void updateDocument(String index, String type, String id, T entity)
            throws IllegalArgumentException {

        if (entity == null)
            throw new IllegalArgumentException("entity cannot be null");

        if (TextUtils.isEmpty(index))
            throw new IllegalArgumentException("index cannot be null or empty");

        if (TextUtils.isEmpty(type))
            throw new IllegalArgumentException("type cannot be null or empty");

        if (TextUtils.isEmpty(id))
            throw new IllegalArgumentException("id cannot be null or empty");

        try {
            String entityJson = ElasticClientMapper.mapToJson(entity);
            String updatePath = getOperationPath(index, type, id, OperationType.UPDATE);

            String updateTemplate
                    = StreamUtils.getRawContent(context, R.raw.update_template);
            String data = updateTemplate.replace("{{ENTITYJSON}}", entityJson);

            connector.post(updatePath, data);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }
    }
}
