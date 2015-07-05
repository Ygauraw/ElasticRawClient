package com.silverforge.elasticsearchrawclient.ElasticFacade.Mappers;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RawSourceMapTo<T> {

	private static final String TAG = RawSourceMapTo.class.getName();
	private ObjectMapper mapper = new ObjectMapper();

	public List<T> mapToList(String jsonSource, Class<T> typeReference) {

		List<T> retValue = new ArrayList<>();

		JSONArray hitsArray;
		JSONObject hits;
		JSONObject source;
		JSONObject json;

		try {
			json = new JSONObject(jsonSource);
			hits = json.getJSONObject("hits");
			hitsArray = hits.getJSONArray("hits");

			for (int i = 0; i < hitsArray.length(); i++) {
				JSONObject h = hitsArray.getJSONObject(i);
				source = h.getJSONObject("_source");
				T entity = mapper.readValue(source.toString(), typeReference);
				retValue.add(entity);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}

		return retValue;
	}
}
