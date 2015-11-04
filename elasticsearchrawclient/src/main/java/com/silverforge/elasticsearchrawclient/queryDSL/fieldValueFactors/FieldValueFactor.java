package com.silverforge.elasticsearchrawclient.queryDSL.fieldValueFactors;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ModifierOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class FieldValueFactor implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;
    public FieldValueFactor(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static FieldValueFactorBuilder builder() {
        return new FieldValueFactorBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .fieldValueFactorGenerator()
                .generate(queryBag);
    }

    public static class FieldValueFactorBuilder extends Init<FieldValueFactorBuilder> {
        @Override
        protected FieldValueFactorBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T field(String field) {
            queryBag.addItem(Constants.FIELD, field);
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

        public FieldValueFactor build() {
            return new FieldValueFactor(queryBag);
        }
    }
}
