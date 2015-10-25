package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

public abstract class MinimumShouldMatchQuery
        implements Queryable {

    @Override
    public String getQueryString() {
        return null;
    }

    public static abstract class MinimumShouldMatchInit<T extends MinimumShouldMatchInit<T>> {
        private final static String MINIMUM_SHOULD_MATCH = "minimum_should_match";
        protected final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();
        protected abstract T self();

        public T minimumShouldMatch(int value) {
            queryTypeBag.addItem(MINIMUM_SHOULD_MATCH, value);
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
            queryTypeBag.addItem(MINIMUM_SHOULD_MATCH, expression);
            return self();
        }
    }
}
