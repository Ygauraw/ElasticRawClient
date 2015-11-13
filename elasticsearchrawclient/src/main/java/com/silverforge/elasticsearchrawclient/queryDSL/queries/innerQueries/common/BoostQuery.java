package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common;

import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public abstract class BoostQuery
        implements Queryable {

    @Override
    public String getQueryString() {
        return null;
    }

    public static abstract class BoostInit<T extends BoostInit<T>> {
        protected QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();
        protected abstract T self();

        public T boost(int boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T boost(float boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }
    }
}
