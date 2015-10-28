package com.silverforge.elasticsearchrawclient.queryDSL.filters.innerfilters;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Filterable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.ConstantScoreFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class MatchAllFilter
        implements Filterable{

    public MatchAllFilter() {
    }

    public static MatchAllFilterBuilder builder() {
        return new MatchAllFilterBuilder();
    }

    @Override
    public String getQueryString() {
        return ConstantScoreFactory
            .matchAllFilterGenerator()
            .generate(null);
    }

    public static class MatchAllFilterBuilder extends Init<MatchAllFilterBuilder> {
    }

    public static abstract class Init<T extends Init<T>> {
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        public MatchAllFilter build() {
            return new MatchAllFilter();
        }
    }
}
