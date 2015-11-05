package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
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

    protected String generateCommonChildren(String queryName,
                                            QueryTypeItem parent,
                                            Map<String, String> childItems) {

        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            String parentValue = getParentValue(parent);
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

                    writeEntries(filteredChildItems);

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

                    writeEntries(filteredChildItems);
                } else {
                    writeEntries(childItems);
                }
                jsonGenerator.writeEndObject();
            } else {
                jsonGenerator.writeObjectFieldStart(parentValue);
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = getOutputStreamValue();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }

    protected String generateMatchChildren(String queryName,
                                           QueryTypeItem parent,
                                           Map<String, String> childItems) {

        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            String parentValue = getParentValue(parent);
            if (stream(childItems).any(i -> i.getKey().equals(Constants.VALUE))) {
                jsonGenerator.writeStringField(
                    parentValue,
                    stream(childItems)
                        .first(i -> i.getKey().equals(Constants.VALUE))
                        .getValue());
            } else {
                if (stream(childItems).any()) {
                    jsonGenerator.writeObjectFieldStart(parentValue);
                    writeEntries(childItems);
                    jsonGenerator.writeEndObject();
                } else {
                    jsonGenerator.writeStringField(parentValue, "");
                }
            }
            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = getOutputStreamValue();
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

            if(!queryName.equals("")) {
                jsonGenerator.writeObjectFieldStart(queryName);
                writeEntries(childItems);
                jsonGenerator.writeEndObject();
            } else {
                writeEntries(childItems);
            }

            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            retValue = getOutputStreamValue();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }

    private String getParentValue(QueryTypeItem parent) {
        Optional<QueryTypeItem> parentItem = Optional.fromNullable(parent);
        String parentValue = parentItem
            .or(QueryTypeItem.builder().value("_all").build())
            .getValue();
        return parentValue;
    }

    private void writeEntries(Map<String, String> childItems) throws IOException {
        for (Map.Entry<String, String> entry : childItems.entrySet()) {
            jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
        }
    }

    private String getOutputStreamValue() {
        return outputStream
            .toString()
            .replace("\\", "")
            .replace("\"[", "[")
            .replace("]\"", "]")
            .replace("\"{", "{")
            .replace("}\"", "}");
    }
}
