package com.silverforge.elasticsearchrawclient.utils;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryTypeArrayList<T extends QueryTypeItem> extends ArrayList<T> {

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

    public List<T> getParentItems () {
        List<T> retValue = new ArrayList<>();
        Iterator<T> iterator = iterator();

        if (size() > 0) {
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (item.isParent()) {
                    retValue.add(item);
                }
            }
        }

        return retValue;
    }

    public List<T> getNonParentItems () {
        List<T> retValue = new ArrayList<>();
        Iterator<T> iterator = iterator();

        if (size() > 0) {
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (!item.isParent()) {
                    retValue.add(item);
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
                    if (item.equals(entry.getKey()))
                        entry.setValue(true);
                }
            }

            hasValues = !wordCheckTable.containsValue(false);
        }
        return hasValues;
    }
}
