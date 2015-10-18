package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.commonquerytemplates.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.BooleanUtils;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.QueryUtils;

public class BoolQuery
    extends MinimumShouldMatchQuery {

    public BoolQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        super(queryTypeBag);
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

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.Init<T> {
        private static final String MUST = "must";
        private static final String MUST_NOT = "must_not";
        private static final String SHOULD = "should";
        private static final String DISABLE_COORD = "disable_coord";
        private static final String BOOST = "boost";

        protected abstract T self();

        public T must(Queryable... queries) {
            String mustQuery = QueryUtils.queryableBuilder(queries);
            if (!queryTypeBag.containsKey(MUST))
                queryTypeBag.add(QueryTypeItem.builder().name(MUST).value(mustQuery).build());
            return self();
        }

        public T mustNot(Queryable... queries) {
            String mustNotQuery = QueryUtils.queryableBuilder(queries);
            if (!queryTypeBag.containsKey(MUST_NOT))
                queryTypeBag.add(QueryTypeItem.builder().name(MUST_NOT).value(mustNotQuery).build());
            return self();
        }

        public T should(Queryable... queries) {
            String shouldQuery = QueryUtils.queryableBuilder(queries);
            if (!queryTypeBag.containsKey(SHOULD))
                queryTypeBag.add(QueryTypeItem.builder().name(SHOULD).value(shouldQuery).build());
            return self();
        }

        public T disableCoord(boolean value) {
            if(!queryTypeBag.containsKey(DISABLE_COORD))
                queryTypeBag.add(QueryTypeItem.builder().name(DISABLE_COORD).value(BooleanUtils.booleanValue(value)).build());
            return self();
        }

        public T boost(int boost) {
            if (!queryTypeBag.containsKey(BOOST))
                queryTypeBag.add(QueryTypeItem.builder().name(BOOST).value(Integer.toString(boost)).build());
            return self();
        }

        public T boost(float boost) {
            if (!queryTypeBag.containsKey(BOOST))
                queryTypeBag.add(QueryTypeItem.builder().name(BOOST).value(Float.toString(boost)).build());
            return self();
        }

        public BoolQuery build() {
            return new BoolQuery(queryTypeBag);
        }
    }
}
