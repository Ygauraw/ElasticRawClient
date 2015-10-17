package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.utils.BooleanUtils;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class BoolQuery
    implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

    public BoolQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {

    }

    public static BoolQueryBuilder builder() {
        return new BoolQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return null;
    }

    public static class BoolQueryBuilder {
        private static final String MUST = "must";
        private static final String MUST_NOT = "must_not";
        private static final String SHOULD = "should";
        private static final String DISABLE_COORD = "disable_coord";
        private static final String MINIMUM_SHOULD_MATCH = "minimum_should_match";

        private QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        public BoolQueryBuilder must(Queryable... queries) {
            String mustQuery = queryableBuilder(queries);
            if (!queryTypeBag.containsKey(MUST))
                queryTypeBag.add(QueryTypeItem.builder().name(MUST).value(mustQuery).build());
            return this;
        }

        public BoolQueryBuilder mustNot(Queryable... queries) {
            String mustNotQuery = queryableBuilder(queries);
            if (!queryTypeBag.containsKey(MUST_NOT))
                queryTypeBag.add(QueryTypeItem.builder().name(MUST_NOT).value(mustNotQuery).build());
            return this;
        }

        public BoolQueryBuilder should(Queryable... queries) {
            String shouldQuery = queryableBuilder(queries);
            if (!queryTypeBag.containsKey(SHOULD))
                queryTypeBag.add(QueryTypeItem.builder().name(SHOULD).value(shouldQuery).build());
            return this;
        }

        public BoolQueryBuilder disableCoord(boolean value) {
            if(!queryTypeBag.containsKey(DISABLE_COORD))
                queryTypeBag.add(QueryTypeItem.builder().name(DISABLE_COORD).value(BooleanUtils.booleanValue(value)).build());
            return this;
        }

        public BoolQuery build() {
            return new BoolQuery(queryTypeBag);
        }

        private String queryableBuilder(Queryable[] queryables) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("[");

            for (int i = 0; i < queryables.length; i++) {
                if (i > 0)
                    queryBuilder.append(",");

                queryBuilder.append(queryables[i].getQueryString());
            }

            queryBuilder.append("]");
            return queryBuilder.toString();
        }
    }
}
