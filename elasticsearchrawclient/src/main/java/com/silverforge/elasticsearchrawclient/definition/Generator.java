package com.silverforge.elasticsearchrawclient.definition;

import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public interface Generator {
    String generate(QueryTypeArrayList<QueryTypeItem> queryBag);
}
