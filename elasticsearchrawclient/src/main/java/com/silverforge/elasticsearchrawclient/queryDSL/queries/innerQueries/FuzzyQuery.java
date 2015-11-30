package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.MultiTermQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.FieldValueQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class FuzzyQuery
        extends FieldValueQuery implements MultiTermQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    FuzzyQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .fuzzyQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new FuzzyQueryBuilder();
    }

    public static class FuzzyQueryBuilder
            extends Init<FuzzyQueryBuilder> {

        @Override
        protected FuzzyQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends FieldValueInit<T> {

        public T boost(int boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T boost(float boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T fuzziness(FuzzinessOperator fuzzinessOperator) {
            String value = fuzzinessOperator.toString();
            queryBag.addItem(Constants.FUZZINESS, value);
            return self();
        }

        public T fuzziness(String fuzzinessOperator) {
            queryBag.addItem(Constants.FUZZINESS, fuzzinessOperator);
            return self();
        }

        public T maxExpansions(int maxExpansions) {
            queryBag.addItem(Constants.MAX_EXPANSIONS, maxExpansions);
            return self();
        }

        public T prefixLength(int prefixLength) {
            queryBag.addItem(Constants.PREFIX_LENGTH, prefixLength);
            return self();
        }

        public FuzzyQuery build() {
            return new FuzzyQuery(queryBag);
        }
    }
}
