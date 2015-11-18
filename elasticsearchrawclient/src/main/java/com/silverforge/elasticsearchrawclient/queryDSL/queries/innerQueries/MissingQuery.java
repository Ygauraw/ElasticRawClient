package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class MissingQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public MissingQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new MissingQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .missingQueryGenerator()
                .generate(queryBag);
    }

    public static class MissingQueryBuilder
            extends Init<MissingQueryBuilder> {

        @Override
        protected MissingQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T field(String field) {
            queryBag.addItem(Constants.FIELD, field);
            return self();
        }

        public T existence(boolean existence) {
            queryBag.addItem(Constants.EXISTENCE, existence);
            return self();
        }

        public T nullValue(boolean nullValue) {
            queryBag.addItem(Constants.NULL_VALUE, nullValue);
            return self();
        }

        public MissingQuery build() throws MandatoryParametersAreMissingException {

            if(!queryBag.containsKey(Constants.FIELD)) {
                throw new MandatoryParametersAreMissingException(Constants.FIELD);
            }
            return new MissingQuery(queryBag);

        }

    }

}
