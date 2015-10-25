package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.BooleanUtils;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.QueryUtils;

public class BoolQuery
    extends MinimumShouldMatchQuery {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    public BoolQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public static Init<?> builder() {
        return new BoolQueryBuilder();
    }

    @Override
    public String getQueryString() {

        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"bool\":{");

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

        queryString.append("}}");
        return queryString.toString();
    }

    public static class BoolQueryBuilder extends Init<BoolQueryBuilder> {
        @Override
        protected BoolQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.MinimumShouldMatchInit<T> {
        private static final String MUST = "must";
        private static final String MUST_NOT = "must_not";
        private static final String SHOULD = "should";
        private static final String DISABLE_COORD = "disable_coord";
        private static final String BOOST = "boost";

        protected abstract T self();

        public T must(Queryable... queries) {
            queryTypeBag.addItem(MUST, queries);
            return self();
        }

        public T mustNot(Queryable... queries) {
            queryTypeBag.addItem(MUST_NOT, queries);
            return self();
        }

        public T should(Queryable... queries) {
            queryTypeBag.addItem(SHOULD, queries);
            return self();
        }

        public T disableCoord(boolean value) {
            queryTypeBag.addItem(DISABLE_COORD, value);
            return self();
        }

        public T boost(int boost) {
            queryTypeBag.addItem(BOOST, boost);
            return self();
        }

        public T boost(float boost) {
            queryTypeBag.addItem(BOOST, boost);
            return self();
        }

        public BoolQuery build() {
            return new BoolQuery(queryTypeBag);
        }
    }
}
