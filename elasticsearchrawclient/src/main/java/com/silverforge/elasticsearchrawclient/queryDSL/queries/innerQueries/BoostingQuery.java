package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.QueryUtils;

public class BoostingQuery
    implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

    public BoostingQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public static BoostingQueryBuilder builder() {
        return new BoostingQueryBuilder();
    }

    @Override
    public String getQueryString() {
        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"boosting\":{");

        QueryUtils.multiQueryBuilder(queryTypeBag, queryString);

        queryString.append("}}");
        return queryString.toString();
    }

    public static class BoostingQueryBuilder {
        private final static String POSITIVE = "positive";
        private final static String NEGATIVE = "negative";
        private final static String NEGATIVE_BOOST = "negative_boost";

        private QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        public BoostingQueryBuilder positive(Queryable... queries) {
            String queryString = QueryUtils.queryableBuilder(queries);
            if (!queryTypeBag.containsKey(POSITIVE))
                queryTypeBag.add(QueryTypeItem.builder().name(POSITIVE).value(queryString).build());
            return this;
        }

        public BoostingQueryBuilder negative(Queryable... queries) {
            String queryString = QueryUtils.queryableBuilder(queries);
            if (!queryTypeBag.containsKey(NEGATIVE))
                queryTypeBag.add(QueryTypeItem.builder().name(NEGATIVE).value(queryString).build());
            return this;
        }

        public BoostingQueryBuilder negativeBoost(ZeroToOneRangeOperator negativeBoostOperator) {
            if (!queryTypeBag.containsKey(NEGATIVE_BOOST))
                queryTypeBag.add(
                    QueryTypeItem
                        .builder()
                        .name(NEGATIVE_BOOST)
                        .value(negativeBoostOperator.toString())
                        .build());
            return this;
        }

        public BoostingQuery build() {
            return new BoostingQuery(queryTypeBag);
        }
    }
}
