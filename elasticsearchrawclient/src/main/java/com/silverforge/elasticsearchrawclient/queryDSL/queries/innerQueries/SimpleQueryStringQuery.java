package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FlagOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SimpleFlagOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static br.com.zbra.androidlinq.Linq.stream;

public class SimpleQueryStringQuery
    implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public SimpleQueryStringQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new SimpleQueryStringQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .simpleQueryStringQueryGenerator()
                .generate(queryBag);
    }

    public static class SimpleQueryStringQueryBuilder extends Init<SimpleQueryStringQueryBuilder> {
        @Override
        protected SimpleQueryStringQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T query(String query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T fields(String... fields) {
            queryBag.addItemsWithParenthesis(Constants.FIELDS, fields);
            return self();
        }

        public T default_operator(LogicOperator defaultOperator) {
            String value = defaultOperator.toString();
            queryBag.addItem(Constants.DEFAULT_OPERATOR, value);
            return self();
        }

        public T analyzer(String analyzer) {
            queryBag.addItem(Constants.ANALYZER, analyzer);
            return self();
        }

        public T flags(SimpleFlagOperator... flags) {
            String joinedFlags = TextUtils.join("|", flags);
            queryBag.addItem(Constants.FLAGS, joinedFlags);
            return self();
        }

        public T lowercase_expanded_terms(boolean lowercaseExpandedTerms) {
            queryBag.addItem(Constants.LOWERCASE_EXPANDED_TERMS, lowercaseExpandedTerms);
            return self();
        }

        public T analyze_wildcard(boolean analyze_wildcard) {
            queryBag.addItem(Constants.ANALYZE_WILDCARD, analyze_wildcard);
            return self();
        }

        public T locale(Locale locale) {
            String value = locale.toString();
            queryBag.addItem(Constants.LOCALE, value);
            return self();
        }

        public T lenient(boolean lenient) {
            queryBag.addItem(Constants.LENIENT, lenient);
            return self();
        }

        public T minimum_should_match(String minimumShouldMatch) {
            queryBag.addItem(Constants.MINIMUM_SHOULD_MATCH, minimumShouldMatch);
            return self();
        }

        public SimpleQueryStringQuery build() throws MandatoryParametersAreMissingException {

            List<String> missingParams = new ArrayList<>();

            if(!queryBag.containsKey(Constants.QUERY))
                missingParams.add(Constants.QUERY);
            if(stream(missingParams).count() > 0)
                throw new MandatoryParametersAreMissingException(missingParams.toString());

            return new SimpleQueryStringQuery(queryBag);
        }
    }

}
