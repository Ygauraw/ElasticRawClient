package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class SpanNearQuery
        implements SpanQueryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    SpanNearQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .spanNearQueryGenerator()
                .generate(queryBag);
    }

    public static Init<?> builder() {
        return new SpanNearQueryBuilder();
    }

    public static class SpanNearQueryBuilder
            extends Init<SpanNearQueryBuilder> {

        @Override
        protected SpanNearQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {

        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T clauses(SpanQueryable... clauses) {
            queryBag.addItem(Constants.CLAUSES, clauses);
            return self();
        }

        public T slop(int slop) {
            queryBag.addItem(Constants.SLOP, slop);
            return self();
        }

        public T in_order(boolean inOrder) {
            queryBag.addItem(Constants.IN_ORDER, inOrder);
            return self();
        }

        public T collect_payloads(boolean collectPayloads) {
            queryBag.addItem(Constants.COLLECT_PAYLOADS, collectPayloads);
            return self();
        }

        public SpanNearQuery build() throws MandatoryParametersAreMissingException {
            if(!queryBag.containsKey(Constants.CLAUSES)) {
                throw new MandatoryParametersAreMissingException(Constants.CLAUSES);
            }
            return new SpanNearQuery(queryBag);
        }

    }

}
