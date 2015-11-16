package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public final class FunctionFactory {

    public static WeightGenerator weightGenerator() {
        return new WeightGenerator();
    }

    public static RandomScoreGenerator randomScoreGenerator() {
        return new RandomScoreGenerator();
    }

    public static class WeightGenerator
            extends QueryGenerator {

        private WeightGenerator() {}

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren(childItems);
        }
    }

    public static class RandomScoreGenerator
            extends QueryGenerator {

        private RandomScoreGenerator() {}

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("random_score", childItems);
        }
    }
}
