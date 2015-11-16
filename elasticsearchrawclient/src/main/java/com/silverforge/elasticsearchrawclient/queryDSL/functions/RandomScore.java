package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.FunctionFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class RandomScore
            extends BaseFunction {

    RandomScore(QueryTypeArrayList<QueryTypeItem> queryBag) {
        super(queryBag);
    }

    @Override
    public String getFunctionString() {
        return FunctionFactory
            .randomScoreGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new RandomScoreBuilder();
    }

    public static class RandomScoreBuilder
            extends Init<RandomScoreBuilder> {

        @Override
        protected RandomScoreBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BaseFunction.Init<T> {

        public T seed(int value) {
            queryBag.addItem(Constants.SEED, value);
            return self();
        }

        public T seed(float value) {
            queryBag.addItem(Constants.SEED, value);
            return self();
        }

        public RandomScore build() {
            return new RandomScore(queryBag);
        }
    }
}
