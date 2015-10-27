package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.MultiMatchTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.Date;

public class MultiMatchQuery
        extends MatchQuery {

    MultiMatchQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        super(queryBag);
    }

    @Override
    public String getQueryString() {

        // TODO : Need a proper solution here
//        if (queryBag.hasKeys("query", "fields"))
//            throw new MandatoryParametersAreMissingException("query", "fields");

        return QueryFactory
            .multiMatchQueryGenerator()
            .generate(queryBag);
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
            return new MultiMatchQuery(queryBag);
        }
    }

}
