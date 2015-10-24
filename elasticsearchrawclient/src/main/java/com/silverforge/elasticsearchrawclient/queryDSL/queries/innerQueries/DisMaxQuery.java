package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.commonQueryTemplates.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.*;

public class DisMaxQuery
        extends BoostQuery {

    protected DisMaxQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        super(queryTypeBag);
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

    public static abstract class Init<T extends Init<T>> extends BoostQuery.Init<T> {
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
            if (queries != null && queries.length > 0) {
                List<String> queryStrings = stream(queries)
                    .select(q -> q.getQueryString())
                    .toList();

                StringBuilder queryJoin = new StringBuilder();
                for (int i = 0; i < queryStrings.size(); i++) {
                    if (i > 0)
                        queryJoin.append(",");

                    queryJoin.append(queryStrings.get(i));
                }

                if (!queryTypeBag.containsKey(QUERIES))
                    queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(QUERIES)
                    .value(queryJoin.toString())
                    .build());
            }
            return self();
        }

        public DisMaxQuery build() {
            return new DisMaxQuery(queryTypeBag);
        }
    }
}
