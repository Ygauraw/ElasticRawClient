package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class TermsQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    TermsQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .termsQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new TermsQueryBuilder();
    }

    public static class TermsQueryBuilder
            extends Init<TermsQueryBuilder> {

        @Override
        protected TermsQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        private String fieldName;

        public T fieldName(String fieldname) {
            this.fieldName = fieldname;
            return self();
        }

        public T values(String... values) {
            queryBag.addItemsWithParenthesis(fieldName, values);
            return self();
        }

        public TermsQuery build() throws MandatoryParametersAreMissingException {
            if(queryBag.isEmpty()) {
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME, Constants.VALUES);
            }
            return new TermsQuery(queryBag);
        }

    }
}
