package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.AnalyzerOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.TimeZoneOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class QueryStringQuery
        extends MinimumShouldMatchQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    QueryStringQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .queryStringQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new QueryStringQueryBuilder();
    }

    public static class QueryStringQueryBuilder
            extends Init<QueryStringQueryBuilder> {

        @Override
        protected QueryStringQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends MinimumShouldMatchInit<T> {

        public T defaultField(String fieldName) {
            queryBag.addItem(Constants.DEFAULT_FIELD, fieldName);
            return self();
        }

        public T query(String expression) {
            queryBag.addItem(Constants.QUERY, expression);
            return self();
        }

        public T defaultOperator(LogicOperator logicOperator) {
            queryBag.addItem(Constants.DEFAULT_OPERATOR, logicOperator.toString());
            return self();
        }

        public T analyzer(AnalyzerOperator analyzerOperator) {
            queryBag.addItem(Constants.ANALYZER, analyzerOperator.toString());
            return self();
        }

        public T analyzer(String analyzer) {
            queryBag.addItem(Constants.ANALYZER, analyzer);
            return self();
        }

        public T allowLeadingWildcard(boolean allow) {
            queryBag.addItem(Constants.ALLOW_LEADING_WILDCARD, allow);
            return self();
        }

        public T lowercaseExpandedTerms(boolean lowerCase) {
            queryBag.addItem(Constants.LOWERCASE_EXPANDED_TERMS, lowerCase);
            return self();
        }

        public T enablePositionIncrements(boolean enable) {
            queryBag.addItem(Constants.ENABLE_POSITION_INCREMENTS, enable);
            return self();
        }

        public T fuzzyMaxExpansions(int max) {
            queryBag.addItem(Constants.FUZZY_MAX_EXPANSIONS, max);
            return self();
        }

        public T fuzzyMaxExpansions(float max) {
            queryBag.addItem(Constants.FUZZY_MAX_EXPANSIONS, max);
            return self();
        }

        public T fuzziness(FuzzinessOperator fuzzinessOperator) {
            String value = fuzzinessOperator.toString();
            queryBag.addItem(Constants.FUZZINESS, value);
            return self();
        }

        public T fuzziness(String fuzzinessOperator) {
            queryBag.addItem(Constants.FUZZINESS, fuzzinessOperator);
            return self();
        }

        public T fuzzyPrefixLength(int length) {
            queryBag.addItem(Constants.FUZZY_PREFIX_LENGTH, length);
            return self();
        }

        public T fuzzyPrefixLength(float length) {
            queryBag.addItem(Constants.FUZZY_PREFIX_LENGTH, length);
            return self();
        }

        public T phraseSlop(int slop) {
            queryBag.addItem(Constants.PHRASE_SLOP, slop);
            return self();
        }

        public T phraseSlop(float slop) {
            queryBag.addItem(Constants.PHRASE_SLOP, slop);
            return self();
        }

        public T boost(int boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T boost(float boost) {
            queryBag.addItem(Constants.BOOST, boost);
            return self();
        }

        public T analyzeWildcard(boolean analyze) {
            queryBag.addItem(Constants.ANALYZE_WILDCARD, analyze);
            return self();
        }

        public T autoGeneratePhraseQueries(boolean auto) {
            queryBag.addItem(Constants.AUTO_GENERATE_PHRASE_QUERIES, auto);
            return self();
        }

        public T maxDeterminizedStates(int max) {
            queryBag.addItem(Constants.MAX_DETERMINIZED_STATES, max);
            return self();
        }

        public T maxDeterminizedStates(float max) {
            queryBag.addItem(Constants.MAX_DETERMINIZED_STATES, max);
            return self();
        }

        public T lenient(boolean lenient) {
            queryBag.addItem(Constants.LENIENT, lenient);
            return self();
        }

        public T locale(String locale) {
            queryBag.addItem(Constants.LOCALE, locale);
            return self();
        }

        public T timeZone(String timeZone) {
            queryBag.addItem(Constants.TIME_ZONE, timeZone);
            return self();
        }

        public T timeZone(TimeZoneOperator timeZone) {
            String value = timeZone.toString();
            queryBag.addItem(Constants.TIME_ZONE, value);
            return self();
        }

        public T fields(String... fields) {
            queryBag.addItemsWithParenthesis(Constants.FIELDS, fields);
            return self();
        }

        public T useDisMax(boolean use) {
            queryBag.addItem(Constants.USE_DIS_MAX, use);
            return self();
        }

        public QueryStringQuery build() throws MandatoryParametersAreMissingException {
            if (!queryBag.containsKey(Constants.DEFAULT_FIELD)
                    && !queryBag.containsKey(Constants.FIELDS))
                throw new MandatoryParametersAreMissingException(Constants.DEFAULT_FIELD, Constants.FIELDS);

            if (!queryBag.containsKey(Constants.QUERY))
                throw new MandatoryParametersAreMissingException(Constants.QUERY);

            return new QueryStringQuery(queryBag);
        }
    }
}
