package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanNotQuery
    implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanNotQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanNotQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanNotQueryBuilder();
    }

    public static class SpanNotQueryBuilder
            extends Init<SpanNotQueryBuilder> {

        @Override
        protected SpanNotQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T include(SpanQueryable include) {
            queryBag.addItem(Constants.INCLUDE, include);
            return self();
        }

        public T exclude(SpanQueryable exclude) {
            queryBag.addItem(Constants.EXCLUDE, exclude);
            return self();
        }

        public SpanNotQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.INCLUDE)) {
                throw new MandatoryParametersAreMissingException(Constants.INCLUDE);
            }
            if(!queryBag.containsKey(Constants.EXCLUDE)) {
                throw new MandatoryParametersAreMissingException(Constants.EXCLUDE);
            }
            return new SpanNotQuery(queryBag);
        }

    }
}
