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

import static br.com.zbra.androidlinq.Linq.*;

public class HasChildQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected HasChildQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new HasChildQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .hasChildQueryGenerator()
            .generate(queryBag);
    }

    public static class HasChildQueryBuilder
            extends Init<HasChildQueryBuilder> {

        @Override
        protected HasChildQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostQuery.BoostInit<T> {

        public T query(Queryable query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T type(String type) {
            queryBag.addItem(Constants.TYPE, type);
            return self();
        }

        public T scoreMode(ScoreModeOperator scoreModeOperator) {
            String value = scoreModeOperator.toString();
            queryBag.addItem(Constants.SCORE_MODE, value);
            return self();
        }

        public T minChildren(int minChildren) {
            queryBag.addItem(Constants.MIN_CHILDREN, minChildren);
            return self();
        }

        public T maxChildren(int maxChildren) {
            queryBag.addItem(Constants.MAX_CHILDREN, maxChildren);
            return self();
        }

        public HasChildQuery build() throws MandatoryParametersAreMissingException {
            List<String> missingParams = new ArrayList<>();

            if(!queryBag.containsKey(Constants.TYPE)) {
                missingParams.add(Constants.TYPE);
            }
            if(!queryBag.containsKey(Constants.QUERY)) {
                missingParams.add(Constants.QUERY);
            }
            if(stream(missingParams).count() > 0) {
                throw new MandatoryParametersAreMissingException(missingParams.toString());
            }
            return new HasChildQuery(queryBag);
        }
    }
}
