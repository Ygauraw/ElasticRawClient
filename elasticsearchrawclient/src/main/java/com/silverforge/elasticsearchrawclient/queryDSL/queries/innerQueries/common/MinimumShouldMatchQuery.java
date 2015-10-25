package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
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
        protected final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();
        protected abstract T self();

        public T minimumShouldMatch(int value) {
            queryTypeBag.addItem(Constants.MINIMUM_SHOULD_MATCH, value);
            return self();
        }

        public T minimumShouldMatchPercentage(int value) {
            queryTypeBag.addPercentageItem(Constants.MINIMUM_SHOULD_MATCH, value);
            return self();
        }

        public T minimumShouldMatchPercentage(float value) {
            queryTypeBag.addPercentageItem(Constants.MINIMUM_SHOULD_MATCH, value);
            return self();
        }

        public T minimumShouldMatchCombination(String expression) {
            queryTypeBag.addItem(Constants.MINIMUM_SHOULD_MATCH, expression);
            return self();
        }
    }
}
