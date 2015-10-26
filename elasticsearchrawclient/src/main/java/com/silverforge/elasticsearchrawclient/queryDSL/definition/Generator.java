package com.silverforge.elasticsearchrawclient.queryDSL.definition;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public interface Generator {
    String generate(QueryTypeArrayList<QueryTypeItem> queryTypeBag);
}
