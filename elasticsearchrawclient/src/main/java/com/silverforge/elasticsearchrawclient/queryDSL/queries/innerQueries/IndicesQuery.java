package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

public class IndicesQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    IndicesQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static IndicesQueryBuilder builder() {
        return new IndicesQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .indicesQueryGenerator()
            .generate(queryBag);
    }

    public static class IndicesQueryBuilder extends Init<IndicesQueryBuilder> {
        @Override
        protected IndicesQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T indices(String... indices) {
            String value = "[" + StringUtils.makeCommaSeparatedListWithQuotationMark(indices) + "]";
            queryBag.addParentItem(Constants.INDICES, value);
            return self();
        }

        public T query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T noMatchQuery(Queryable query) {
            queryBag.addItem(Constants.NO_MATCH_QUERY, query);
            return self();
        }

        public IndicesQuery build() {
            return new IndicesQuery(queryBag);
        }
    }
}
