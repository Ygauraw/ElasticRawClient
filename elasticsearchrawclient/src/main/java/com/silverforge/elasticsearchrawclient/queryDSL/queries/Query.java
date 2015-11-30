package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.ComposableQuery;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.definition.Sortable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public final class Query
        implements Queryable, ComposableQuery {

    private final QueryTypeArrayList<QueryTypeItem> queryBag;

    Query(QueryBuilder queryBuilder) {
        queryBag = queryBuilder.queryBag;
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .queryGenerator()
            .generate(queryBag);
    }

    public static class QueryBuilder {
        private final QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        QueryBuilder() {}

        public QueryBuilder from(int from) {
            queryBag.addItem(Constants.FROM, from);
            return this;
        }

        public QueryBuilder size(int size) {
            queryBag.addItem(Constants.SIZE, size);
            return this;
        }

        public QueryBuilder query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return this;
        }

        public QueryBuilder sort(Sortable... sortables) {
            queryBag.addItem(Constants.SORT, sortables);
            return this;
        }

        public QueryBuilder trackScores(boolean track) {
            queryBag.addItem(Constants.TRACK_SCORES, track);
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }
}
