package com.silverforge.elasticsearchrawclient.utils;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;

public class QueryUtils {
    public static String queryableBuilder(Queryable[] queryables) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("[");

        for (int i = 0; i < queryables.length; i++) {
            if (i > 0)
                queryBuilder.append(",");

            queryBuilder.append(queryables[i].getQueryString());
        }

        queryBuilder.append("]");
        return queryBuilder.toString();
    }


    public static void multiQueryBuilder(QueryTypeArrayList<QueryTypeItem> queryTypeBag, StringBuilder queryString) {
        for (int i = 0; i < queryTypeBag.size(); i++) {
            if (i > 0)
                queryString.append(",");

            QueryTypeItem item = queryTypeBag.get(i);
            queryString.append("\"").append(item.getName()).append("\":");
            String value = item.getValue();
            if (value.startsWith("{") || value.startsWith("[")) {
                queryString.append(value);
            } else {
                queryString.append("\"").append(value).append("\"");
            }
        }
    }

}
