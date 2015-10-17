package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.commonquerytemplates;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class MinimumShouldMatchQuery
    implements Queryable{

    protected final QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    protected MinimumShouldMatchQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    public class MinimumShouldMatchQueryBuilder extends Init<MinimumShouldMatchQueryBuilder> {
        @Override
        protected MinimumShouldMatchQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private final static String MINIMUM_SHOULD_MATCH = "minimum_should_match";

        protected final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        protected abstract T self();


        public T minimumShouldMatch(int value) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(MINIMUM_SHOULD_MATCH)
                    .value(Integer.toString(value))
                    .build());
            return self();
        }

        public T minimumShouldMatchPercentage(int value) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(MINIMUM_SHOULD_MATCH)
                    .value(value + "%")
                    .build());
            return self();
        }

        public T minimumShouldMatchPercentage(float value) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH)) {
                String percentage = (value * 100) + "%";
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(MINIMUM_SHOULD_MATCH)
                    .value(percentage)
                    .build());
            }
            return self();
        }

        public T minimumShouldMatchCombination(String expression) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(MINIMUM_SHOULD_MATCH)
                    .value(expression)
                    .build());
            return self();
        }

        public MinimumShouldMatchQuery build() {
            return new MinimumShouldMatchQuery(queryTypeBag);
        }
    }
}
