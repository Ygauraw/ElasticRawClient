package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Filterable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.StrategyOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class FilteredQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public FilteredQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static FilteredQueryBuilder builder() {
        return new FilteredQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .filteredQueryGenerator()
            .generate(queryBag);
    }

    public static class FilteredQueryBuilder extends Init<FilteredQueryBuilder> {
        @Override
        protected FilteredQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T filter(Filterable filter) {
            queryBag.addItem(Constants.FILTER, filter);
            return self();
        }

        public T strategy(StrategyOperator strategyOperator) {
            String value = strategyOperator.toString();
            queryBag.addItem(Constants.STRATEGY, value);
            return self();
        }

        public T strategy(StrategyOperator strategyOperator, int threshold) {
            String value = strategyOperator.toString();
            if (strategyOperator == StrategyOperator.RANDOM_ACCESS_N) {
                if (threshold < 1)
                    threshold = 1;

                value = value.replace("_N", "_" + threshold);
            }

            queryBag.addItem(Constants.STRATEGY, value);
            return self();
        }

        public FilteredQuery build() {
            return new FilteredQuery(queryBag);
        }
    }
}
