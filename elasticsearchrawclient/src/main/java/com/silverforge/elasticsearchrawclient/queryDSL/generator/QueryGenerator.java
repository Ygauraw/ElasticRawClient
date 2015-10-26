package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Generator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public class QueryGenerator
        implements Generator{

    private final static String TAG = QueryGenerator.class.getName();

    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    protected final JsonFactory jsonFactory = new JsonFactory();
    protected final ObjectMapper mapper = new ObjectMapper();
    protected JsonGenerator jsonGenerator;

    protected QueryGenerator() {
        try {
            jsonGenerator = jsonFactory.createGenerator(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public String generate(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        return null;
    }

    protected String generateParentWithChildren (
            String queryName,
            QueryTypeItem parent,
            Map<String, String> childItems) {

        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            String parentValue;
            if (parent == null) {
                parentValue = "_all";
            } else
                parentValue = parent.getValue();

            if (stream(childItems).any(i -> i.getKey().equals(Constants.VALUE))) {
                jsonGenerator.writeStringField(
                    parentValue,
                    stream(childItems)
                        .first(i -> i.getKey().equals(Constants.VALUE))
                        .getValue());
            } else {
                if (stream(childItems).any()) {
                    jsonGenerator.writeObjectFieldStart(parentValue);
                    for (Map.Entry<String, String> entry : childItems.entrySet()) {
                        jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
                    }
                    jsonGenerator.writeEndObject();
                } else {
                    jsonGenerator.writeStringField(parentValue, "");
                }
            }
            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }

    protected String generateChildren(String queryName, Map<String, String> childItems) {
        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            for (Map.Entry<String, String> entry : childItems.entrySet()) {
                String value = entry.getValue();
                if (value.startsWith("[")) {
                    jsonGenerator.writeObjectFieldStart(entry.getKey());
                    jsonGenerator.writeRaw(value);
                } else if (value.startsWith("{")) {
                    JsonNode actualObj = mapper.readTree(value);
                    jsonGenerator.writeObjectField(entry.getKey(), actualObj);
                } else
                    jsonGenerator.writeStringField(entry.getKey(), value);
            }

            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = outputStream
                .toString()
                .replace("\\", "")
                .replace("{[", "[")
                .replace("]}", "]");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }
}
