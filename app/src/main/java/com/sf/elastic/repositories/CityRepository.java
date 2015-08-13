package com.sf.elastic.repositories;

import android.content.Context;
import android.util.Log;

import com.sf.elastic.R;
import com.sf.elastic.models.City;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.ElasticClient;
import com.silverforge.elasticsearchrawclient.elasticFacade.ElasticRawClient;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

@EBean
public class CityRepository implements Repository<City> {

	private static final String TAG = CityRepository.class.getName();
	private static final String ELASTIC_URL = "https://mgj.east-us.azr.facetflow.io";
	private static final String ELASTIC_APIKEY = "wihIilbbekmCeppKlgQXDwpSZEUekkk0";
	private static final String[] ELASTIC_INDICES = new String[] {"cities"};

	private ElasticRawClient client;

	@RootContext
	public Context context;

	public CityRepository() {
		ConnectorSettings settings = ConnectorSettings
			.builder()
			.baseUrl(ELASTIC_URL)
			.indices(ELASTIC_INDICES)
			.userName(ELASTIC_APIKEY)
			.build();

		try {
			client = new ElasticClient(settings);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public Observable<City> getNextCity(final String text) {
        String searchQuery = getSearch(text);
        return client
                .searchAsync(searchQuery, City.class);
	}

	private String getSearch(String text) {
		String queryText = StreamUtils.getRawContent(context, R.raw.city_name_prefix_query);
		return queryText
			.replace("{{SIZE}}", "1000")
			.replace("{{NAME}}", text.toLowerCase());
	}


	private List<String> convertStreamToList(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		ArrayList<String> retValue = new ArrayList<>();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				retValue.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retValue;
	}

	public void addCities() {
		ConnectorSettings mySettings = ConnectorSettings
			.builder()
			.baseUrl("https://mgj.east-us.azr.facetflow.io")
			.userName(ELASTIC_APIKEY)
			.build();

		try {
			ElasticClient myClient = new ElasticClient(mySettings);

			InputStream cityIS = context
				.getResources()
				.openRawResource(R.raw.city_list);

			List<String> cities = convertStreamToList(cityIS);

			String queryText = StreamUtils.getRawContent(context, R.raw.city_add);







			Observable
				.from(cities)
				.subscribe(city -> {

					String postData = queryText
						.replace("{{CITY_NAME}}", city);

//					myClient.executeRawRequest().post("/cities/city", postData);
				});
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
}
