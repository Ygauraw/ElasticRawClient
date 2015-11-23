package com.silverforge.elasticsearchrawclient.queryDSL.scripts;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Scriptable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import java.util.Map;

public class Script
        implements Scriptable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected Script(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new ScriptBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .scriptGenerator()
            .generate(queryBag);
    }

    public static class ScriptBuilder extends Init<ScriptBuilder> {
        @Override
        protected ScriptBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T lang(String lang) {
            queryBag.addItem(Constants.LANG, lang);
            return self();
        }

        public T params(Map<String, String> params) {
            queryBag.addItem(Constants.PARAMS, params);
            return self();
        }

        public T inline(String inline) {
            queryBag.addItem(Constants.INLINE, inline);
            return self();
        }

        public Script build() {
            return new Script(queryBag);
        }

    }
}
