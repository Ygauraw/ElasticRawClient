package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanContainingQuery
        implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanContainingQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanContainingQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanContainingQueryBuilder();
    }

    public static class SpanContainingQueryBuilder
            extends Init<SpanContainingQueryBuilder> {

        @Override
        protected SpanContainingQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T little(SpanQueryable little) {
            queryBag.addItem(Constants.LITTLE, little);
            return self();
        }

        public T big(SpanQueryable big) {
            queryBag.addItem(Constants.BIG, big);
            return self();
        }

        public SpanContainingQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.LITTLE)) {
                throw new MandatoryParametersAreMissingException(Constants.LITTLE);
            }
            if(!queryBag.containsKey(Constants.BIG)) {
                throw new MandatoryParametersAreMissingException(Constants.BIG);
            }
            return new SpanContainingQuery(queryBag);
        }
    }
}
