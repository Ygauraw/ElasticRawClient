package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class BoostingQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    public BoostingQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static BoostingQueryBuilder builder() {
        return new BoostingQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .boostingQueryGenerator()
            .generate(queryBag);
    }

    public static class BoostingQueryBuilder {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        public BoostingQueryBuilder positive(Queryable... queries) {
            queryBag.addItem(Constants.POSITIVE, queries);
            return this;
        }

        public BoostingQueryBuilder negative(Queryable... queries) {
            queryBag.addItem(Constants.NEGATIVE, queries);
            return this;
        }

        public BoostingQueryBuilder negativeBoost(ZeroToOneRangeOperator negativeBoostOperator) {
            String value = negativeBoostOperator.toString();
            queryBag.addItem(Constants.NEGATIVE_BOOST, value);
            return this;
        }

        public BoostingQuery build() {
            return new BoostingQuery(queryBag);
        }
    }
}
