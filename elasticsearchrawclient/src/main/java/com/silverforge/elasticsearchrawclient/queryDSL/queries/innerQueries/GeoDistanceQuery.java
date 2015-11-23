package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.DistanceTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.OptimizeBboxOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import lombok.val;

public class GeoDistanceQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeoDistanceQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geoDistanceQueryGenerator()
            .generate(queryBag);
    }

    public static GeoDistanceQueryBuilder builder() {
        return new GeoDistanceQueryBuilder();
    }

    public static class GeoDistanceQueryBuilder
            extends Init<GeoDistanceQueryBuilder> {

        @Override
        protected GeoDistanceQueryBuilder self() {
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

        public T distance(String distance) {
            queryBag.addItem(Constants.DISTANCE, distance);
            return self();
        }

        public T distanceType(DistanceTypeOperator distanceTypeOperator) {
            String value = distanceTypeOperator.toString();
            queryBag.addItem(Constants.DISTANCE_TYPE, value);
            return self();
        }

        public T optimizeBbox(OptimizeBboxOperator optimizeBboxOperator) {
            String value = optimizeBboxOperator.toString();
            queryBag.addItem(Constants.OPTIMIZE_BBOX, value);
            return self();
        }

        public T queryName(String name) {
            queryBag.addItem(Constants._NAME, name);
            return self();
        }

        public T coerce(boolean coerce) {
            queryBag.addItem(Constants.COERCE, coerce);
            return self();
        }

        public T ignoreMalformed(boolean ignore) {
            queryBag.addItem(Constants.IGNORE_MALFORMED, ignore);
            return self();
        }

        public GeoDistanceQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            return new GeoDistanceQuery(queryBag);
        }
    }
}
