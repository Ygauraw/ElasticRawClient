package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public final class Query
        implements Queryable {

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
            if (item.getName().equals(QueryBuilder.INNER_QUERY)) {
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
        private final static String FROM = "from";
        private final static String SIZE = "size";
        private final static String INNER_QUERY = "INNER_QUERY";

        private final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        QueryBuilder() {}

        public QueryBuilder from(Integer from) {
            if (!queryTypeBag.containsKey(FROM))
                queryTypeBag.add(QueryTypeItem.builder().name(FROM).value(from.toString()).build());
            return this;
        }

        public QueryBuilder size(Integer size) {
            if (!queryTypeBag.containsKey(SIZE))
                queryTypeBag.add(QueryTypeItem.builder().name(SIZE).value(size.toString()).build());
            return this;
        }

        public QueryBuilder innerQuery(Queryable query) {
            String queryString = query.getQueryString();
            if (!queryTypeBag.containsKey(INNER_QUERY))
                queryTypeBag.add(QueryTypeItem.builder().name(INNER_QUERY).value(queryString).build());
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }
}
