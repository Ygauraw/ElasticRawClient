package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Generator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.ConstantScoreQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import br.com.zbra.androidlinq.Stream;

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

    protected String generateChildren(Map<String, String> childItems) {
        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            writeEntries(childItems);
            jsonGenerator.writeEndObject();
            jsonGenerator.close();
            retValue = getOutputStreamValue();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }

    protected String generateFunctionChildren(Map<String, String> childItems) {
        return generateFunctionChildren(null, childItems);
    }

    protected String generateFunctionChildren(String queryName, Map<String, String> childItems) {
        String retValue = "";
        try {
            Map.Entry<String, String> filter = stream(childItems)
                .firstOrNull(ci -> ci.getKey().equals(Constants.FILTER));

            Map<String, String> children = stream(childItems)
                .where(ci -> !ci.getKey().equals(Constants.FILTER))
                .toMap(ci -> ci.getKey(), ci -> ci.getValue());

            jsonGenerator.writeStartObject();
            if (filter != null)
                jsonGenerator.writeStringField(filter.getKey(), filter.getValue());

            if (!TextUtils.isEmpty(queryName))
                jsonGenerator.writeObjectFieldStart(queryName);

            writeEntries(children);

            if (!TextUtils.isEmpty(queryName))
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

    protected String generateScriptScoreChildren(String queryName, Map<String, String> childItems) {
        String retValue = "";
        try {
            Map.Entry<String, String> filter = stream(childItems)
                .firstOrNull(ci -> ci.getKey().equals(Constants.FILTER));

            Map.Entry<String, String> script = stream(childItems)
                .firstOrNull(ci -> ci.getKey().equals(Constants.SCRIPT));

            Map<String, String> children = stream(childItems)
                .where(ci -> !ci.getKey().equals(Constants.FILTER))
                .where(ci -> !ci.getKey().equals(Constants.SCRIPT))
                .toMap(ci -> ci.getKey(), ci -> ci.getValue());

            jsonGenerator.writeStartObject();
            if (filter != null)
                jsonGenerator.writeStringField(filter.getKey(), filter.getValue());

            if (script != null)
                jsonGenerator.writeStringField(queryName, script.getValue());

            writeEntries(children);

            jsonGenerator.writeEndObject();
            jsonGenerator.close();
            retValue = getOutputStreamValue();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return retValue;
    }

    protected String generateFuzzyChildren(String queryName,
                                           QueryTypeItem parent,
                                           Map<String, String> childItems) {

        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            String parentValue = getParentValue(parent);
            if (stream(childItems).count() == 1
                    && stream(childItems).any(i -> i.getKey().equals(Constants.VALUE))) {

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

    protected String generateGeoShapeChildren(String queryName,
                                           QueryTypeItem parent,
                                           Map<String, String> childItems) {

        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);

            String parentValue = getParentValue(parent);
            jsonGenerator.writeObjectFieldStart(parentValue);

            Map.Entry<String, String> shapeEntry = stream(childItems)
                .firstOrDefault(ci ->
                        ci.getKey().equals(Constants.INDEXED_SHAPE),
                    Maps.immutableEntry(Constants.SHAPE, Constants.SHAPE));

            jsonGenerator.writeObjectFieldStart(shapeEntry.getValue());

            if (shapeEntry.getKey().equals(Constants.SHAPE)) {
                Map<String, String> map = stream(childItems)
                    .where(ci -> ci.getKey().equals(Constants.TYPE)
                        || ci.getKey().equals(Constants.COORDINATES))
                    .toMap(ci -> ci.getKey(), ci -> ci.getValue());

                writeEntries(map);
            } else {
                Map<String, String> map = stream(childItems)
                    .where(ci ->
                           ci.getKey().equals(Constants.ID)
                        || ci.getKey().equals(Constants.TYPE)
                        || ci.getKey().equals(Constants.INDEX)
                        || ci.getKey().equals(Constants.PATH))
                    .toMap(ci -> ci.getKey(), ci -> ci.getValue());

                writeEntries(map);
            }

            jsonGenerator.writeEndObject();

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

    protected String generateParentChildren(String queryName, QueryTypeItem parent, Map<String, String> childItems) {
        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);
            jsonGenerator.writeObjectFieldStart(parent.getValue());
            writeEntries(childItems);
            jsonGenerator.writeEndObject();
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
            jsonGenerator.writeObjectFieldStart(queryName);
            writeEntries(childItems);
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

    protected String generateSpanFirst(String queryName, Map<String, String> childItems) {
        String retValue = "";
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(queryName);
            jsonGenerator.writeObjectFieldStart("match");
            jsonGenerator.writeObjectFieldStart("span_term");

            jsonGenerator.writeStringField(
                stream(childItems)
                    .firstOrDefault(c -> c.getKey().equals(Constants.FIELD_NAME), Maps.immutableEntry(Constants.FIELD_NAME, "_all")).getValue(),
                stream(childItems)
                    .firstOrDefault(c -> c.getKey().equals(Constants.VALUE), Maps.immutableEntry(Constants.VALUE, "")).getValue()
            );

            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();

            jsonGenerator.writeStringField(Constants.END,
                stream(childItems)
                    .firstOrDefault(c -> c.getKey().equals(Constants.END), Maps.immutableEntry(Constants.END, "3")).getValue()
            );

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

    protected String generateDecayFunction(
            String functionName,
            QueryTypeItem parent,
            Map<String, String> childItems) {

        String retValue = "";

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart(functionName);

            Map.Entry<String, String> multiValueMode = stream(childItems)
                .firstOrNull(ci -> ci.getKey().equals(Constants.MULTI_VALUE_MODE));

            if (parent != null) {
                Map<String, String> children = stream(childItems)
                    .where(ci -> !ci.getKey().equals(Constants.MULTI_VALUE_MODE))
                    .toMap(ci -> ci.getKey(), ci -> ci.getValue());

                jsonGenerator.writeObjectFieldStart(parent.getValue());
                writeEntries(children);
                jsonGenerator.writeEndObject();
            }

            if (multiValueMode != null)
                jsonGenerator.writeStringField(multiValueMode.getKey(), multiValueMode.getValue());

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
