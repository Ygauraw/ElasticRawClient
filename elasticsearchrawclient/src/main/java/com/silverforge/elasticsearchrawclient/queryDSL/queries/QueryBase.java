package com.silverforge.elasticsearchrawclient.queryDSL.queries;

public abstract class QueryBase {

    protected final String queryEnvelope = "{\"query\" : {{BUILTQUERYSTRING}}}";

}
