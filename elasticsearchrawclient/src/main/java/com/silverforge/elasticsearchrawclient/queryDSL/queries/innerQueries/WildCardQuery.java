package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.MultiTermQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.FieldValueQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import static br.com.zbra.androidlinq.Linq.stream;

public class WildCardQuery
        extends FieldValueQuery implements MultiTermQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    WildCardQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .wildCardQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new WildCardQueryBuilder();
    }

    public static class WildCardQueryBuilder
            extends Init<WildCardQueryBuilder> {

        @Override
        protected WildCardQueryBuilder self() {
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

        public T wildcard(String wildcard) {
            queryBag.addItem(Constants.WILDCARD, wildcard);
            return self();
        }

        public WildCardQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.FIELD_NAME)) {
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);
            }
            return new WildCardQuery(queryBag);
        }

    }
}
