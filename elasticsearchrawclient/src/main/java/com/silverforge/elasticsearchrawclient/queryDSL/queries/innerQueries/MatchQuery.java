package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzyRewriteOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.PhraseTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroTermsQueryOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.BooleanUtils;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MatchQuery
        implements Queryable {

    protected final QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    MatchQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public String getQueryString() {
        List<QueryTypeItem> parentItems = queryTypeBag.getParentItems();
        List<QueryTypeItem> nonParentItems = queryTypeBag.getNonParentItems();

        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"match\":{\"");

        if (parentItems.size() == 0)
            queryString.append("_all");
        else {
            String value = parentItems.get(0).getValue();
            queryString.append(value);
        }
        queryString.append("\":");

        if (nonParentItems.size() == 0) {
            queryString.append("\"\"");
        } else if (nonParentItems.size() == 1) {
            QueryTypeItem item = nonParentItems.get(0);
            if (item.getName().toLowerCase().equals(Init.VALUE)) {
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

    public static abstract class Init<T extends Init<T>> {
        private final static String FIELD_NAME = "FIELDNAME";
        private final static String VALUE = "value";
        private final static String ANALYZER = "analyzer";
        private final static String FUZZINESS = "fuzziness";
        private final static String FUZZY_REWRITE = "fuzzy_rewrite";
        private final static String MINIMUM_SHOULD_MATCH = "minimum_should_match";
        private final static String MAX_EXPANSIONS = "max_expansions";
        private final static String LENIENT = "lenient";
        private final static String OPERATOR = "operator";
        private final static String PREFIX_LENGTH = "prefix_length";
        private final static String QUERY = "query";
        private final static String TYPE = "type";
        private final static String ZERO_TERMS_QUERY = "zero_terms_query";

        protected final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T fieldName(String fieldName) {
            if (TextUtils.isEmpty(fieldName))
                return allFields();

            if (!queryTypeBag.containsKey(FIELD_NAME))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(FIELD_NAME)
                                    .value(fieldName)
                                    .isParent(true)
                                    .build());
            return self();
        }

        public T allFields() {
            if (!queryTypeBag.containsKey(FIELD_NAME))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(FIELD_NAME)
                                    .value("_all")
                                    .isParent(true)
                                    .build());
            return self();
        }

        // region value operators

        public T value(String value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value).build());
            return self();
        }

        // region integer numbers

        public T value(byte value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(Byte.toString(value)).build());
            return self();
        }

        public T value(short value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(Short.toString(value)).build());
            return self();
        }

        public T value(int value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(Integer.toString(value)).build());
            return self();
        }

        public T value(long value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(Long.toString(value)).build());
            return self();
        }

        // endregion

        // region float numbers

        public T value(float value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(Float.toString(value)).build());
            return self();
        }

        public T value(double value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(Double.toString(value)).build());
            return self();
        }

        // endregion

        public T value(Date value, String format) {
            if (!queryTypeBag.containsKey(VALUE)) {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(formatter.format(value)).build());
            }
            return self();
        }

        public T value(boolean value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(VALUE)
                                    .value(BooleanUtils.booleanValue(value))
                                    .build());
            return self();
        }

        // endregion

        public T analyzer(String analyzer) {
            if (!queryTypeBag.containsKey(ANALYZER))
                queryTypeBag.add(QueryTypeItem.builder().name(ANALYZER).value(analyzer).build());
            return self();
        }

        public T fuzziness(FuzzinessOperator fuzzinessOperator) {
            if (!queryTypeBag.containsKey(FUZZINESS))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(FUZZINESS)
                                    .value(fuzzinessOperator.toString())
                                    .build());
            return self();
        }

        public T fuzziness(String fuzzinessOperator) {
            if (!queryTypeBag.containsKey(FUZZINESS))
                queryTypeBag.add(QueryTypeItem.builder().name(FUZZINESS).value(fuzzinessOperator).build());
            return self();
        }

        public T fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator) {
            return fuzzyRewrite(fuzzyRewriteOperator, (byte)1);
        }

        public T fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator, byte topN) {
            if (!queryTypeBag.containsKey(FUZZY_REWRITE)) {
                if (fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_N
                        || fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_BOOST_N) {

                    String fuzzyRewriteTop = fuzzyRewriteOperator.toString().replace("_N", "_" + topN);
                    queryTypeBag.add(QueryTypeItem
                                        .builder()
                                        .name(FUZZY_REWRITE)
                                        .value(fuzzyRewriteTop)
                                        .build());
                } else {
                    queryTypeBag.add(QueryTypeItem
                                        .builder()
                                        .name(FUZZY_REWRITE)
                                        .value(fuzzyRewriteOperator.toString())
                                        .build());
                }
            }
            return self();
        }

        public T lenient(boolean lenient) {
            if (!queryTypeBag.containsKey(LENIENT))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(LENIENT)
                                    .value(BooleanUtils.booleanValue(lenient))
                                    .build());
            return self();
        }

        public T minimumShouldMatch(int value) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(MINIMUM_SHOULD_MATCH)
                                    .value(Integer.toString(value))
                                    .build());
            return self();
        }

        public T minimumShouldMatchPercentage(int value) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(MINIMUM_SHOULD_MATCH)
                                    .value(value + "%")
                                    .build());
            return self();
        }

        public T minimumShouldMatchPercentage(float value) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH)) {
                String percentage = (value * 100) + "%";
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(MINIMUM_SHOULD_MATCH)
                                    .value(percentage)
                                    .build());
            }
            return self();
        }

        public T minimumShouldMatchCombination(String expression) {
            if (!queryTypeBag.containsKey(MINIMUM_SHOULD_MATCH))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(MINIMUM_SHOULD_MATCH)
                                    .value(expression)
                                    .build());
            return self();
        }

        public T maxExpansions(int maxExpansions) {
            if (!queryTypeBag.containsKey(MAX_EXPANSIONS))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(MAX_EXPANSIONS)
                                    .value(Integer.toString(maxExpansions))
                                    .build());
            return self();
        }

        public T operator(LogicOperator queryOperator) {
            if (!queryTypeBag.containsKey(OPERATOR))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(OPERATOR)
                                    .value(queryOperator.toString())
                                    .build());
            return self();
        }

        public T prefixLength(int prefixLength) {
            if (!queryTypeBag.containsKey(PREFIX_LENGTH))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(PREFIX_LENGTH)
                                    .value(Integer.toString(prefixLength))
                                    .build());
            return self();
        }

        public T query(String queryExpression) {
            if (!queryTypeBag.containsKey(QUERY))
                queryTypeBag.add(QueryTypeItem.builder().name(QUERY).value(queryExpression).build());
            return self();
        }

        public T type(PhraseTypeOperator phraseTypeOperator) {
            if (!queryTypeBag.containsKey(TYPE))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(TYPE)
                                    .value(phraseTypeOperator.toString())
                                    .build());
            return self();
        }

        public T zeroTermsQuery(ZeroTermsQueryOperator zeroTermsQueryOperator) {
            if (!queryTypeBag.containsKey(ZERO_TERMS_QUERY))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(ZERO_TERMS_QUERY)
                                    .value(zeroTermsQueryOperator.toString())
                                    .build());
            return self();
        }

        public MatchQuery build() {
            return new MatchQuery(queryTypeBag);
        }
    }
}
