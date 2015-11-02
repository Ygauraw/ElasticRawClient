package com.silverforge.elasticsearchrawclient.queryDSL.generator;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public final class QueryFactory {
    public static MatchQueryGenerator matchQueryGenerator() {
        return new MatchQueryGenerator();
    }

    public static MultiMatchQueryGenerator multiMatchQueryGenerator() {
        return new MultiMatchQueryGenerator();
    }

    public static BoolQueryGenerator boolQueryGenerator() {
        return new BoolQueryGenerator();
    }

    public static BoostingQueryGenerator boostingQueryGenerator() {
        return new BoostingQueryGenerator();
    }

    public static ConstantScoreQueryGenerator constantScoreQueryGenerator() {
        return new ConstantScoreQueryGenerator();
    }

    public static DisMaxQueryGenerator disMaxQueryGenerator() {
        return new DisMaxQueryGenerator();
    }

    public static FilteredQueryGenerator filteredQueryGenerator() {
        return new FilteredQueryGenerator();
    }

    public static FunctionScoreQueryGenerator functionScoreQueryGenerator() {
        return new FunctionScoreQueryGenerator();
    }

    public final static class MatchQueryGenerator
            extends QueryGenerator {

        private MatchQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .where(q -> !q.isParent())
                .toMap(q -> q.getName(), q -> q.getValue());

            QueryTypeItem parent = stream(queryBag)
                .firstOrNull(q -> q.isParent());

            return generateParentWithChildren("match", parent, childItems);
        }
    }

    public final static class MultiMatchQueryGenerator
            extends QueryGenerator {

        private MultiMatchQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("multi_match", childItems);
        }
    }

    public final static class BoolQueryGenerator
            extends QueryGenerator {

        private BoolQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("bool", childItems);
        }
    }

    public final static class BoostingQueryGenerator
            extends QueryGenerator {

        private BoostingQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("boosting", childItems);
        }
    }

    public final static class ConstantScoreQueryGenerator
            extends QueryGenerator {

        private ConstantScoreQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("constant_score", childItems);
        }
    }

    public final static class DisMaxQueryGenerator
            extends QueryGenerator {

        private DisMaxQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("dis_max", childItems);
        }
    }

    public final static class FilteredQueryGenerator
            extends QueryGenerator {

        private FilteredQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("filtered", childItems);
        }
    }

    public final static class FunctionScoreQueryGenerator
            extends QueryGenerator {

        private FunctionScoreQueryGenerator() {
        }

        @Override
        public String generate(QueryTypeArrayList<QueryTypeItem> queryBag) {
            Map<String, String> childItems = stream(queryBag)
                    .toMap(q -> q.getName(), q -> q.getValue());

            return generateChildren("function_score", childItems);
        }
    }


}
