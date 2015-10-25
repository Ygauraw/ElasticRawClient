package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.AnalyzerOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzyRewriteOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.PhraseTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroTermsQueryOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.BooleanUtils;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.*;

public class MatchQuery
        extends MinimumShouldMatchQuery {

    protected QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    MatchQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public String getQueryString() {
        List<QueryTypeItem> parentItems = stream(queryTypeBag)
            .where(i -> i.isParent())
            .toList();
        List<QueryTypeItem> nonParentItems = stream(queryTypeBag)
            .where(i -> !i.isParent())
            .toList();

        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"match\":{\"");

        prepareParentItem(parentItems, queryString);
        prepareNonParentItems(nonParentItems, queryString);

        queryString.append("}}");

        return queryString.toString();
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

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.MinimumShouldMatchInit<T> {

        public T fieldName(String fieldName) {
            if (TextUtils.isEmpty(fieldName))
                return allFields();

            queryTypeBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T allFields() {
            queryTypeBag.addParentItem(Constants.FIELD_NAME, "_all");
            return self();
        }

        // region value operators

        public T value(String value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        // region integer numbers

        public T value(byte value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(short value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(int value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(long value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        // endregion

        // region float numbers

        public T value(float value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T value(double value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        // endregion

        public T value(Date value, String format) {
            queryTypeBag.addItem(Constants.VALUE, value, format);
            return self();
        }

        public T value(boolean value) {
            queryTypeBag.addItem(Constants.VALUE, value);
            return self();
        }

        // endregion

        public T analyzer(String analyzer) {
            queryTypeBag.addItem(Constants.ANALYZER, analyzer);
            return self();
        }

        public T analyzer(AnalyzerOperator analyzer) {
            String value = analyzer.toString();
            queryTypeBag.addItem(Constants.ANALYZER, value);
            return self();
        }

        public T fuzziness(FuzzinessOperator fuzzinessOperator) {
            String value = fuzzinessOperator.toString();
            queryTypeBag.addItem(Constants.FUZZINESS, value);
            return self();
        }

        public T fuzziness(String fuzzinessOperator) {
            queryTypeBag.addItem(Constants.FUZZINESS, fuzzinessOperator);
            return self();
        }

        public T fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator) {
            return fuzzyRewrite(fuzzyRewriteOperator, (byte) 1);
        }

        public T fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator, byte topN) {
            if (!queryTypeBag.containsKey(Constants.FUZZY_REWRITE)) {
                if (fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_N
                        || fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_BOOST_N) {

                    String fuzzyRewriteTop = fuzzyRewriteOperator.toString().replace("_N", "_" + topN);
                    queryTypeBag.add(QueryTypeItem
                                        .builder()
                                        .name(Constants.FUZZY_REWRITE)
                                        .value(fuzzyRewriteTop)
                                        .build());
                } else {
                    queryTypeBag.add(QueryTypeItem
                                        .builder()
                                        .name(Constants.FUZZY_REWRITE)
                                        .value(fuzzyRewriteOperator.toString())
                                        .build());
                }
            }
            return self();
        }

        public T lenient(boolean lenient) {
            queryTypeBag.addItem(Constants.LENIENT, lenient);
            return self();
        }

        public T maxExpansions(int maxExpansions) {
            queryTypeBag.addItem(Constants.MAX_EXPANSIONS, maxExpansions);
            return self();
        }

        public T operator(LogicOperator queryOperator) {
            String value = queryOperator.toString();
            queryTypeBag.addItem(Constants.OPERATOR, value);
            return self();
        }

        public T prefixLength(int prefixLength) {
            queryTypeBag.addItem(Constants.PREFIX_LENGTH, prefixLength);
            return self();
        }

        public T query(String queryExpression) {
            queryTypeBag.addItem(Constants.QUERY, queryExpression);
            return self();
        }

        public T type(PhraseTypeOperator phraseTypeOperator) {
            String value = phraseTypeOperator.toString();
            queryTypeBag.addItem(Constants.TYPE, value);
            return self();
        }

        public T zeroTermsQuery(ZeroTermsQueryOperator zeroTermsQueryOperator) {
            String value = zeroTermsQueryOperator.toString();
            queryTypeBag.addItem(Constants.ZERO_TERMS_QUERY, value);
            return self();
        }

        public MatchQuery build() {
            return new MatchQuery(queryTypeBag);
        }
    }

    private void prepareNonParentItems(List<QueryTypeItem> nonParentItems, StringBuilder queryString) {
        if (nonParentItems.size() == 0) {
            queryString.append("\"\"");
        } else if (nonParentItems.size() == 1) {
            QueryTypeItem item = nonParentItems.get(0);
            if (item.getName().toLowerCase().equals(Constants.VALUE)) {
                String value = item.getValue();
                if (value == null)
                    value = "";
                queryString.append("\"").append(value).append("\"");
            } else {
                queryString.append("\"\"");
            }
        } else {
            queryString.append("{");

            for (int i = 0; i < nonParentItems.size(); i++) {
                if (i > 0)
                    queryString.append(",");

                QueryTypeItem item = nonParentItems.get(i);
                queryString
                    .append("\"")
                    .append(item.getName())
                    .append("\":\"")
                    .append(item.getValue())
                    .append("\"");
            }

            queryString.append("}");
        }
    }

    private void prepareParentItem(List<QueryTypeItem> parentItems, StringBuilder queryString) {
        if (parentItems.size() == 0)
            queryString.append("_all");
        else {
            String value = parentItems.get(0).getValue();
            queryString.append(value);
        }
        queryString.append("\":");
    }
}
