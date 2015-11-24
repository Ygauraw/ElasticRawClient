package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.queryDSL.definition.SpanQueryable;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.common.BoostQuery;

public class SpanMultiTermQuery
        extends BoostQuery implements SpanQueryable {

    @Override
    public String getQueryString() {
        return null;
    }

    public static abstract class Init<T extends Init<T>> extends BoostInit<T> {

    }
}
