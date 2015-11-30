package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Functionable;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

class BaseFunction
        implements Functionable {

    protected QueryTypeArrayList<QueryTypeItem> queryBag;

    BaseFunction(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getFunctionString() {
        return null;
    }

    public static abstract class Init<T extends Init<T>> {
        protected final QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T filter(Queryable query) {
            queryBag.addItem(Constants.FILTER, query);
            return self();
        }
    }
}
