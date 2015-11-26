package com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Sortable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.SortQueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.DistanceTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class GeoDistanceSorting
        implements Sortable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeoDistanceSorting(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static GeoDistanceSortingBuilder builder() {
        return new GeoDistanceSortingBuilder();
    }

    @Override
    public String getSortableQuery() {
        return SortQueryFactory
            .geoDistanceSortingGenerator()
            .generate(queryBag);
    }

    public static class GeoDistanceSortingBuilder {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();


        public GeoDistanceSortingBuilder fieldName(String fieldName) {
            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return this;
        }

        public GeoDistanceSortingBuilder location(GeoPoint geoPoint) {
            queryBag.addItem(Constants.VALUE, geoPoint);
            return this;
        }

        public GeoDistanceSortingBuilder locationGeohash(String geohash) {
            queryBag.addItem(Constants.VALUE, geohash);
            return this;
        }

        public GeoDistanceSortingBuilder locations(GeoPoint... geoPoints) {
            queryBag.addItem(Constants.VALUE, geoPoints);
            return this;
        }

        public GeoDistanceSortingBuilder locationGeohashes(String... geohash) {
            queryBag.addItemsWithParenthesis(Constants.VALUE, geohash);
            return this;
        }

        public GeoDistanceSortingBuilder order(SortOperator sortOperator) {
            String value = sortOperator.toString();
            queryBag.addItem(Constants.ORDER, value);
            return this;
        }

        public GeoDistanceSortingBuilder mode(SortModeOperator sortModeOperator) {
            String value = sortModeOperator.toString();
            queryBag.addItem(Constants.MODE, value);
            return this;
        }

        public GeoDistanceSortingBuilder unit(String unit) {
            queryBag.addItem(Constants.UNIT, unit);
            return this;
        }

        public GeoDistanceSortingBuilder distanceType(DistanceTypeOperator distanceTypeOperator) {
            String value = distanceTypeOperator.toString();
            queryBag.addItem(Constants.DISTANCE_TYPE, value);
            return this;
        }

        public GeoDistanceSorting build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.VALUE))
                throw new MandatoryParametersAreMissingException(Constants.VALUE);

            return new GeoDistanceSorting(queryBag);
        }
    }
}
