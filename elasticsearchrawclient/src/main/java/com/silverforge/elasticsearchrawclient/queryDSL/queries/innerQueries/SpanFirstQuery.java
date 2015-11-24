package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.FieldValueQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanFirstQuery
        extends FieldValueQuery
        implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    SpanFirstQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static SpanFirstQueryBuilder builder() {
        return new SpanFirstQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .spanFirstQueryGenerator()
            .generate(queryBag);
    }

    public static class SpanFirstQueryBuilder
            extends Init<SpanFirstQueryBuilder> {

        @Override
        protected SpanFirstQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends FieldValueInit<T> {

        public T end(int end) {
            queryBag.addItem(Constants.END, end);
            return self();
        }

        public SpanFirstQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            if (!queryBag.containsKey(Constants.END))
                queryBag.addItem(Constants.END, 3);

            return new SpanFirstQuery(queryBag);
        }
    }
}
