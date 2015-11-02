package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.AnalyzerOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzyRewriteOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.PhraseTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroTermsQueryOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Date;

public class MatchQuery
        extends MinimumShouldMatchQuery {

    protected QueryTypeArrayList<QueryTypeItem> queryBag;

    MatchQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public String getQueryString() {
        return QueryFactory
            .matchQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
         return new MatchQueryBuilder();
    }

    public static class MatchQueryBuilder extends Init<MatchQueryBuilder> {
        @Override
        protected MatchQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends MinimumShouldMatchQuery.MinimumShouldMatchInit<T> {

        public T fieldName(String fieldName) {
            if (TextUtils.isEmpty(fieldName))
                return allFields();

            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T allFields() {
            queryBag.addParentItem(Constants.FIELD_NAME, "_all");
            return self();
        }

        // region value operators

        public T value(String value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        // region integer numbers

        public T value(byte value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(short value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(int value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(long value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        // endregion

        // region float numbers

        public T value(float value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(double value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        // endregion

        public T value(Date value, String format) {
            queryBag.addItem(Constants.VALUE, value, format);
            return self();
        }

        public T value(boolean value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        // endregion

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

        public MatchQuery build() {
            return new MatchQuery(queryBag);
        }
    }
}
