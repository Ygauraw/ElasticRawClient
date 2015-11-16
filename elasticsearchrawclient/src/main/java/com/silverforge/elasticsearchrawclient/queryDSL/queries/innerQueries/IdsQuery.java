package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.ArrayList;
import java.util.List;

public class IdsQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected IdsQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new IdsQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .idsQueryGenerator()
            .generate(queryBag);
    }

    public static class IdsQueryBuilder
            extends Init<IdsQueryBuilder> {

        @Override
        protected IdsQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostQuery.BoostInit<T> {

        public T type(String type) {
            queryBag.addItem(Constants.TYPE, type);
            return self();
        }

        public T values(String... id) {
            queryBag.addItemsWithParenthesis(Constants.VALUES, id);
            return self();
        }

        public IdsQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.VALUES)) {
                throw new MandatoryParametersAreMissingException(Constants.VALUES);
            }
            return new IdsQuery(queryBag);
        }

    }
}
