package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MultiValueModeOperator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public abstract class DecayFunction {
    public static abstract class Init<T extends Init<T>> {
        protected final QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T fieldName(String fieldName) {
            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T origin(String origin) {
            queryBag.addItem(Constants.ORIGIN, origin);
            return self();
        }

        public T scale(String scale) {
            queryBag.addItem(Constants.SCALE, scale);
            return self();
        }

        public T offset(String offset) {
            queryBag.addItem(Constants.OFFSET, offset);
            return self();
        }

        public T decay(float decay) {
            queryBag.addItem(Constants.DECAY, decay);
            return self();
        }

        public T multiValueMode(MultiValueModeOperator multiValueModeOperator) {
            String value = multiValueModeOperator.toString();
            queryBag.addItem(Constants.MULTI_VALUE_MODE, value);
            return self();
        }
    }
}
