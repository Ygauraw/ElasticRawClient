package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Generator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public class QueryGenerator
        implements Generator {

    private final static String TAG = QueryGenerator.class.getName();

    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    protected final JsonFactory jsonFactory = new JsonFactory();
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
    public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
        return null;
    }

    protected String generateParentWithChildren (
            String queryName,
            QueryTypeItem parent,
            Map<String, String> childItems) {
        return generateParentWithChildren(queryName, parent, childItems, false);
    }

    protected String generateObjectParentWithChildren (
            String queryName,
            QueryTypeItem parent,
            Map<String, String> childItems) {
        return generateParentWithChildren(queryName, parent, childItems, true);
    }

    protected String generateEmptyParent (
            String queryName) {
        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }

    protected String generateParentWithChildren (
            String queryName,
            QueryTypeItem parent,
            Map<String, String> childItems,
            boolean isEmptyParent) {

        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            String parentValue;
            if (parent == null) {
                parentValue = "_all";
            } else
                parentValue = parent.getValue();

            // TODO : it's awful, cries for refactor

            if (stream(childItems).any(i -> i.getKey().equals(Constants.VALUE))) {
                jsonGenerator.writeStringField(
                    parentValue,
                    stream(childItems)
                        .first(i -> i.getKey().equals(Constants.VALUE))
                        .getValue());
            } else {
                if (stream(childItems).any()) {
                    jsonGenerator.writeObjectFieldStart(parentValue);

                    boolean minimumExists = stream(childItems).any(i -> i.getKey().equals(Constants.MINIMUM_SHOULD_MATCH));
                    Map.Entry<String, String> lowFreq = stream(childItems)
                        .firstOrNull(i -> i.getKey().equals(Constants.LOW_FREQ));
                    Map.Entry<String, String> highFreq = stream(childItems)
                        .firstOrNull(i -> i.getKey().equals(Constants.HIGH_FREQ));

                    if (!minimumExists && (lowFreq != null || highFreq != null)) {
                        Map<String, String> filteredChildItems = stream(childItems.entrySet())
                            .where(i -> !i.getKey().equals(Constants.MINIMUM_SHOULD_MATCH))
                            .where(i -> !i.getKey().equals(Constants.LOW_FREQ))
                            .where(i -> !i.getKey().equals(Constants.HIGH_FREQ))
                            .toMap(i -> i.getKey(), i -> i.getValue());

                        for (Map.Entry<String, String> entry : filteredChildItems.entrySet()) {
                            jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
                        }

                        jsonGenerator.writeObjectFieldStart(Constants.MINIMUM_SHOULD_MATCH);
                        if (lowFreq != null)
                            jsonGenerator.writeStringField(lowFreq.getKey(), lowFreq.getValue());
                        if (highFreq != null)
                            jsonGenerator.writeStringField(highFreq.getKey(), highFreq.getValue());
                        jsonGenerator.writeEndObject();
                    } else if (minimumExists) {
                        Map<String, String> filteredChildItems = stream(childItems.entrySet())
                            .where(i -> !i.getKey().equals(Constants.LOW_FREQ))
                            .where(i -> !i.getKey().equals(Constants.HIGH_FREQ))
                            .toMap(i -> i.getKey(), i -> i.getValue());

                        for (Map.Entry<String, String> entry : filteredChildItems.entrySet()) {
                            jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
                        }
                    } else {
                        for (Map.Entry<String, String> entry : childItems.entrySet()) {
                            jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
                        }
                    }

                    jsonGenerator.writeEndObject();
                } else {
                    if (isEmptyParent) {
                        jsonGenerator.writeObjectFieldStart(parentValue);
                        jsonGenerator.writeEndObject();
                    } else
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
                jsonGenerator.writeStringField(entry.getKey(), value);
            }

            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = outputStream
                .toString()
                .replace("\\", "")
                .replace("\"[", "[")
                .replace("]\"", "]")
                .replace("\"{", "{")
                .replace("}\"", "}");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }
}
