package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.FieldValueQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanTermQuery
        extends FieldValueQuery implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanTermQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanTermQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanTermQueryBuilder();
    }

    public static class SpanTermQueryBuilder
            extends Init<SpanTermQueryBuilder> {

        @Override
        protected SpanTermQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends FieldValueInit<T> {

        public T boost(int boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T boost(float boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T value(String value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public SpanTermQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.FIELD_NAME)) {
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);
            }
            if(!queryBag.containsKey(Constants.VALUE)) {
                throw new MandatoryParametersAreMissingException(Constants.VALUE);
            }
            return new SpanTermQuery(queryBag);
        }

    }

}
