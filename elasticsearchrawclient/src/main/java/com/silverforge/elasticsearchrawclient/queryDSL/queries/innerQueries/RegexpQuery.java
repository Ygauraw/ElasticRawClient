package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FlagOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.TimeZoneOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class RegexpQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    RegexpQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .regexpQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new RegexpQueryBuilder();
    }

    public static class RegexpQueryBuilder
            extends Init<RegexpQueryBuilder> {

        @Override
        protected RegexpQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
            extends BoostQuery.BoostInit<T> {

        public T fieldName(String fieldName) {
            if (TextUtils.isEmpty(fieldName))
                return allFields();

            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T allFields() {
            queryBag.addParentItem(Constants.FIELD_NAME, "_all");
            return self();
        }

        public T value(String value) {
            queryBag.addItem(Constants.VALUE, value);
            return self();
        }

        public T flags(FlagOperator... flags) {
            String joinedFlags = TextUtils.join("|", flags);
            queryBag.addItem(Constants.FLAGS, joinedFlags);
            return self();
        }

        public T max_determinized_states(int max_determinized_states) {
            queryBag.addItem(Constants.MAX_DETERMINIZED_STATES, max_determinized_states);
            return self();
        }

        public RegexpQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.FIELD_NAME)) {
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);
            }
            return new RegexpQuery(queryBag);
        }

    }
}
