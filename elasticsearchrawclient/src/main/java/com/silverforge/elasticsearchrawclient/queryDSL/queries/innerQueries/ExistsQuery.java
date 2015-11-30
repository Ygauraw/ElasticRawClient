package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class ExistsQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public ExistsQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new ExistsQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .existsQueryGenerator()
                .generate(queryBag);
    }

    public static class ExistsQueryBuilder
            extends Init<ExistsQueryBuilder> {

        @Override
        protected ExistsQueryBuilder self() {
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

        public ExistsQuery build() throws MandatoryParametersAreMissingException {

            if(!queryBag.containsKey(Constants.FIELD)) {
                throw new MandatoryParametersAreMissingException(Constants.FIELD);
            }
            return new ExistsQuery(queryBag);

        }

    }

}
