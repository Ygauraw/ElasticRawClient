package com.silverforge.elasticsearchrawclient.elasticFacade.operations;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.Connectable;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryOperations extends Operations {
    public QueryOperations(Connectable connector) {
        super(connector);
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

        List<T> retValue = new ArrayList<>();

        try {
            String queryPath = getOperationPath(OperationType.SEARCH);
            String documents = connector.post(queryPath, query).getResult();

            retValue = ElasticClientMapper.mapToHitList(documents, classType);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }

        return retValue;
    }

    public <T> List<T> search(String index, String query, Class<T> classType)
            throws IllegalArgumentException{

        if (TextUtils.isEmpty(index))
            throw new IllegalArgumentException("index cannot be null or empty");

        List<T> retValue = new ArrayList<>();

        try	{

            String queryPath = getOperationPath(index, null, OperationType.SEARCH);
            String documents = connector.post(queryPath, query).getResult();

            retValue = ElasticClientMapper.mapToHitList(documents, classType);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
        }

        return retValue;
    }
}
