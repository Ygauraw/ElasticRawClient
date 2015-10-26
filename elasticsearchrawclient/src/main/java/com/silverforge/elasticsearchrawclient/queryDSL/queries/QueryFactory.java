package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryGenerator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public final class QueryFactory {
    public static MatchQueryGenerator matchQueryGenerator() {
        return new MatchQueryGenerator();
    }

    public final static class MatchQueryGenerator
            extends QueryGenerator {

        private MatchQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
            Map<String, String> childItems = stream(queryTypeBag)
                .where(q -> !q.isParent())
                .toMap(q -> q.getName(), q -> q.getValue());

            QueryTypeItem parent = stream(queryTypeBag)
                .firstOrNull(q -> q.isParent());

            return generateParentWithChildren("match", parent, childItems);
        }
    }
}
