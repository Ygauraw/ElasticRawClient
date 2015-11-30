package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class NestedQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    NestedQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static NestedQueryBuilder builder() {
        return new NestedQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .nestedQueryGenerator()
            .generate(queryBag);
    }

    public static class NestedQueryBuilder
            extends Init<NestedQueryBuilder> {

        @Override
        protected NestedQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T path(String path) {
            queryBag.addItem(Constants.PATH, path);
            return self();
        }

        public T query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T scoreMode(ScoreModeOperator scoreMode) {
            queryBag.addItem(Constants.SCORE_MODE, scoreMode.toString());
            return self();
        }

        public NestedQuery build() throws MandatoryParametersAreMissingException {
            if (!queryBag.containsKey(Constants.PATH))
                throw new MandatoryParametersAreMissingException(Constants.PATH);

            if (!queryBag.containsKey(Constants.QUERY))
                throw new MandatoryParametersAreMissingException(Constants.QUERY);

            return new NestedQuery(queryBag);
        }
    }
}
