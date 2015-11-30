package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class NotQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected NotQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new NotQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .notQueryGenerator()
                .generate(queryBag);
    }

    public static class NotQueryBuilder
            extends Init<NotQueryBuilder> {

        @Override
        protected NotQueryBuilder self() {
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

        public NotQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.QUERY)) {
                throw new MandatoryParametersAreMissingException(Constants.QUERY);
            }
            return new NotQuery(queryBag);
        }

    }

}
