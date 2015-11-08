package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.GeoShapeTypeOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.*;

public class GeoShapeQuery
    implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    GeoShapeQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .geoShapeQueryGenerator()
            .generate(queryBag);
    }

    public static GeoShapeQueryBuilder builder() {
        return new GeoShapeQueryBuilder();
    }

    public static class GeoShapeQueryBuilder extends Init<GeoShapeQueryBuilder> {
        @Override
        protected GeoShapeQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

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

        public T type(GeoShapeTypeOperator geoShapeTypeOperator) {
            String value = geoShapeTypeOperator.toString();
            queryBag.addItem(Constants.TYPE, value);
            return self();
        }

        public T coordinates(GeoPoint geoPoint) {
            String value = String.format("[%s,%s]", geoPoint.getLongitude(), geoPoint.getLatitude());
            queryBag.addItem(Constants.COORDINATES, value);
            return self();
        }

        public T coordinates(List<GeoPoint> coordinates) {
            Optional<List<GeoPoint>> coordinateItems = Optional.fromNullable(coordinates);
            List<GeoPoint> coordinateList = coordinateItems.or(new ArrayList<>());

            List<String> geoPointStrings = stream(coordinateList)
                .select(c -> {
                    String value = String.format("[%s,%s]", c.getLongitude(), c.getLatitude());
                    return value;
                })
                .toList();

            String value = "[" + StringUtils.makeCommaSeparatedList(geoPointStrings) + "]";
            queryBag.addItem(Constants.COORDINATES, value);
            return self();
        }

        public T multiCoordinates(List<List<GeoPoint>> coordinates) {
            Optional<List<List<GeoPoint>>> coordinateItems = Optional.fromNullable(coordinates);
            List<List<GeoPoint>> coordinateList = coordinateItems.or(new ArrayList<>(new ArrayList<>()));

            List<String> geoPointStrings = stream(coordinateList)
                .select(outerlist -> {
                    List<String> geoList = stream(outerlist)
                        .select(c -> {
                            String value = String.format("[%s,%s]", c.getLongitude(), c.getLatitude());
                            return value;
                        })
                        .toList();

                    String retValue = "[" + StringUtils.makeCommaSeparatedList(geoList) + "]";
                    return retValue;
                })
                .toList();

            String value = "[" + StringUtils.makeCommaSeparatedList(geoPointStrings) + "]";
            queryBag.addItem(Constants.COORDINATES, value);
            return self();
        }

        public T multiPolygonCoordinates(List<List<List<GeoPoint>>> coordinates) {
            Optional<List<List<List<GeoPoint>>>> coordinateItems = Optional.fromNullable(coordinates);
            List<List<List<GeoPoint>>> hyperCoordinateList = coordinateItems.or(new ArrayList<>(new ArrayList<>(new ArrayList())));

            List<String> insaneList = stream(hyperCoordinateList)
                .select(hc -> {
                    List<String> hyperList = stream(hc)
                        .select(mc -> {
                            List<String> multiList = stream(mc)
                                .select(c -> {
                                    String value = String.format("[%s,%s]", c.getLongitude(), c.getLatitude());
                                    return value;
                                })
                                .toList();

                            String value = "[" + StringUtils.makeCommaSeparatedList(multiList) + "]";
                            return value;
                        })
                        .toList();

                    String value = "[" + StringUtils.makeCommaSeparatedList(hyperList) + "]";
                    return value;
                })
                .toList();

            String retValue = "[" + StringUtils.makeCommaSeparatedList(insaneList) + "]";
            queryBag.addItem(Constants.COORDINATES, retValue);
            return self();
        }

        public T indexedShape() {
            queryBag.addItem(Constants.INDEXED_SHAPE, Constants.INDEXED_SHAPE);
            return self();
        }

        public T id(String id) {
            queryBag.addItem(Constants.ID, id);
            return self();
        }

        public T type(String type) {
            queryBag.addItem(Constants.TYPE, type);
            return self();
        }

        public T index(String index) {
            queryBag.addItem(Constants.INDEX, index);
            return self();
        }

        public T path(String path) {
            queryBag.addItem(Constants.PATH, path);
            return self();
        }

        public GeoShapeQuery build() throws MandatoryParametersAreMissingException {
            if (queryBag.containsKey(Constants.INDEXED_SHAPE)) {
                int count = stream(queryBag)
                    .where(q ->
                           q.getName().equals(Constants.ID)
                        || q.getName().equals(Constants.TYPE)
                        || q.getName().equals(Constants.INDEX)
                        || q.getName().equals(Constants.PATH))
                    .count();

                if (count < 4)
                    throw new MandatoryParametersAreMissingException(
                        Constants.ID,
                        Constants.TYPE,
                        Constants.INDEX,
                        Constants.PATH);
            }

            return new GeoShapeQuery(queryBag);
        }
    }
}
