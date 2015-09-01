package com.silverforge.elasticsearchrawclient.elasticFacade.operations;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.Connectable;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.BulkResultParser;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkActionResult;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkResultItem;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkTuple;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BulkOperations extends Operations {
    public BulkOperations(Connectable connector) {
        super(connector);
    }

    public List<BulkActionResult> bulk(List<BulkTuple> bulkItems) {
        String createTemplate = StreamUtils.getRawContent(context, R.raw.create_action_template);
        String indexTemplate = StreamUtils.getRawContent(context, R.raw.index_action_template);
        String updateTemplate = StreamUtils.getRawContent(context, R.raw.update_action_template);
        String deleteTemplate = StreamUtils.getRawContent(context, R.raw.delete_action_template);

        StringBuilder bodyBuilder = new StringBuilder();

        for (BulkTuple bulkItem : bulkItems) {
            prepareRequestBody(createTemplate, indexTemplate, updateTemplate, deleteTemplate, bodyBuilder, bulkItem);
        }

        // Execute bulk actions
        String bulkRequestBody = bodyBuilder.append(StringUtils.LINE_SEPARATOR).toString();
        InvokeResult bulkResult = connector.post("/_bulk", bulkRequestBody);

        return prepareBulkActionResults(bulkItems, bulkResult);
    }

    private ArrayList<BulkActionResult> prepareBulkActionResults(List<BulkTuple> bulkItems, InvokeResult bulkResult) {
        List<BulkResultItem> bulkResultItemList = BulkResultParser.getResults(bulkResult.getResult());
        ArrayList<BulkActionResult> retValue = new ArrayList<>();
        for (int i = 0; i < bulkItems.size(); i++) {
            BulkTuple tuple = bulkItems.get(i);
            BulkResultItem resultItem = bulkResultItemList.get(i);
            BulkActionResult bulkActionResult
                = new BulkActionResult(resultItem.getOperation(),
                    resultItem.getIndexName(),
                    resultItem.getTypeName(),
                    resultItem.getId(),
                    resultItem.getVersion(),
                    resultItem.getStatus(),
                    resultItem.getFound(), tuple);

            retValue.add(bulkActionResult);
        }
        return retValue;
    }

    private void prepareRequestBody(String createTemplate, String indexTemplate, String updateTemplate, String deleteTemplate, StringBuilder bodyBuilder, BulkTuple bulkItem) {
        String indexName = ensureIndexName(bulkItem);
        String typeName = ensureTypeName(bulkItem);
        String documentId = bulkItem.getId();
        String bulkItemEntityJson = "";
        OperationType operationType = bulkItem.getOperationType();

        switch (operationType) {
            case CREATE:
                bulkItemEntityJson = ElasticClientMapper.mapToJson(bulkItem.getEntity());
                operationType.prepareTemplateForAction(createTemplate, bodyBuilder, indexName, typeName, documentId, bulkItemEntityJson);
                break;
            case DELETE:
                operationType.prepareTemplateForAction(deleteTemplate, bodyBuilder, indexName, typeName, documentId, bulkItemEntityJson);
                break;
            case UPDATE:
                bulkItemEntityJson = ElasticClientMapper.mapToJson(bulkItem.getEntity());
                operationType.prepareTemplateForAction(updateTemplate, bodyBuilder, indexName, typeName, documentId, bulkItemEntityJson);
                break;
            case INDEX:
                bulkItemEntityJson = ElasticClientMapper.mapToJson(bulkItem.getEntity());
                operationType.prepareTemplateForAction(indexTemplate, bodyBuilder, indexName, typeName, documentId, bulkItemEntityJson);
                break;
        }
    }

    private String ensureTypeName(BulkTuple bulkItem) {
        String typeName = bulkItem.getTypeName();
        if (TextUtils.isEmpty(typeName)) {
            String[] types = connector.getSettings().getTypes();
            if (types != null && types.length > 0)
                typeName = types[0];
        }
        return typeName;
    }

    private String ensureIndexName(BulkTuple bulkItem) {
        String indexName = bulkItem.getIndexName();
        if (TextUtils.isEmpty(indexName)) {
            String[] indices = connector.getSettings().getIndices();
            if (indices != null && indices.length > 0)
                indexName = indices[0];
        }
        return indexName;
    }
}
