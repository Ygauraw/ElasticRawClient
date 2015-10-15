package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

public class MultiMatchQuery
    extends MatchQuery
    implements Queryable {

    MultiMatchQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        super(queryTypeBag);
    }

    @Override
    public String getQueryString() {
        return null;
    }

    public static Init<?> builder() {
        return new MultiMatchQueryBuilder();
    }

    public static class MultiMatchQueryBuilder extends Init<MultiMatchQueryBuilder> {
        @Override
        protected MultiMatchQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MatchQuery.Init<T> {
        private final static String FIELDS = "fields";

        private final QueryTypeArrayList<QueryTypeItem> queryTypeBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T fields(String[] fields) {
            if (!queryTypeBag.containsKey(FIELDS)) {
                String fieldList = StringUtils.makeCommaSeparatedList(fields);
                queryTypeBag.add(QueryTypeItem.builder().name(FIELDS).value(fieldList).build());
            }
            return self();
        }

        public MultiMatchQuery build() {
            return new MultiMatchQuery(queryTypeBag);
        }
    }

}
