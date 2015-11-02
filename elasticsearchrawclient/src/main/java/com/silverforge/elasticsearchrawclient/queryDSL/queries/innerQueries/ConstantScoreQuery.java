package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class ConstantScoreQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public ConstantScoreQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new ConstantScoreQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .constantScoreQueryGenerator()
            .generate(queryBag);
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
            queryBag.addParentItem(Constants.FILTER, value);
            return self();
        }

        public ConstantScoreQuery build() {
            return new ConstantScoreQuery(queryBag);
        }
    }
}
