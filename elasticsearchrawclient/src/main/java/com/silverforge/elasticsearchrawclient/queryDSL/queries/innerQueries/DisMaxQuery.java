package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class DisMaxQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    protected DisMaxQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public static Init<?> builder() {
        return new DisMaxQueryBuilder();
    }

    @Override
    public String getQueryString() {
        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"dis_max\":{");

        for (int i = 0; i < queryTypeBag.size(); i++) {
            if (i > 0)
                queryString.append(",");

            QueryTypeItem item = queryTypeBag.get(i);
            String name = item.getName();
            String value = item.getValue();
            queryString
                .append("\"")
                .append(name)
                .append("\":");

            if (name.equals(Init.QUERIES)) {
                queryString
                    .append("[")
                    .append(value)
                    .append("]");
            } else {
                queryString
                    .append("\"")
                    .append(value)
                    .append("\"");
            }
        }

        queryString.append("}}");
        return queryString.toString();
    }

    public static class DisMaxQueryBuilder extends Init<DisMaxQueryBuilder> {
        @Override
        protected DisMaxQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends BoostQuery.BoostInit<T> {
        private final static String TIE_BREAKER = "tie_breaker";
        private final static String QUERIES = "queries";

        public T tieBreaker(ZeroToOneRangeOperator tieBreakerOperator) {
            if (!queryTypeBag.containsKey(TIE_BREAKER))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(TIE_BREAKER)
                    .value(tieBreakerOperator.toString())
                    .build());
            return self();
        }

        public T queries(Queryable... queries) {
            queryTypeBag.addItem(QUERIES, queries);
            return self();
        }

        public DisMaxQuery build() {
            return new DisMaxQuery(queryTypeBag);
        }
    }
}
