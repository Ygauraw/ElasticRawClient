package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common;

import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.DistanceTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.OptimizeBboxOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public abstract class GeoDistanceBaseQuery
        implements Queryable {

    @Override
    public String getQueryString() {
        return null;
    }

    public static abstract class GeoInit<T extends GeoInit<T>> {
        protected QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();
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
    }
}
