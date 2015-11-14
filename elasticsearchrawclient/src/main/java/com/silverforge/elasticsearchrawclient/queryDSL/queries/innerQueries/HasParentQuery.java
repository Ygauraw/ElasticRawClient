package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.ArrayList;
import java.util.List;

public class HasParentQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected HasParentQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new HasParentQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .hasParentQueryGenerator()
            .generate(queryBag);
    }

    public static class HasParentQueryBuilder
            extends Init<HasParentQueryBuilder> {

        @Override
        protected HasParentQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostQuery.BoostInit<T> {

        public T query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T parent_type(String parent_type) {
            queryBag.addItem(Constants.PARENT_TYPE, parent_type);
            return self();
        }

        public T scoreMode(ScoreModeOperator scoreModeOperator) {
            String value = scoreModeOperator.toString();
            queryBag.addItem(Constants.SCORE_MODE, value);
            return self();
        }

        public HasParentQuery build() throws MandatoryParametersAreMissingException {
            List<String> missingParams = new ArrayList<>();
            boolean hasMissingParams = false;

            if(!queryBag.containsKey(Constants.PARENT_TYPE)) {
                hasMissingParams = true;
                missingParams.add(Constants.PARENT_TYPE);
            }
            if(!queryBag.containsKey(Constants.QUERY)) {
                hasMissingParams = true;
                missingParams.add(Constants.QUERY);
            }

            if(hasMissingParams) {
                throw new MandatoryParametersAreMissingException(missingParams.toString());
            }
            return new HasParentQuery(queryBag);
        }
    }
}
