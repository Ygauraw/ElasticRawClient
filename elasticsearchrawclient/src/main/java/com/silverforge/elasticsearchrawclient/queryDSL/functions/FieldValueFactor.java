package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.FunctionFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ModifierOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class FieldValueFactor
        extends BaseFunction {

    FieldValueFactor(QueryTypeArrayList<QueryTypeItem> queryBag) {
        super(queryBag);
    }

    @Override
    public String getFunctionString() {
        return FunctionFactory
            .fieldValueFactorGenerator()
            .generate(queryBag);
    }

    public static FieldValueFactorBuilder builder() {
        return new FieldValueFactorBuilder();
    }

    public static class FieldValueFactorBuilder
            extends Init<FieldValueFactorBuilder> {

        @Override
        protected FieldValueFactorBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BaseFunction.Init<T> {

        public T field(String field) {
            queryBag.addItem(Constants.FIELD, field);
            return self();
        }

        public T factor(int factor) {
            queryBag.addItem(Constants.FACTOR, factor);
            return self();
        }

        public T factor(float factor) {
            queryBag.addItem(Constants.FACTOR, factor);
            return self();
        }

        public T modifier(ModifierOperator modifier) {
            String value = modifier.toString();
            queryBag.addItem(Constants.MODIFIER, value);
            return self();
        }

        public T missing(int missing) {
            queryBag.addItem(Constants.MISSING, missing);
            return self();
        }

        public T missing(float missing) {
            queryBag.addItem(Constants.MISSING, missing);
            return self();
        }

        public FieldValueFactor build() {
            return new FieldValueFactor(queryBag);
        }
    }
}
