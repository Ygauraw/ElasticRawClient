package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.commonquerytemplates.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class CommonTermsQuery
    extends MinimumShouldMatchQuery {

    protected CommonTermsQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        super(queryTypeBag);
    }

    @Override
    public String getQueryString() {
        return null;
    }

    public static CommonTermsQueryBuilder builder() {
        return new CommonTermsQueryBuilder();
    }

    public static class CommonTermsQueryBuilder extends Init<CommonTermsQueryBuilder> {
        @Override
        protected CommonTermsQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.Init<T> {

        public CommonTermsQuery build() {
            return new CommonTermsQuery(queryTypeBag);
        }
    }
}
