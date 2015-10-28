package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public class ConstantScoreFactory {
    private final static String CONSTANT_SCORE_TEMPLATE = "{\"constant_score\":{\"filter\":{{INNERFILTER}}}}";

    public static MatchAllFilterGenerator matchAllFilterGenerator() {
        return new MatchAllFilterGenerator();
    }

    public final static class MatchAllFilterGenerator
            extends QueryGenerator {

        private MatchAllFilterGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            String matchAllFilter = CONSTANT_SCORE_TEMPLATE.replace("{{INNERFILTER}}", generateEmptyParent("match_all"));
            return matchAllFilter;
        }
    }
}
