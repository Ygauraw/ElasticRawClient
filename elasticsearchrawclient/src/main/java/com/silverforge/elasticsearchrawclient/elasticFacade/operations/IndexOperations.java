package com.silverforge.elasticsearchrawclient.elasticFacade.operations;

import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.AliasParser;
import com.silverforge.elasticsearchrawclient.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;
import com.silverforge.webconnector.definitions.Connectable;
import com.silverforge.webconnector.model.InvokeStringResult;

import java.util.ArrayList;
import java.util.List;

public class IndexOperations extends Operations {
    public IndexOperations(Connectable connector, ElasticSettings elasticSettings) {
        super(connector, elasticSettings);
    }

    public boolean createIndex(String indexName, String data) {
        String path = StringUtils.ensurePath(indexName);
        InvokeStringResult result = connector.put(path, data);
        return result.isSuccess();
    }

    public boolean indexExists(String indexName) {
        String path = StringUtils.ensurePath(indexName);
        InvokeStringResult result = connector.head(path);
        return result.isSuccess();
    }

    public void removeIndices(String[] indexNames) {
        for (String indexName : indexNames) {
            String path = StringUtils.ensurePath(indexName);
            connector.delete(path);
        }
    }

    public void addAlias(String indexName, String aliasName) {
        String addAliasTemplate
                = StreamUtils.getRawContent(context, R.raw.add_alias);

        String data = addAliasTemplate
                .replace("{{INDEXNAME}}", indexName)
                .replace("{{ALIASNAME}}", aliasName);

        connector.post("/_aliases", data);
    }

    public List<String> getAliases(String index) {
        ArrayList<String> retValue = new ArrayList<>();
        String getPath = String.format("/%s/_aliases", index);

        InvokeStringResult invokeResult = connector.get(getPath);
        if (invokeResult.isSuccess()) {
            List<String> aliasesFromJson = AliasParser.getAliasesFromJson(index, invokeResult.getResult());
            retValue.addAll(aliasesFromJson);
        }
        return retValue;
    }

    public void removeAlias(String indexName, String aliasName) {
        String addAliasTemplate
                = StreamUtils.getRawContent(context, R.raw.remove_alias);

        String data = addAliasTemplate
                .replace("{{INDEXNAME}}", indexName)
                .replace("{{ALIASNAME}}", aliasName);

        connector.post("/_aliases", data);
    }
}
