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
        String lineSeparator = System.getProperty("line.separator");

        for (BulkTuple bulkItem : bulkItems) {
            OperationType operationType = bulkItem.getOperationType();

            String indexName = bulkItem.getIndexName();
            if (TextUtils.isEmpty(indexName)) {
                String[] indices = connector.getSettings().getIndices();
                if (indices != null && indices.length > 0)
                    indexName = indices[0];
            }

            String typeName = bulkItem.getTypeName();
            if (TextUtils.isEmpty(typeName)) {
                String[] types = connector.getSettings().getTypes();
                if (types != null && types.length > 0)
                    typeName = types[0];
            }

            String documentId = bulkItem.getId();

            String bulkItemEntityJson = "";
            if (operationType != OperationType.DELETE)
                bulkItemEntityJson = ElasticClientMapper.mapToJson(bulkItem.getEntity());

            switch (operationType) {
                case CREATE:
                    if (!TextUtils.isEmpty(indexName)
                            && !TextUtils.isEmpty(typeName)
                            && !TextUtils.isEmpty(documentId)
                            && !TextUtils.isEmpty(bulkItemEntityJson)) {

                        String createActionJson
                                = createTemplate
                                .replace("{{INDEX}}", indexName)
                                .replace("{{TYPE}}", typeName)
                                .replace("{{ID}}", documentId);

                        bodyBuilder.append(createActionJson);
                        bodyBuilder.append(bulkItemEntityJson).append(lineSeparator);
                    }
                    break;
                case DELETE:
                    if (!TextUtils.isEmpty(indexName)
                            && !TextUtils.isEmpty(typeName)
                            && !TextUtils.isEmpty(documentId)) {

                        String deleteActionJson
                                = deleteTemplate
                                .replace("{{INDEX}}", indexName)
                                .replace("{{TYPE}}", typeName)
                                .replace("{{ID}}", documentId);

                        bodyBuilder.append(deleteActionJson);
                    }
                    break;
                case UPDATE:
                    if (!TextUtils.isEmpty(indexName)
                            && !TextUtils.isEmpty(typeName)
                            && !TextUtils.isEmpty(documentId)
                            && !TextUtils.isEmpty(bulkItemEntityJson)) {

                        String updateActionJson
                                = updateTemplate
                                .replace("{{INDEX}}", indexName)
                                .replace("{{TYPE}}", typeName)
                                .replace("{{ID}}", documentId);

                        bodyBuilder.append(updateActionJson);
                        bodyBuilder.append("{\"doc\":").append(bulkItemEntityJson).append("}").append(lineSeparator);
                    }
                    break;
                case INDEX:
                    if (!TextUtils.isEmpty(indexName)
                            && !TextUtils.isEmpty(typeName)
                            && !TextUtils.isEmpty(bulkItemEntityJson)) {

                        String indexActionJson
                                = indexTemplate
                                .replace("{{INDEX}}", indexName)
                                .replace("{{TYPE}}", typeName);

                        bodyBuilder.append(indexActionJson);
                        bodyBuilder.append(bulkItemEntityJson).append(lineSeparator);
                    }
                    break;
            }
        }

        String bulkRequestBody = bodyBuilder.append(lineSeparator).toString();
        InvokeResult bulkResult = connector.post("/_bulk", bulkRequestBody);

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
}
