package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class TypeQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public TypeQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new TypeQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .typeQueryGenerator()
                .generate(queryBag);
    }

    public static class TypeQueryBuilder
            extends Init<TypeQueryBuilder> {

        @Override
        protected TypeQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T value(String value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public TypeQuery build() throws MandatoryParametersAreMissingException {

            if(!queryBag.containsKey(Constants.VALUE)) {
                throw new MandatoryParametersAreMissingException(Constants.VALUE);
            }
            return new TypeQuery(queryBag);

        }

    }
}
