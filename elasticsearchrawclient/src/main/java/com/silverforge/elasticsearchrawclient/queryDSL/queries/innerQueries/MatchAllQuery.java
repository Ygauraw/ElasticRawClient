package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class MatchAllQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    MatchAllQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new MatchAllQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .matchAllQueryGenerator()
            .generate(queryBag);
    }

    public static class MatchAllQueryBuilder
            extends Init<MatchAllQueryBuilder> {

        @Override
        protected MatchAllQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostInit<T> {

        public MatchAllQuery build() {
            return new MatchAllQuery(queryBag);
        }
    }
}
