package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
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
        private QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        public BoostingQueryBuilder positive(Queryable... queries) {
            queryTypeBag.addItem(Constants.POSITIVE, queries);
            return this;
        }

        public BoostingQueryBuilder negative(Queryable... queries) {
            queryTypeBag.addItem(Constants.NEGATIVE, queries);
            return this;
        }

        public BoostingQueryBuilder negativeBoost(ZeroToOneRangeOperator negativeBoostOperator) {
            String value = negativeBoostOperator.toString();
            queryTypeBag.addItem(Constants.NEGATIVE_BOOST, value);
            return this;
        }

        public BoostingQuery build() {
            return new BoostingQuery(queryTypeBag);
        }
    }
}
