package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class CommonTermsQuery
        extends MinimumShouldMatchQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected CommonTermsQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .commonTermsQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new CommonTermsQueryBuilder();
    }

    public static class CommonTermsQueryBuilder extends Init<CommonTermsQueryBuilder> {
        @Override
        protected CommonTermsQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.MinimumShouldMatchInit<T> {

        public T field(String fieldName) {
            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T query(String query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T cutoffFrequency(ZeroToOneRangeOperator cutOffFrequencyOperator) {
            String value = cutOffFrequencyOperator.toString();
            queryBag.addItem(Constants.CUTOFF_FREQUENCY, value);
            return self();
        }

        // region Low Freq

        public T lowFreq(int value) {
            queryBag.addItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqPercentage(int value) {
            queryBag.addPercentageItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqPercentage(float value) {
            queryBag.addPercentageItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqCombination(String expression) {
            queryBag.addItem(Constants.LOW_FREQ, expression);
            return self();
        }

        // endregion

        // region High Freq

        public T highFreq(int value) {
            queryBag.addItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqPercentage(int value) {
            queryBag.addPercentageItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqPercentage(float value) {
            queryBag.addPercentageItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqCombination(String expression) {
            queryBag.addItem(Constants.HIGH_FREQ, expression);
            return self();
        }

        // endregion

        public T lowFreqOperator(LogicOperator lowFreqOperator) {
            String value = lowFreqOperator.toString();
            queryBag.addItem(Constants.LOW_FREQ_OPERATOR, value);
            return self();
        }

        public T highFreqOperator(LogicOperator highFreqOperator) {
            String value = highFreqOperator.toString();
            queryBag.addItem(Constants.HIGH_FREQ_OPERATOR, value);
            return self();
        }

        public CommonTermsQuery build() {
            return new CommonTermsQuery(queryBag);
        }
    }
}
