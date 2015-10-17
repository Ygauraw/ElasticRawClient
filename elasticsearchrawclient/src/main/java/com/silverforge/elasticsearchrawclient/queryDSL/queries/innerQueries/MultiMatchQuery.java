package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.MultiMatchTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.TieBreakerOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.BooleanUtils;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.Date;

public class MultiMatchQuery
    extends MatchQuery {

    MultiMatchQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        super(queryTypeBag);
    }

    @Override
    public String getQueryString() {

        // TODO : Need a proper solution here
//        if (queryTypeBag.hasKeys("query", "fields"))
//            throw new MandatoryParametersAreMissingException("query", "fields");

        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"multi_match\":{");

        for (int i = 0; i < queryTypeBag.size(); i++) {
            if (i > 0)
                queryString.append(",");

            QueryTypeItem item = queryTypeBag.get(i);
            queryString
                .append("\"")
                .append(item.getName())
                .append("\":");

            if (item.getValue().startsWith("[") || item.getValue().startsWith("{"))
                queryString
                    .append(item.getValue());
            else
                queryString
                    .append("\"")
                    .append(item.getValue())
                    .append("\"");
        }

        queryString.append("}}");

        return queryString.toString();
    }

    public static Init<?> builder() {
        return new MultiMatchQueryBuilder();
    }

    public static class MultiMatchQueryBuilder extends Init<MultiMatchQueryBuilder> {
        @Override
        protected MultiMatchQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MatchQuery.Init<T> {
        private final static String FIELDS = "fields";
        private final static String USE_DIS_MAX = "use_dis_max";
        private final static String TIE_BREAKER = "tie_breaker";
        private final static String TYPE = "type";

        protected abstract T self();

        public T fields(String... fields) {
            return fields(fields, false);
        }

        public T fields(String[] fields, boolean isEdge) {
            if (!queryTypeBag.containsKey(FIELDS)) {
                String format;
                if (isEdge)
                    format = "[%s.edge]";
                else
                    format = "[%s]";

                String fieldList = String.format(format,
                    StringUtils.makeCommaSeparatedListWithQuotationMark(fields));

                queryTypeBag.add(QueryTypeItem.builder().name(FIELDS).value(fieldList).build());
            }
            return self();
        }

        public T useDisMax(boolean use) {
            if (!queryTypeBag.containsKey(USE_DIS_MAX))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(USE_DIS_MAX)
                                    .value(BooleanUtils.booleanValue(use))
                                    .build());
            return self();
        }

        public T tieBreaker(TieBreakerOperator tieBreakerOperator) {
            if (!queryTypeBag.containsKey(TIE_BREAKER))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(TIE_BREAKER)
                                    .value(tieBreakerOperator.toString())
                                    .build());
            return self();
        }

        public T type(MultiMatchTypeOperator typeOperator) {
            if (!queryTypeBag.containsKey(TYPE))
                queryTypeBag.add(QueryTypeItem
                                    .builder()
                                    .name(TYPE)
                                    .value(typeOperator.toString())
                                    .build());
            return self();
        }

        @Deprecated
        public T fieldName(String fieldName) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        public T allFields() {
            throw new UnsupportedOperationException();
        }

        // region value operators

        @Deprecated
        public T value(String value) {
            throw new UnsupportedOperationException();
        }

        // region integer numbers

        @Deprecated
        public T value(Byte value) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        public T value(Short value) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        public T value(Integer value) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        public T value(Long value) {
            throw new UnsupportedOperationException();
        }

        // endregion

        // region float numbers

        @Deprecated
        public T value(Float value) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        public T value(Double value) {
            throw new UnsupportedOperationException();
        }

        // endregion

        @Deprecated
        public T value(Date value, String format) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        public T value(Boolean value) {
            throw new UnsupportedOperationException();
        }

        // endregion

        public MultiMatchQuery build() {
            return new MultiMatchQuery(queryTypeBag);
        }
    }

}
