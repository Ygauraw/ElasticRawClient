package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Sortable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.SortQueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MissingOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.GeoDistanceSorting;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.ScriptBasedSorting;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public final class Sort
        implements Sortable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public Sort(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getSortableQuery() {
        return SortQueryFactory
            .sortGenerator()
            .generate(queryBag);
    }

    public static SortBuilder builder() {
        return new SortBuilder();
    }

    public static class SortBuilder {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        public SortBuilder fieldName(String fieldName) {
            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return this;
        }

        public SortBuilder order(SortOperator sortOperator) {
            String value = sortOperator.toString();
            queryBag.addItem(Constants.ORDER, value);
            return this;
        }

        public SortBuilder mode(SortModeOperator sortModeOperator) {
            String value = sortModeOperator.toString();
            queryBag.addItem(Constants.MODE, value);
            return this;
        }

        public SortBuilder nestedPath(String path) {
            queryBag.addItem(Constants.NESTED_PATH, path);
            return this;
        }

        public SortBuilder nestedFilter(Queryable queryable) {
            queryBag.addItem(Constants.NESTED_FILTER, queryable);
            return this;
        }

        public SortBuilder missing(MissingOperator missingOperator) {
            String value = missingOperator.toString();
            queryBag.addItem(Constants.MISSING, value);
            return this;
        }

        public SortBuilder missing(String missing) {
            queryBag.addItem(Constants.MISSING, missing);
            return this;
        }

        public SortBuilder unmappedType(String type) {
            queryBag.addItem(Constants.UNMAPPED_TYPE, type);
            return this;
        }

        public SortBuilder geoDistanceSorting(GeoDistanceSorting geoDistanceSorting) {
            queryBag.addItem(Constants._GEO_DISTANCE, geoDistanceSorting);
            return this;
        }

        public SortBuilder scriptBasedSorting(ScriptBasedSorting scriptBasedSorting) {
            queryBag.addItem(Constants._SCRIPT, scriptBasedSorting);
            return this;
        }

        public Sort build() {
            return new Sort(queryBag);
        }
    }
}
