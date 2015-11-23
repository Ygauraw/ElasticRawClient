package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.*;

public class GeoPolygonQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    GeoPolygonQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geoPolygonQueryGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new GeoPolygonQueryBuilder();
    }

    public static class GeoPolygonQueryBuilder
            extends Init<GeoPolygonQueryBuilder> {

        @Override
        protected GeoPolygonQueryBuilder self() {
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

        public T points(GeoPoint... geoPoints) {
            List<String> points = stream(geoPoints)
                .select(gp ->
                    String.format("[%s,%s]",
                        Double.toString(gp.getLongitude()),
                        Double.toString(gp.getLatitude())))
                    .toList();

            String concatenatedPoints = StringUtils.makeCommaSeparatedList(points);
            queryBag.addItem(Constants.POINTS, String.format("[%s]", concatenatedPoints));
            return self();
        }

        public T points(String... geoHashes) {
            String concatenatedPoints
                = StringUtils.makeCommaSeparatedListWithQuotationMark(geoHashes);
            queryBag.addItem(Constants.POINTS, String.format("[%s]", concatenatedPoints));
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

        public GeoPolygonQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.POINTS))
                throw new MandatoryParametersAreMissingException(Constants.POINTS);

            return new GeoPolygonQuery(queryBag);
        }
    }
}
