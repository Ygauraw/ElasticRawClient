package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
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
        return QueryFactory
            .disMaxQueryGenerator()
            .generate(queryTypeBag);
    }

    public static class DisMaxQueryBuilder extends Init<DisMaxQueryBuilder> {
        @Override
        protected DisMaxQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends BoostQuery.BoostInit<T> {
        public T tieBreaker(ZeroToOneRangeOperator tieBreakerOperator) {
            String value = tieBreakerOperator.toString();
            queryTypeBag.addItem(Constants.TIE_BREAKER, value);
            return self();
        }

        public T queries(Queryable... queries) {
            queryTypeBag.addItem(Constants.QUERIES, queries);
            return self();
        }

        public DisMaxQuery build() {
            return new DisMaxQuery(queryTypeBag);
        }
    }
}
