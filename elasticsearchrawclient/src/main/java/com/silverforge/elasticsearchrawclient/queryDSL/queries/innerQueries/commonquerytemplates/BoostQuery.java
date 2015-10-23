package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.commonQueryTemplates;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.BoostingQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class BoostQuery
        implements Queryable{

    protected QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

    protected BoostQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    public static class BoostQueryBuilder extends Init<BoostQueryBuilder> {
        @Override
        protected BoostQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private final static String BOOST = "boost";

        protected QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();
        protected abstract T self();

        public T boost(float boost) {
            if (!queryTypeBag.containsKey(BOOST))
                queryTypeBag.add(QueryTypeItem.builder().name(BOOST).value(Float.toString(boost)).build());

            return self();
        }

        public BoostQuery build() {
            return new BoostQuery(queryTypeBag);
        }
    }
}
