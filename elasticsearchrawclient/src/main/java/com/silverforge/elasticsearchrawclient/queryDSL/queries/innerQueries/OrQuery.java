package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class OrQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected OrQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new OrQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .orQueryGenerator()
                .generate(queryBag);
    }

    public static class OrQueryBuilder
            extends Init<OrQueryBuilder> {

        @Override
        protected OrQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T filters(Queryable first, Queryable second) {
            queryBag.addItem(Constants.FILTERS, first, second);
            return self();
        }

        public OrQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.FILTERS)) {
                throw new MandatoryParametersAreMissingException(Constants.FILTERS);
            }
            return new OrQuery(queryBag);
        }

    }

}
