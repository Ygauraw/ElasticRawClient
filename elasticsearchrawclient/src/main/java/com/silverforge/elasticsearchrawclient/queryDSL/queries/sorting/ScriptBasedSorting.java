package com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Scriptable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Sortable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class ScriptBasedSorting
        implements Sortable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    ScriptBasedSorting(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getSortableQuery() {
        return null;
    }

    public static class ScriptBasedSortingBuilder {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        public ScriptBasedSortingBuilder script(Scriptable script) {
            queryBag.addItem(Constants.SCRIPT, script);
            return this;
        }

        public ScriptBasedSortingBuilder type(String type) {
            queryBag.addItem(Constants.TYPE, type);
            return this;
        }

        public ScriptBasedSortingBuilder order(SortOperator sortOperator) {
            String value = sortOperator.toString();
            queryBag.addItem(Constants.ORDER, value);
            return this;
        }

        public ScriptBasedSorting build() {
            return new ScriptBasedSorting(queryBag);
        }
    }
}
