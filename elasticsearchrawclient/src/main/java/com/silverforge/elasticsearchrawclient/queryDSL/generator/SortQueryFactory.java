package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Map;

import static br.com.zbra.androidlinq.Linq.*;

public final class SortQueryFactory {

    public static ScriptBasedSortingGenerator scriptBasedSortingGenerator() {
        return new ScriptBasedSortingGenerator();
    }

    public static GeoDistanceSortingGenerator geoDistanceSortingGenerator() {
        return new GeoDistanceSortingGenerator();
    }

    public static class GeoDistanceSortingGenerator
            extends QueryGenerator {

        private GeoDistanceSortingGenerator() {}

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            QueryTypeItem parent = stream(queryBag)
                .firstOrNull(qb -> qb.isParent());

            Map<String, String> childItems = stream(queryBag)
                .where(qb -> !qb.isParent())
                .toMap(qb -> qb.getName(), qb -> qb.getValue());

            return generateGeoDistance("_geo_distance", parent, childItems);
        }
    }

    public static class ScriptBasedSortingGenerator
            extends QueryGenerator {

        private ScriptBasedSortingGenerator() {}

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(qb -> qb.getName(), qb -> qb.getValue());

            return generateChildren("_script", childItems);
        }
    }
}
