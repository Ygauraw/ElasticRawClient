package com.silverforge.elasticsearchrawclient.elasticFacade.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;
import com.silverforge.elasticsearchrawclient.model.BulkResultItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BulkResultParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<BulkResultItem> getResults(String bulkResponseJson) {
        ArrayList<BulkResultItem> retValue = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(bulkResponseJson);
            JSONArray items = json.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject actionItemResult = items.getJSONObject(i);

                JSONArray names = actionItemResult.names();
                if (names.length() > 0) {
                    String bulkActionType = names.getString(0);
                    JSONObject actionValue = actionItemResult.getJSONObject(bulkActionType);
                    BulkResultItem bulkResultItem = mapper.readValue(actionValue.toString(), BulkResultItem.class);
                    bulkResultItem.setOperation(OperationType.getOperationType(bulkActionType));
                    retValue.add(bulkResultItem);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return retValue;
    }
}
