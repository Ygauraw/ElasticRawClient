package com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.definition.Sortable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.SortQueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MissingOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class Sorting
        implements Sortable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public Sorting(QueryTypeArrayList<QueryTypeItem> queryBag) {
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

        public Sorting build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            return new Sorting(queryBag);
        }
    }
}
