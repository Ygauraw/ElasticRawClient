package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class GeohashCellQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeohashCellQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geohashCellQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new GeohashCellQueryBuilder();
    }

    public static class GeohashCellQueryBuilder
            extends Init<GeohashCellQueryBuilder> {

        @Override
        protected GeohashCellQueryBuilder self() {
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

            String value
                = String.format("{\"lat\":%s,\"lon\":%s}",
                    Double.toString(geoPoint.getLatitude()),
                    Double.toString(geoPoint.getLongitude()));

            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T precision(String precision) {
            queryBag.addItem(Constants.PRECISION, precision);
            return self();
        }

        public T precision(int precision) {
            queryBag.addItem(Constants.PRECISION, precision);
            return self();
        }

        public T precision(float precision) {
            queryBag.addItem(Constants.PRECISION, precision);
            return self();
        }

        public T neighbors(boolean neighbors) {
            queryBag.addItem(Constants.NEIGHBORS, neighbors);
            return self();
        }

        public GeohashCellQuery build() throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            return new GeohashCellQuery(queryBag);
        }
    }
}
