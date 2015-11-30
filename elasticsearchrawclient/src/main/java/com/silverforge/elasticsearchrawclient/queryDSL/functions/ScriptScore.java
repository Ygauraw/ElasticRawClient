package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Scriptable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.FunctionFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class ScriptScore
        extends BaseFunction {

    ScriptScore(QueryTypeArrayList<QueryTypeItem> queryBag) {
        super(queryBag);
    }

    public static Init<?> builder() {
        return new ScriptScoreBuilder();
    }

    @Override
    public String getFunctionString() {
        return FunctionFactory
            .scriptScoreGenerator()
            .generate(queryBag);
    }

    public static class ScriptScoreBuilder
            extends Init<ScriptScoreBuilder> {

        @Override
        protected ScriptScoreBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>>
        extends BaseFunction.Init<T> {

        public T script(Scriptable scriptable) {
            queryBag.addItem(Constants.SCRIPT, scriptable);
            return self();
        }

        public ScriptScore build() {
            return new ScriptScore(queryBag);
        }
    }
}
