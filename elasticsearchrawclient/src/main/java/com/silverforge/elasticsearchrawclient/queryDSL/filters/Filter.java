package com.silverforge.elasticsearchrawclient.queryDSL.filters;

import com.silverforge.elasticsearchrawclient.queryDSL.definition.ComposableFilter;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Filterable;

public class Filter
        implements Filterable, ComposableFilter {

    @Override
    public String getQueryString() {
        return null;
    }
}
