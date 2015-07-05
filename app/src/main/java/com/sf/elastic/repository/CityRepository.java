package com.sf.elastic.repository;

import android.content.Context;
import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.elastic.R;
import com.sf.elastic.model.City;
import com.silverforge.elasticsearchrawclient.Connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.ElasticFacade.ElasticClient;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import rx.Observable;

@EBean
public class CityRepository implements Repository<City> {

	private static final String ELASTIC_URL = "https://silverforge.east-us.azr.facetflow.io/cities/_search";
	private static final String ELASTIC_APIKEY = "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k";

	@RootContext
	public Context context;

	@Override
	public Observable<City> getNextCity(final String text) {

		Observable<City> observable = Observable.create(subscriber -> {
			String search = getSearch(text);
			try {
				List<City> cities = new ArrayList<City>();

				ConnectorSettings settings = ConnectorSettings
					.builder()
					.url(ELASTIC_URL)
					.userName(ELASTIC_APIKEY)
					.build();

				ElasticClient client = new ElasticClient(settings);
				String result = client.raw.post(search);


				ObjectMapper mapper = new ObjectMapper();

				// JSON objects
				JSONArray hitsArray = null;
				JSONObject hits = null;
				JSONObject source = null;
				JSONObject json = null;

				// parse the JSON response
				json = new JSONObject(result);
				hits = json.getJSONObject("hits");
				hitsArray = hits.getJSONArray("hits");

				for (int i = 0; i < hitsArray.length(); i++) {
					JSONObject h = hitsArray.getJSONObject(i);
					source = h.getJSONObject("_source");
					City city = mapper.readValue(source.toString(), City.class);
					cities.add(city);

					//string object = (source.getString("the string you want to get"));
				}

				subscriber.onCompleted();
			} catch (JSONException | KeyManagementException | IOException | NoSuchAlgorithmException e) {
				e.printStackTrace();
				subscriber.onError(e);
			}
		});

		return observable;
	}

	private String getSearch(String text) {
		InputStream is = context
			.getResources()
			.openRawResource(R.raw.city_name_prefix_query);

		String queryText = convertStreamToString(is);
		return queryText
			.replace("{{SIZE}}", "1000")
			.replace("{{NAME}}", text.toLowerCase());
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
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
		return sb.toString();
	}

	private InputStream getInputStream(String urlStr, String user, String password, String query) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// Create the SSL connection
		SSLContext sc;
		sc = SSLContext.getInstance("TLS");
		sc.init(null, null, new java.security.SecureRandom());
		conn.setSSLSocketFactory(sc.getSocketFactory());

		// Use this if you need SSL authentication
		String userpass = user + ":" + password;
		String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
		conn.setRequestProperty("Authorization", basicAuth);

		// set Timeout and method
		conn.setReadTimeout(7000);
		conn.setConnectTimeout(7000);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		// Add any data you wish to post here
		OutputStreamWriter outputWriter = new OutputStreamWriter(conn.getOutputStream());
		outputWriter.write(query);
		outputWriter.flush();
		outputWriter.close();

		conn.connect();
		return conn.getInputStream();
	}
}
