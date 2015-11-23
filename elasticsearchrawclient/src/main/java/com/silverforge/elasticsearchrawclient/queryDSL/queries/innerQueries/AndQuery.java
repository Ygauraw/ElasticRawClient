package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class AndQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected AndQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new AndQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .andQueryGenerator()
                .generate(queryBag);
    }

    public static class AndQueryBuilder
            extends Init<AndQueryBuilder> {

        @Override
        protected AndQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T filters(Queryable... filters) {
            queryBag.addItem(Constants.FILTERS, filters);
            return self();
        }

        public AndQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.FILTERS)) {
                throw new MandatoryParametersAreMissingException(Constants.FILTERS);
            }
            return new AndQuery(queryBag);
        }

    }

}
