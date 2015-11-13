package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.FieldValueFactorable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Functionable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Scriptable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class Function
        implements Functionable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;
    public Function(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static FunctionBuilder builder() {
        return new FunctionBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .functionGenerator()
                .generate(queryBag);
    }

    public static class FunctionBuilder extends Init<FunctionBuilder> {
        @Override
        protected FunctionBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T weight(int weight) {
            queryBag.addItem(Constants.WEIGHT, weight);
            return self();
        }

        public T weight(float weight) {
            queryBag.addItem(Constants.WEIGHT, weight);
            return self();
        }

        public T scriptScore(String script) {
            queryBag.addItem(Constants.SCRIPT_SCORE, script);
            return self();
        }

        public T scriptScore(Scriptable script) {
            queryBag.addItem(Constants.SCRIPT_SCORE, script);
            return self();
        }

        public T randomScore(int seed) {
            queryBag.addItem(Constants.SEED, seed);
            return self();
        }

        public T randomScore(float seed) {
            queryBag.addItem(Constants.SEED, seed);
            return self();
        }

        public T randomScore() {
            queryBag.addItem(Constants.SEED, "");
            return self();
        }

        public T fieldValueFactor(FieldValueFactorable fieldValueFactor) {
            queryBag.addItem(Constants.FIELD_VALUE_FACTOR, fieldValueFactor);
            return self();
        }

        public Function build() {
            return new Function(queryBag);
        }
    }
}
