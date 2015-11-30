package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.Map;

public class TemplateQuery
    implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    public TemplateQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    public static Init<?> builder() {
        return new TemplateQueryBuilder();
    }

    @Override
    public String getQueryString() {
        return QueryFactory
                .templateQueryGenerator()
                .generate(queryBag);
    }

    public static class TemplateQueryBuilder
            extends Init<TemplateQueryBuilder> {

        @Override
        protected TemplateQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T inline(Queryable inline) {
            queryBag.addItem(Constants.INLINE, inline);
            return self();
        }

        public T inline(String inline) {
            queryBag.addItem(Constants.INLINE, inline);
            return self();
        }

        public T params(Map<String, String> params) {
            queryBag.addItem(Constants.PARAMS, params);
            return self();
        }

        public T file(String file) {
            queryBag.addItem(Constants.FILE, file);
            return self();
        }

        public T id(String id) {
            queryBag.addItem(Constants.ID, id);
            return self();
        }

        public TemplateQuery build() throws MandatoryParametersAreMissingException {

            if(!queryBag.containsKey(Constants.INLINE) && !queryBag.containsKey(Constants.FILE) && !queryBag.containsKey(Constants.ID)) {
                throw new MandatoryParametersAreMissingException(Constants.INLINE,Constants.FILE,Constants.ID);
            }
            return new TemplateQuery(queryBag);

        }

    }
}
