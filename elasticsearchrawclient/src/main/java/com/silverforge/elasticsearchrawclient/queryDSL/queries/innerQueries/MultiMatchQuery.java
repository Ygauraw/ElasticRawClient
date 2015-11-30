package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.AnalyzerOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzyRewriteOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MultiMatchTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.PhraseTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroTermsQueryOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

public class MultiMatchQuery
        extends MinimumShouldMatchQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    MultiMatchQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .multiMatchQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new MultiMatchQueryBuilder();
    }

    public static class MultiMatchQueryBuilder
            extends Init<MultiMatchQueryBuilder> {

        @Override
        protected MultiMatchQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends MinimumShouldMatchInit<T> {

        public T fields(String... fields) {
            return fields(fields, false);
        }

        public T fields(String[] fields, boolean isEdge) {
            if (!queryBag.containsKey(Constants.FIELDS)) {
                String format;
                if (isEdge)
                    format = "[%s.edge]";
                else
                    format = "[%s]";

                String fieldList = String.format(format,
                    StringUtils.makeCommaSeparatedListWithQuotationMark(fields));

                queryBag.add(QueryTypeItem
                    .builder()
                    .name(Constants.FIELDS)
                    .value(fieldList)
                    .build());
            }
            return self();
        }

        public T useDisMax(boolean use) {
            queryBag.addItem(Constants.USE_DIS_MAX, use);
            return self();
        }

        public T tieBreaker(ZeroToOneRangeOperator tieBreakerOperator) {
            String value = tieBreakerOperator.toString();
            queryBag.addItem(Constants.TIE_BREAKER, value);
            return self();
        }

        public T type(MultiMatchTypeOperator typeOperator) {
            String value = typeOperator.toString();
            queryBag.addItem(Constants.TYPE, value);
            return self();
        }

        public T analyzer(String analyzer) {
            queryBag.addItem(Constants.ANALYZER, analyzer);
            return self();
        }

        public T analyzer(AnalyzerOperator analyzer) {
            String value = analyzer.toString();
            queryBag.addItem(Constants.ANALYZER, value);
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

        public T fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator) {
            return fuzzyRewrite(fuzzyRewriteOperator, (byte) 1);
        }

        public T fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator, byte topN) {
            if (!queryBag.containsKey(Constants.FUZZY_REWRITE)) {
                if (fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_N
                    || fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_BOOST_N) {

                    String fuzzyRewriteTop = fuzzyRewriteOperator.toString().replace("_N", "_" + topN);
                    queryBag.add(QueryTypeItem
                        .builder()
                        .name(Constants.FUZZY_REWRITE)
                        .value(fuzzyRewriteTop)
                        .build());
                } else {
                    queryBag.add(QueryTypeItem
                        .builder()
                        .name(Constants.FUZZY_REWRITE)
                        .value(fuzzyRewriteOperator.toString())
                        .build());
                }
            }
            return self();
        }

        public T lenient(boolean lenient) {
            queryBag.addItem(Constants.LENIENT, lenient);
            return self();
        }

        public T maxExpansions(int maxExpansions) {
            queryBag.addItem(Constants.MAX_EXPANSIONS, maxExpansions);
            return self();
        }

        public T operator(LogicOperator queryOperator) {
            String value = queryOperator.toString();
            queryBag.addItem(Constants.OPERATOR, value);
            return self();
        }

        public T prefixLength(int prefixLength) {
            queryBag.addItem(Constants.PREFIX_LENGTH, prefixLength);
            return self();
        }

        public T query(String queryExpression) {
            queryBag.addItem(Constants.QUERY, queryExpression);
            return self();
        }

        public T type(PhraseTypeOperator phraseTypeOperator) {
            String value = phraseTypeOperator.toString();
            queryBag.addItem(Constants.TYPE, value);
            return self();
        }

        public T zeroTermsQuery(ZeroTermsQueryOperator zeroTermsQueryOperator) {
            String value = zeroTermsQueryOperator.toString();
            queryBag.addItem(Constants.ZERO_TERMS_QUERY, value);
            return self();
        }

        public MultiMatchQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.QUERY))
                throw new MandatoryParametersAreMissingException(Constants.QUERY);

            if (!queryBag.containsKey(Constants.FIELDS))
                throw new MandatoryParametersAreMissingException(Constants.FIELDS);

            return new MultiMatchQuery(queryBag);
        }
    }
}
