package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Date;

public class SpanFirstQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    SpanFirstQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static SpanFirstQueryBuilder builder() {
        return new SpanFirstQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .spanFirstQueryGenerator()
            .generate(queryBag);
    }

    public static class SpanFirstQueryBuilder extends Init<SpanFirstQueryBuilder> {
        @Override
        protected SpanFirstQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private final QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();


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

        public T end(int end) {
            queryBag.addItem(Constants.END, end);
            return self();
        }

        public SpanFirstQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            if (!queryBag.containsKey(Constants.END))
                queryBag.addItem(Constants.END, 3);

            return new SpanFirstQuery(queryBag);
        }
    }
}
