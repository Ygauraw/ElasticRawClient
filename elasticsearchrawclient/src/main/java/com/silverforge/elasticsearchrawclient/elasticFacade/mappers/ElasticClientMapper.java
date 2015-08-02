package com.silverforge.elasticsearchrawclient.elasticFacade.mappers;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticClientMapper<T> {

	private static final String TAG = ElasticClientMapper.class.getName();
	private ObjectMapper mapper = new ObjectMapper();

	public List<T> mapToList(String jsonSource, Class<T> typeReference) {

		List<T> retValue = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(jsonSource);
			JSONObject hits = json.getJSONObject("hits");
			JSONArray hitsArray = hits.getJSONArray("hits");

			for (int i = 0; i < hitsArray.length(); i++) {
				JSONObject h = hitsArray.getJSONObject(i);
				JSONObject source = h.getJSONObject("_source");
				T entity = mapper.readValue(source.toString(), typeReference);
				retValue.add(entity);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}

		return retValue;
	}

	public String map(T entity) {
		try {
			return mapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
