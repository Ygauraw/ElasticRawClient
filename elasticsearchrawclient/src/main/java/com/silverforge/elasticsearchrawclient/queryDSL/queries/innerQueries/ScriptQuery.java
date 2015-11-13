package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Scriptable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class ScriptQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    ScriptQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static ScriptQueryBuilder builder() {
        return new ScriptQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .scriptQueryGenerator()
                .generate(queryBag);
    }

    public static class ScriptQueryBuilder extends Init<ScriptQueryBuilder> {
        @Override
        protected ScriptQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T script(Scriptable script) {
            queryBag.addItem(Constants.SCRIPT, script);
            return self();
        }

        public ScriptQuery build() {
            return new ScriptQuery(queryBag);
        }
    }
}