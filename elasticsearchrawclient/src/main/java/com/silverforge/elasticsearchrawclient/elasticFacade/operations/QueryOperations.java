package com.silverforge.elasticsearchrawclient.elasticFacade.operations;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.definition.Queryable;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;
import com.silverforge.webconnector.definitions.Connectable;

import java.util.ArrayList;
import java.util.List;

public class QueryOperations extends Operations {
    public QueryOperations(Connectable connector, ElasticSettings elasticSettings) {
        super(connector, elasticSettings);
    }

    public <T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType) {

        List<T> retValue = new ArrayList<>();
        try {
            String queryTemplate;
            String queryIds = StringUtils.makeCommaSeparatedListWithQuotationMark(ids);
            String query;
            String queryPath;
            if (TextUtils.isEmpty(type)) {
                queryTemplate = StreamUtils.getRawContent(context, R.raw.search_by_ids);
                query = queryTemplate.replace("{{IDS}}", queryIds);
            } else {
                queryTemplate = StreamUtils.getRawContent(context, R.raw.search_by_ids_and_type);
                query = queryTemplate
                        .replace("{{IDS}}", queryIds)
                        .replace("{{TYPE}}", type);
            }

            queryPath = getOperationPath(indices, null, OperationType.SEARCH);

            String documents = connector.post(queryPath, query).getResult();
            retValue = ElasticClientMapper.mapToHitList(documents, classType);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }

        return retValue;
    }

    public <T> List<T> search(String query, Class<T> classType) {
        return processSearch(query, classType);
    }

    public <T> List<T> search(String index, String query, Class<T> classType)
            throws IllegalArgumentException{

        if (TextUtils.isEmpty(index))
            throw new IllegalArgumentException("index cannot be null or empty");

        return processSearch(index, query, classType);
    }

    public <T> List<T> search(Queryable query, Class<T> classType) {
        return processSearch(query.getQueryString(), classType);
    }

    public <T> List<T> search(String index, Queryable query, Class<T> classType)
            throws IllegalArgumentException {

        if (TextUtils.isEmpty(index))
            throw new IllegalArgumentException("index cannot be null or empty");

        return processSearch(index, query.getQueryString(), classType);
    }


    private <T> List<T> processSearch(String queryString, Class<T> classType) {
        return processSearch(null, queryString, classType);
    }

    private <T> List<T> processSearch(String index, String queryString, Class<T> classType) {

        List<T> retValue = new ArrayList<>();

        try {
            String queryPath;
            if (TextUtils.isEmpty(index))
                queryPath = getOperationPath(index, null, OperationType.SEARCH);
            else
                queryPath = getOperationPath(OperationType.SEARCH);

            String documents = connector.post(queryPath, queryString).getResult();
            retValue = ElasticClientMapper.mapToHitList(documents, classType);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }

        return retValue;
    }
}
