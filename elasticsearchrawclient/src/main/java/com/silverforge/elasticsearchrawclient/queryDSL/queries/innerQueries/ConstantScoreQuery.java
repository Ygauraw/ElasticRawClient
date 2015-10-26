package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class ConstantScoreQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    public ConstantScoreQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public static Init<?> builder() {
        return new ConstantScoreQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .constantScoreQueryGenerator()
            .generate(queryTypeBag);
    }

    public static class ConstantScoreQueryBuilder extends Init<ConstantScoreQueryBuilder> {
        @Override
        protected ConstantScoreQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends BoostQuery.BoostInit<T> {
        public T filter(Queryable queryable) {
            String value = queryable.getQueryString();
            queryTypeBag.addParentItem(Constants.FILTER, value);
            return self();
        }

        public ConstantScoreQuery build() {
            return new ConstantScoreQuery(queryTypeBag);
        }
    }
}
