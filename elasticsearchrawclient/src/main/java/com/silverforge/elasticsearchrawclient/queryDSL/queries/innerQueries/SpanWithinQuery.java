package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanWithinQuery
        implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanWithinQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanWithinQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanWithinQueryBuilder();
    }

    public static class SpanWithinQueryBuilder
            extends Init<SpanWithinQueryBuilder> {

        @Override
        protected SpanWithinQueryBuilder self() {
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

        public SpanWithinQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.LITTLE)) {
                throw new MandatoryParametersAreMissingException(Constants.LITTLE);
            }
            if(!queryBag.containsKey(Constants.BIG)) {
                throw new MandatoryParametersAreMissingException(Constants.BIG);
            }
            return new SpanWithinQuery(queryBag);
        }
    }

}
