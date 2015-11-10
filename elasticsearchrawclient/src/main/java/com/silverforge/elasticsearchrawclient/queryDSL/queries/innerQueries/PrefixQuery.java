package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Date;

public class PrefixQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    PrefixQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new PrefixQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .prefixQueryGenerator()
            .generate(queryBag);
    }

    public static class PrefixQueryBuilder extends Init<PrefixQueryBuilder> {
        @Override
        protected PrefixQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends BoostQuery.BoostInit<T> {

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

        public PrefixQuery build() throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            return new PrefixQuery(queryBag);
        }
    }
}
