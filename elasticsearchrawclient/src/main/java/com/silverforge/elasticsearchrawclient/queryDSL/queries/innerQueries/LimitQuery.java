package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class LimitQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected LimitQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new LimitQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .limitQueryGenerator()
                .generate(queryBag);
    }

    public static class LimitQueryBuilder
            extends Init<LimitQueryBuilder> {

        @Override
        protected LimitQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T value(int value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public LimitQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.VALUE)) {
                throw new MandatoryParametersAreMissingException(Constants.VALUE);
            }
            return new LimitQuery(queryBag);
        }

    }

}
