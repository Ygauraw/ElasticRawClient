package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.TimeZoneOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class RangeQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    RangeQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .rangeQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new RangeQueryBuilder();
    }

    public static class RangeQueryBuilder
            extends Init<RangeQueryBuilder> {

        @Override
        protected RangeQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostQuery.BoostInit<T> {

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

        // region number

        public T gte(int gte) {
            queryBag.addItem(Constants.GTE, gte);
            return self();
        }

        public T gte(float gte) {
            queryBag.addItem(Constants.GTE, gte);
            return self();
        }

        public T gt(int gt) {
            queryBag.addItem(Constants.GT, gt);
            return self();
        }

        public T gt(float gt) {
            queryBag.addItem(Constants.GT, gt);
            return self();
        }

        public T lte(int lte) {
            queryBag.addItem(Constants.LTE, lte);
            return self();
        }

        public T lte(float lte) {
            queryBag.addItem(Constants.LTE, lte);
            return self();
        }

        public T lt(int lt) {
            queryBag.addItem(Constants.LT, lt);
            return self();
        }

        public T lt(float lt) {
            queryBag.addItem(Constants.LT, lt);
            return self();
        }

        // endregion number

        // region date

        public T gte(String gte) {
            queryBag.addItem(Constants.GTE, gte);
            return self();
        }

        public T gte(Date gte, String format) {
            queryBag.addItem(Constants.GTE, gte, format);
            return self();
        }

        public T gt(String gt) {
            queryBag.addItem(Constants.GT, gt);
            return self();
        }

        public T gt(Date gt, String format) {
            queryBag.addItem(Constants.GT, gt, format);
            return self();
        }

        public T lte(Date lte, String format) {
            queryBag.addItem(Constants.LTE, lte, format);
            return self();
        }

        public T lte(String lte) {
            queryBag.addItem(Constants.LTE, lte);
            return self();
        }

        public T lt(Date lt, String format) {
            queryBag.addItem(Constants.LT, lt, format);
            return self();
        }

        public T lt(String lt) {
            queryBag.addItem(Constants.LT, lt);
            return self();
        }

        public T format(String format) {
            queryBag.addItem(Constants.FORMAT, format);
            return self();
        }

        public T timeZone(String timeZone) {
            queryBag.addItem(Constants.TIME_ZONE, timeZone);
            return self();
        }

        public T timeZone(TimeZoneOperator timeZone) {
            String value = timeZone.toString();
            queryBag.addItem(Constants.TIME_ZONE, value);
            return self();
        }

        // endregion date

        public RangeQuery build() throws MandatoryParametersAreMissingException {
            List<String> missingParams = new ArrayList<>();

            if(!queryBag.containsKey(Constants.FIELD_NAME))
                missingParams.add(Constants.FIELD_NAME);
            if(stream(missingParams).count() > 0)
                throw new MandatoryParametersAreMissingException(missingParams.toString());

            return new RangeQuery(queryBag);
        }

    }
}
