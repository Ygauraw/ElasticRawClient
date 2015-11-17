package com.silverforge.elasticsearchrawclient.queryDSL.params;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class Params
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    Params(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static ParamsBuilder builder() {
        return new ParamsBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .paramsGenerator()
                .generate(queryBag);
    }

    public static class ParamsBuilder
            extends Init<ParamsBuilder> {

        @Override
        protected ParamsBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T name(String name) {
            queryBag.addItem(Constants.NAME, name);
            return self();
        }

        public T value(String value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public Params build() {
            return new Params(queryBag);
        }
    }

}
