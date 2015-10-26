package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.ComposableQuery;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public final class Query
        implements Queryable, ComposableQuery {

    private final QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    Query(QueryBuilder queryBuilder) {
        queryTypeBag = queryBuilder.queryTypeBag;
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    @Override
    public String getQueryString() {
        StringBuilder queryString = new StringBuilder();

        queryString.append("{");
        for (int i = 0; i < queryTypeBag.size(); i++) {
            if (i > 0)
                queryString.append(",");

            QueryTypeItem item = queryTypeBag.get(i);
            if (item.getName().equals(Constants.INNER_QUERY)) {
                queryString.append("\"query\":").append(item.getValue());
            } else {
                queryString
                    .append("\"")
                    .append(item.getName())
                    .append("\":\"")
                    .append(item.getValue())
                    .append("\"");
            }
        }
        queryString.append("}");

        return queryString.toString();
    }

    public static class QueryBuilder {
        private final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        QueryBuilder() {}

        public QueryBuilder from(int from) {
            queryTypeBag.addItem(Constants.FROM, from);
            return this;
        }

        public QueryBuilder size(Integer size) {
            queryTypeBag.addItem(Constants.SIZE, size);
            return this;
        }

        public QueryBuilder innerQuery(Queryable query) {
            queryTypeBag.addItem(Constants.INNER_QUERY, query);
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }
}
