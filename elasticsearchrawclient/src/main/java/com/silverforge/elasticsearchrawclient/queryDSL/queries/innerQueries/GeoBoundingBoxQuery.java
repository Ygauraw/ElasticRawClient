package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.GeoBoundingBoxTypeOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class GeoBoundingBoxQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeoBoundingBoxQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geoBoundingBoxQueryGenerator()
            .generate(queryBag);
    }

    public static GeoBoundingBoxQueryBuilder builder() {
        return new GeoBoundingBoxQueryBuilder();
    }

    public static class GeoBoundingBoxQueryBuilder
            extends Init<GeoBoundingBoxQueryBuilder> {

        @Override
        protected GeoBoundingBoxQueryBuilder self() {
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

        public T topLeft(GeoPoint geoPoint) {
            queryBag.addItem(Constants.TOP_LEFT, geoPoint);
            return self();
        }

        public T topLeftGeohash(String geoHash) {
            queryBag.addItem(Constants.TOP_LEFT, geoHash);
            return self();
        }

        public T topRight(GeoPoint geoPoint) {
            queryBag.addItem(Constants.TOP_RIGHT, geoPoint);
            return self();
        }

        public T topRightGeohash(String geoHash) {
            queryBag.addItem(Constants.TOP_RIGHT, geoHash);
            return self();
        }

        public T bottomLeft(GeoPoint geoPoint) {
            queryBag.addItem(Constants.BOTTOM_LEFT, geoPoint);
            return self();
        }

        public T bottomLeftGeohash(String geoHash) {
            queryBag.addItem(Constants.BOTTOM_LEFT, geoHash);
            return self();
        }

        public T bottomRight(GeoPoint geoPoint) {
            queryBag.addItem(Constants.BOTTOM_RIGHT, geoPoint);
            return self();
        }

        public T bottomRightGeohash(String geoHash) {
            queryBag.addItem(Constants.BOTTOM_RIGHT, geoHash);
            return self();
        }

        public T type(GeoBoundingBoxTypeOperator geoBoundingBoxTypeOperator) {
            String value = geoBoundingBoxTypeOperator.toString();
            queryBag.addItem(Constants.TYPE, value);
            return self();
        }

        public GeoBoundingBoxQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            return new GeoBoundingBoxQuery(queryBag);
        }
    }
}
