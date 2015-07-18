package com.sf.elastic.repositories;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.sf.elastic.R;
import com.sf.elastic.models.City;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.ElasticClient;
import com.silverforge.elasticsearchrawclient.elasticFacade.Mappers.RawSourceMapTo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
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

	private static final String TAG = CityRepository.class.getName();
	private static final String ELASTIC_URL = "https://silverforge.east-us.azr.facetflow.io";
	private static final String ELASTIC_APIKEY = "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k";
	private static final String[] ELASTIC_INDICES = new String[] {"cities"};

	private ElasticClient client;
	private RawSourceMapTo<City> cityMapper = new RawSourceMapTo<>();

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
		Observable<City> observable = Observable.create(subscriber -> {
			String search = getSearch(text);

			try {
				String result = client.search(search);
				List<City> cities = cityMapper.mapToList(result, City.class);

				Observable
					.from(cities)
					.subscribe(city -> subscriber.onNext(city));
			} catch (KeyManagementException | IOException | NoSuchAlgorithmException e) {
				e.printStackTrace();
				subscriber.onError(e);
			} finally {
				subscriber.onCompleted();
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

	public void addCities() {
		ConnectorSettings mySettings = ConnectorSettings
			.builder()
			.baseUrl("https://silverforge.east-us.azr.facetflow.io")
			.userName(ELASTIC_APIKEY)
			.build();

		try {
			ElasticClient myClient = new ElasticClient(mySettings);

			InputStream cityIS = context
				.getResources()
				.openRawResource(R.raw.city_list);

			List<String> cities = convertStreamToList(cityIS);

			InputStream is = context
				.getResources()
				.openRawResource(R.raw.city_add);

			String queryText = convertStreamToString(is);

			Observable
				.from(cities)
				.subscribe(city -> {

					String postData = queryText
						.replace("{{CITY_NAME}}", city);
					try {
						myClient.raw.post("/cities/city", postData);
					} catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
						e.printStackTrace();
					}
				});
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
}
