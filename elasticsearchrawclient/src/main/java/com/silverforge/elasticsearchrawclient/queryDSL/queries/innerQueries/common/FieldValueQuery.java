package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Date;

public abstract class FieldValueQuery
        implements Queryable {

    @Override
    public String getQueryString() {
        return null;
    }

    public static abstract class FieldValueInit<T extends FieldValueInit<T>> {
        protected QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();
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
    }
}
