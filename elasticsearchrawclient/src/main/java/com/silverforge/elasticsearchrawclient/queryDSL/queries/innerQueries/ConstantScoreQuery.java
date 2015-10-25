package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.BoostQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.*;

public class ConstantScoreQuery
        extends BoostQuery {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    public ConstantScoreQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    public static Init<?> builder() {
        return new ConstantScoreQueryBuilder();
    }

    @Override
    public String getQueryString() {
        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"constant_score\":{");

        List<QueryTypeItem> parentItems = stream(queryTypeBag)
            .where(i -> i.isParent())
            .toList();
        List<QueryTypeItem> nonParentItems = stream(queryTypeBag)
            .where(i -> !i.isParent())
            .toList();

        if (parentItems.size() > 0) {
            QueryTypeItem parentItem = parentItems.get(0);
            queryString
                .append("\"")
                .append(parentItem.getName())
                .append("\":")
                .append(parentItem.getValue());
        }

        if (nonParentItems.size() > 0 && parentItems.size() > 0)
            queryString.append(",");

        if (nonParentItems.size() > 0) {
            for (int i = 0; i < nonParentItems.size(); i++) {
                if (i > 0)
                    queryString.append(",");

                QueryTypeItem nonParentItem = nonParentItems.get(0);

                queryString
                    .append("\"")
                    .append(nonParentItem.getName())
                    .append("\":\"")
                    .append(nonParentItem.getValue())
                    .append("\"");
            }
        }

        queryString.append("}}");
        return queryString.toString();
    }

    public static class ConstantScoreQueryBuilder extends Init<ConstantScoreQueryBuilder> {
        @Override
        protected ConstantScoreQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends BoostQuery.BoostInit<T> {
        public T filter(Queryable queryable) {
            String value = queryable.getQueryString();
            queryTypeBag.addParentItem(Constants.FILTER, value);
            return self();
        }

        public ConstantScoreQuery build() {
            return new ConstantScoreQuery(queryTypeBag);
        }
    }
}
