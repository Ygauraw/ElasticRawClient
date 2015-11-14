package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Functionable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.BoostModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class FunctionScoreQuery
            extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public FunctionScoreQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new FunctionScoreQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .functionScoreQueryGenerator()
                .generate(queryBag);
    }

    public static class FunctionScoreQueryBuilder
            extends Init<FunctionScoreQueryBuilder> {

        @Override
        protected FunctionScoreQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostQuery.BoostInit<T> {

        public T query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T maxBoost(int maxBoost) {
            queryBag.addItem(Constants.MAX_BOOST, maxBoost);
            return self();
        }

        public T maxBoost(float maxBoost) {
            queryBag.addItem(Constants.MAX_BOOST, maxBoost);
            return self();
        }

        public T scoreMode(ScoreModeOperator scoreModeOperator) {
            String value = scoreModeOperator.toString();
            queryBag.addItem(Constants.SCORE_MODE, value);
            return self();
        }

        public T boostMode(BoostModeOperator boostModeOperator) {
            String value = boostModeOperator.toString();
            queryBag.addItem(Constants.BOOST_MODE, value);
            return self();
        }

        public T minScore(int minScore) {
            queryBag.addItem(Constants.MIN_SCORE, minScore);
            return self();
        }

        public T minScore(float minScore) {
            queryBag.addItem(Constants.MIN_SCORE, minScore);
            return self();
        }

        public T functions(Functionable... function) {
            queryBag.addItem(Constants.FUNCTION, function);
            return self();
        }

        public T function(Functionable function) {
            queryBag.addItem(Constants.FUNCTION, function);
            return self();
        }

        public FunctionScoreQuery build() {
            return new FunctionScoreQuery(queryBag);
        }
    }
}
