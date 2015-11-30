package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanOrQuery
        implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanOrQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanOrQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanOrQueryBuilder();
    }

    public static class SpanOrQueryBuilder
            extends Init<SpanOrQueryBuilder> {

        @Override
        protected SpanOrQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T clauses(SpanQueryable... clauses) {
            queryBag.addItem(Constants.CLAUSES, clauses);
            return self();
        }

        public SpanOrQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.CLAUSES)) {
                throw new MandatoryParametersAreMissingException(Constants.CLAUSES);
            }
            return new SpanOrQuery(queryBag);
        }

    }
}
