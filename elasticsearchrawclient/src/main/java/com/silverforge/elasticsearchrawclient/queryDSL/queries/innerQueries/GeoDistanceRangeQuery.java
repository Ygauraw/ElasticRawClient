package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.GeoDistanceBaseQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class GeoDistanceRangeQuery
        extends GeoDistanceBaseQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeoDistanceRangeQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geoDistanceRangeQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new GeoDistanceRangeQueryBuilder();
    }

    public static class GeoDistanceRangeQueryBuilder
            extends Init<GeoDistanceRangeQueryBuilder> {

        @Override
        protected GeoDistanceRangeQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends GeoInit<T> {

        public T from(String distance) {
            queryBag.addItem(Constants.FROM, distance);
            return self();
        }

        public T to(String distance) {
            queryBag.addItem(Constants.TO, distance);
            return self();
        }

        public T gt(String gt) {
            queryBag.addItem(Constants.GT, gt);
            return self();
        }

        public T gte(String gte) {
            queryBag.addItem(Constants.GTE, gte);
            return self();
        }

        public T lt(String lt) {
            queryBag.addItem(Constants.LT, lt);
            return self();
        }

        public T lte(String lte) {
            queryBag.addItem(Constants.LTE, lte);
            return self();
        }

        public GeoDistanceRangeQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            return new GeoDistanceRangeQuery(queryBag);
        }
    }
}
