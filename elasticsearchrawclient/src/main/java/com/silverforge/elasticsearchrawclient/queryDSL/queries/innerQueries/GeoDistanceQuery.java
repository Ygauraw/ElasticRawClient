package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.DistanceTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.OptimizeBboxOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.GeoDistanceBaseQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import lombok.val;

public class GeoDistanceQuery
        extends GeoDistanceBaseQuery {

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

    public static Init<?> builder() {
        return new GeoDistanceQueryBuilder();
    }

    public static class GeoDistanceQueryBuilder
            extends Init<GeoDistanceQueryBuilder> {

        @Override
        protected GeoDistanceQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends GeoInit<T> {

        public T distance(String distance) {
            queryBag.addItem(Constants.DISTANCE, distance);
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
