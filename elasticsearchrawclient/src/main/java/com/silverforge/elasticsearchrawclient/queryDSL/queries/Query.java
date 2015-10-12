package com.silverforge.elasticsearchrawclient.queryDSL.queries;

public final class Query
        implements InnerQuery {

    private final Integer size;
    private final String innerQueryString;

    Query(Integer size, String innerQueryString) {
        this.size = size;
        this.innerQueryString = innerQueryString;
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    @Override
    public String getQueryString() {
        StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder.append("{");
        if (size != null && size > 0)
            queryStringBuilder
                .append("\"size\":")
                .append("\"").append(size).append("\",");

        queryStringBuilder.append("\"query\":").append(innerQueryString).append("}");
        return queryStringBuilder.toString();
    }

    public static class QueryBuilder {
        private Integer size;
        private String innerQueryString;

        QueryBuilder() {}

        public QueryBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public QueryBuilder innerQuery(InnerQuery query) {
            String queryString = query.getQueryString();
            innerQueryString = queryString;
            return this;
        }

        public Query build() {
            return new Query(size, innerQueryString);
        }
    }
}
