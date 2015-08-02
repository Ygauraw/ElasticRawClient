package com.silverforge.elasticsearchrawclient.elasticFacade.mappers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AliasParser {
    public static List<String> getAliasesFromJson(String indexName, String jsonString) {
        List<String> retValue = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject index = json.getJSONObject(indexName);
            JSONObject aliases = index.getJSONObject("aliases");

            for(Iterator iterator = aliases.keys(); iterator.hasNext();) {
                String key = (String) iterator.next();
                retValue.add(key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retValue;
    }
}
