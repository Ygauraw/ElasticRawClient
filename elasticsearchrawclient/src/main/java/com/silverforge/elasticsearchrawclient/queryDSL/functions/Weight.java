package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.FunctionFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class Weight
        extends BaseFunction {

    Weight(QueryTypeArrayList<QueryTypeItem> queryBag) {
        super(queryBag);
    }

    public static Init<?> builder() {
        return new WeightBuilder();
    }

    @Override
    public String getFunctionString() {
        return FunctionFactory
            .weightGenerator()
            .generate(queryBag);
    }

    public static class WeightBuilder
            extends Init<WeightBuilder> {

        @Override
        protected WeightBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BaseFunction.Init<T> {

        public T weight(int value) {
            queryBag.addItem(Constants.WEIGHT, value);
            return self();
        }

        public T weight(float value) {
            queryBag.addItem(Constants.WEIGHT, value);
            return self();
        }

        public Weight build() {
            return new Weight(queryBag);
        }
    }
}
