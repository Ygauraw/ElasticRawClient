package com.silverforge.elasticsearchrawclient.utils;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
}
