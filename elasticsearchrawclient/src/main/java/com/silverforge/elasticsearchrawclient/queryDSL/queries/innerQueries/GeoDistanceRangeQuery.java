package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class GeoDistanceRangeQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeoDistanceRangeQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geoDistanceQueryGenerator()
            .generate(queryBag);
    }

    public static GeoDistanceRangeQueryBuilder builder() {
        return new GeoDistanceRangeQueryBuilder();
    }

    public static class GeoDistanceRangeQueryBuilder
            extends Init<GeoDistanceRangeQueryBuilder> {

        @Override
        protected GeoDistanceRangeQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T fieldName(String fieldName) {
            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T location(GeoPoint geoPoint) {
            queryBag.addItem(Constants.VALUE, geoPoint);
            return self();
        }

        public T locationGeohash(String geohash) {
            queryBag.addItem(Constants.VALUE, geohash);
            return self();
        }

        public T from(String distance) {
            queryBag.addItem(Constants.FROM, distance);
            return self();
        }

        public T to(String distance) {
            queryBag.addItem(Constants.TO, distance);
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
