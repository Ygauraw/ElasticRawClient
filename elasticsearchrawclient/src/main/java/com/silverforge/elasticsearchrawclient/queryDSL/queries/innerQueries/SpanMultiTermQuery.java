package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.MultiTermQueryable;
import com.silverforge.elasticsearchrawclient.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanMultiTermQuery
        implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanMultiTermQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanMultiTermQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanMultiTermQueryBuilder();
    }

    public static class SpanMultiTermQueryBuilder
            extends Init<SpanMultiTermQueryBuilder> {

        @Override
        protected SpanMultiTermQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T match(MultiTermQueryable match) {
            queryBag.addItem(Constants.MATCH, match);
            return self();
        }

        public SpanMultiTermQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.MATCH)) {
                throw new MandatoryParametersAreMissingException(Constants.MATCH);
            }
            return new SpanMultiTermQuery(queryBag);
        }
    }

}
