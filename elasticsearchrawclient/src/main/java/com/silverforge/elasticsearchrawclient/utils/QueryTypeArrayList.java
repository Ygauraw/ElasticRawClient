package com.silverforge.elasticsearchrawclient.utils;

import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Functionable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.SpanQueryable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public class QueryTypeArrayList<T extends QueryTypeItem>
        extends ArrayList<T> {

    public boolean containsKey(String fieldName) {
        boolean retValue = false;
        Iterator<T> iterator = iterator();

        if (size() > 0) {
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (item.getName().toLowerCase().equals(fieldName.toLowerCase())) {
                    retValue = true;
                    break;
                }
            }
        }

        return retValue;
    }

    public T getByKey(String fieldName) {
        T retValue = null;
        Iterator<T> iterator = iterator();

        if (size() > 0) {
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (item.getName().toLowerCase().equals(fieldName.toLowerCase())) {
                    retValue = item;
                    break;
                }
            }
        }

        return retValue;
    }

    public boolean hasKeys(String... keys) {
        boolean hasValues = false;
        HashMap<String, Boolean> wordCheckTable = new HashMap<>();
        for (String word : keys) {
            wordCheckTable.put(word, false);
        }

        Iterator<T> iterator = iterator();

        if (size() > 0) {
            while (iterator.hasNext()) {
                T item = iterator.next();

                for (Map.Entry<String, Boolean> entry : wordCheckTable.entrySet()) {
                    if (item.getName().equals(entry.getKey()))
                        entry.setValue(true);
                }
            }

            hasValues = !wordCheckTable.containsValue(false);
        }
        return hasValues;
    }

    public boolean hasAtLeastOneKey(String... keys) {
        boolean hasValues = false;
        HashMap<String, Boolean> wordCheckTable = new HashMap<>();
        for (String word : keys) {
            wordCheckTable.put(word, false);
        }

        Iterator<T> iterator = iterator();

        if (size() > 0) {
            while (iterator.hasNext()) {
                T item = iterator.next();

                for (Map.Entry<String, Boolean> entry : wordCheckTable.entrySet()) {
                    if (item.getName().equals(entry.getKey()))
                        entry.setValue(true);
                }
            }

            hasValues = wordCheckTable.containsValue(true);
        }
        return hasValues;
    }

    @SuppressWarnings("unchecked")
    public void addParentItem(String key, String value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(StringUtils.ensureNotNull(value))
                .isParent(true)
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, String value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(StringUtils.ensureNotNull(value))
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, boolean value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(BooleanUtils.booleanValue(value))
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, byte value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(Byte.toString(value))
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, short value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(Short.toString(value))
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, int value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(Integer.toString(value))
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, long value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(Long.toString(value))
                .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, float value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(Float.toString(value))
                .build());
    }


    @SuppressWarnings("unchecked")
    public void addItem(String key, double value) {
        if (!containsKey(key))
            add((T) T
                    .builder()
                    .name(key)
                    .value(Double.toString(value))
                    .build());
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, String... values) {
        if (values != null && values.length > 0 && !containsKey(key)) {
            List<String> valuesList = new ArrayList<>();
            for(String i : values) {
                valuesList.add(i);
            }

            add((T) T
                    .builder()
                    .name(key)
                    .value(valuesList.toString())
                    .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, Date value, String format) {
        if (!containsKey(key)) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            add((T) T
                .builder()
                .name(key)
                .value(formatter.format(value))
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, GeoPoint geoPoint) {
        if (!containsKey(key)) {
            String value = String.format("[%s,%s]", geoPoint.getLongitude(), geoPoint.getLatitude());
            add((T) T
                .builder()
                .name(key)
                .value(value)
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, Queryable value) {
        if (value != null && !containsKey(key)) {
            add((T) T
                .builder()
                .name(key)
                .value(value.getQueryString())
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, SpanQueryable value) {
        if (value != null && !containsKey(key)) {
            add((T) T
                    .builder()
                    .name(key)
                    .value(value.getQueryString())
                    .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, Queryable... values) {
        if (values != null && values.length > 0 && !containsKey(key)) {
            String[] queries = stream(values)
                .select(q -> q.getQueryString())
                .toList()
                .toArray(new String[]{});

            String joinedQueries = StringUtils.makeCommaSeparatedList(queries);
            add((T) T
                .builder()
                .name(key)
                .value("[" + joinedQueries + "]")
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, SpanQueryable... values) {
        if (values != null && values.length > 0 && !containsKey(key)) {
            String[] queries = stream(values)
                    .select(q -> q.getQueryString())
                    .toList()
                    .toArray(new String[]{});

            String joinedQueries = StringUtils.makeCommaSeparatedList(queries);
            add((T) T
                    .builder()
                    .name(key)
                    .value("[" + joinedQueries + "]")
                    .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, Functionable value) {
        if (value != null && !containsKey(key)) {
            add((T) T
                .builder()
                .name(key)
                .value(value.getFunctionString())
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, Functionable... values) {
        if (values != null && values.length > 0 && !containsKey(key)) {
            String[] queries = stream(values)
                .select(q -> q.getFunctionString())
                .toList()
                .toArray(new String[]{});

            String joinedQueries = StringUtils.makeCommaSeparatedList(queries);
            add((T) T
                .builder()
                .name(key)
                .value("[" + joinedQueries + "]")
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItemsWithParenthesis(String key, String... values) {
        if (values != null && values.length > 0 && !containsKey(key)) {
            String joinedQueries = StringUtils.makeCommaSeparatedListWithQuotationMark(values);
            add((T) T
                .builder()
                .name(key)
                .value("[" + joinedQueries + "]")
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(String key, Map<String, ?> value) {
        if (value != null && value.size() > 0 && !containsKey(key)) {
            String[] params = stream(value)
                .select(q -> q.getKey() + "\"" + ":" + "\"" + q.getValue())
                .toList()
                .toArray(new String[]{});

            String joinedParams = StringUtils.makeCommaSeparatedListWithQuotationMark(params);
            add((T) T
                .builder()
                .name(key)
                .value("{" + joinedParams + "}")
                .build());
        }
    }

    @SuppressWarnings("unchecked")
    public void addPercentageItem(String key, int value) {
        if (!containsKey(key))
            add((T) T
                .builder()
                .name(key)
                .value(value + "%")
                .build());

    }

    @SuppressWarnings("unchecked")
    public void addPercentageItem(String key, float value) {
        if (!containsKey(key)) {
            String percentage = (value * 100) + "%";
            add((T) T
                .builder()
                .name(key)
                .value(percentage)
                .build());
        }
    }
}
