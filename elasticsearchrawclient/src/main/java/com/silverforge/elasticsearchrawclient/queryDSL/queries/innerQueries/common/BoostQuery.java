package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public abstract class BoostQuery
        implements Queryable{

    @Override
    public String getQueryString() {
        return null;
    }

    public static abstract class BoostInit<T extends BoostInit<T>> {
        private final static String BOOST = "boost";
        protected QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();
        protected abstract T self();

        public T boost(int boost) {
            queryTypeBag.addItem(BOOST, boost);
            return self();
        }

        public T boost(float boost) {
            queryTypeBag.addItem(BOOST, boost);
            return self();
        }
    }
}
