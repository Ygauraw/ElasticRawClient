package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

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

    private final QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    MatchQuery(MatchQueryBuilder matchQueryBuilder) {
        queryTypeBag = matchQueryBuilder.queryTypeBag;
    }

    public static MatchQueryBuilder builder() {
         return new MatchQueryBuilder();
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
            if (item.getName().toLowerCase().equals(MatchQueryBuilder.VALUE)) {
                queryString.append("\"").append(item.getValue()).append("\"");
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

    public static class MatchQueryBuilder {
        private final static String FIELD_NAME = "FIELDNAME";
        private final static String VALUE = "value";
        private final static String ANALYZER = "analyzer";
        private final static String FUZZINESS = "fuzziness";
        private final static String FUZZY_REWRITE = "fuzzy_rewrite";
        private final static String MAX_EXPANSIONS = "max_expansions";
        private final static String LENIENT = "lenient";
        private final static String OPERATOR = "operator";
        private final static String PREFIX_LENGTH = "prefix_length";
        private final static String QUERY = "query";
        private final static String TYPE = "type";
        private final static String ZERO_TERMS_QUERY = "zero_terms_query";

        private final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        MatchQueryBuilder() {}

        public MatchQueryBuilder fieldName(String fieldName) {
            if (!queryTypeBag.containsKey(FIELD_NAME))
                queryTypeBag.add(QueryTypeItem.builder().name(FIELD_NAME).value(fieldName).isParent(true).build());
            return this;
        }

        public MatchQueryBuilder allFields() {
            if (!queryTypeBag.containsKey(FIELD_NAME))
                queryTypeBag.add(QueryTypeItem.builder().name(FIELD_NAME).value("_all").isParent(true).build());
            return this;
        }

        // region value operators

        public MatchQueryBuilder value(String value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value).build());
            return this;
        }

        // region integer numbers

        public MatchQueryBuilder value(Byte value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value.toString()).build());
            return this;
        }

        public MatchQueryBuilder value(Short value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value.toString()).build());
            return this;
        }

        public MatchQueryBuilder value(Integer value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value.toString()).build());
            return this;
        }

        public MatchQueryBuilder value(Long value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value.toString()).build());
            return this;
        }

        // endregion

        // region float numbers

        public MatchQueryBuilder value(Float value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value.toString()).build());
            return this;
        }

        public MatchQueryBuilder value(Double value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(value.toString()).build());
            return this;
        }

        // endregion

        public MatchQueryBuilder value(Date value, String format) {
            if (!queryTypeBag.containsKey(VALUE)) {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(formatter.format(value)).build());
            }
            return this;
        }

        public MatchQueryBuilder value(Boolean value) {
            if (!queryTypeBag.containsKey(VALUE))
                queryTypeBag.add(QueryTypeItem.builder().name(VALUE).value(BooleanUtils.booleanValue(value)).build());
            return this;
        }

        // endregion

        public MatchQueryBuilder analyzer(String analyzer) {
            if (!queryTypeBag.containsKey(ANALYZER))
                queryTypeBag.add(QueryTypeItem.builder().name(ANALYZER).value(analyzer).build());
            return this;
        }

        public MatchQueryBuilder fuzziness(FuzzinessOperator fuzzinessOperator) {
            if (!queryTypeBag.containsKey(FUZZINESS))
                queryTypeBag.add(QueryTypeItem.builder().name(FUZZINESS).value(fuzzinessOperator.toString()).build());
            return this;
        }

        public MatchQueryBuilder fuzziness(String fuzzinessOperator) {
            if (!queryTypeBag.containsKey(FUZZINESS))
                queryTypeBag.add(QueryTypeItem.builder().name(FUZZINESS).value(fuzzinessOperator).build());
            return this;
        }

        public MatchQueryBuilder fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator) {
            return fuzzyRewrite(fuzzyRewriteOperator, (byte)1);
        }

        public MatchQueryBuilder fuzzyRewrite(FuzzyRewriteOperator fuzzyRewriteOperator, byte topN) {
            if (!queryTypeBag.containsKey(FUZZY_REWRITE)) {
                if (fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_N || fuzzyRewriteOperator == FuzzyRewriteOperator.TOP_TERMS_BOOST_N) {
                    String fuzzyRewriteTop = fuzzyRewriteOperator.toString().replace("_N", "_" + topN);
                    queryTypeBag.add(QueryTypeItem.builder().name(FUZZY_REWRITE).value(fuzzyRewriteTop).build());
                } else {
                    queryTypeBag.add(QueryTypeItem.builder().name(FUZZY_REWRITE).value(fuzzyRewriteOperator.toString()).build());
                }
            }
            return this;
        }

        public MatchQueryBuilder lenient(Boolean lenient) {
            if (!queryTypeBag.containsKey(LENIENT))
                queryTypeBag.add(QueryTypeItem.builder().name(LENIENT).value(BooleanUtils.booleanValue(lenient)).build());
            return this;
        }

        public MatchQueryBuilder maxExpansions(Integer maxExpansions) {
            if (!queryTypeBag.containsKey(MAX_EXPANSIONS))
                queryTypeBag.add(QueryTypeItem.builder().name(MAX_EXPANSIONS).value(maxExpansions.toString()).build());
            return this;
        }

        public MatchQueryBuilder operator(LogicOperator queryOperator) {
            if (!queryTypeBag.containsKey(OPERATOR))
                queryTypeBag.add(QueryTypeItem.builder().name(OPERATOR).value(queryOperator.toString()).build());
            return this;
        }

        public MatchQueryBuilder prefixLength(Integer prefixLength) {
            if (!queryTypeBag.containsKey(PREFIX_LENGTH))
                queryTypeBag.add(QueryTypeItem.builder().name(PREFIX_LENGTH).value(prefixLength.toString()).build());
            return this;
        }

        public MatchQueryBuilder query(String queryExpression) {
            if (!queryTypeBag.containsKey(QUERY))
                queryTypeBag.add(QueryTypeItem.builder().name(QUERY).value(queryExpression).build());
            return this;
        }

        public MatchQueryBuilder type(PhraseTypeOperator phraseTypeOperator) {
            if (!queryTypeBag.containsKey(TYPE))
                queryTypeBag.add(QueryTypeItem.builder().name(TYPE).value(phraseTypeOperator.toString()).build());
            return this;
        }

        public MatchQueryBuilder zeroTermsQuery(ZeroTermsQueryOperator zeroTermsQueryOperator) {
            if (!queryTypeBag.containsKey(ZERO_TERMS_QUERY))
                queryTypeBag.add(QueryTypeItem.builder().name(ZERO_TERMS_QUERY).value(zeroTermsQueryOperator.toString()).build());
            return this;
        }

        public MatchQuery build() {
            return new MatchQuery(this);
        }
    }
}
