package com.sf.elastic.repository;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.sf.elastic.R;
import com.sf.elastic.model.City;

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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.params.SearchType;
import rx.Observable;

@EBean
public class CityRepository implements Repository<City> {

	private JestClient client;

	@RootContext
	public Context context;

	public CityRepository() {
		DroidClientConfig clientConfig = new DroidClientConfig.Builder("https://ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k:@silverforge.east-us.azr.facetflow.io")
			.multiThreaded(true)
			.readTimeout(10000)
			.build();
		JestClientFactory factory = new JestClientFactory();
		factory.setDroidClientConfig(clientConfig);

		client = factory
			.getObject();
	}

	@Override
	public Observable<City> getNextCity(final String text) {
		Observable<City> observable
			= Observable.create(subscriber -> {
			Search search = getSearch(text);
			try {

//					KeyStore keyStore = ...;
//					String algorithm = TrustManagerFactory.getDefaultAlgorithm();
//					TrustManagerFactory tmf = null;
//					tmf = TrustManagerFactory.getInstance(algorithm);
//					tmf.init(keyStore);
//
//					SSLContext context = SSLContext.getInstance("TLS");
//					context.init(null, tmf.getTrustManagers(), null);


//				URL url = new URL("https://ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k:@silverforge.east-us.azr.facetflow.io");
//				String charset = "UTF-8";
//				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
////					connection.setSSLSocketFactory(context.getSocketFactory());
//				connection.setRequestMethod("POST");
//				connection.setDoOutput(true);
//				connection.setRequestProperty("Authorization", "Basic WmpqbmtOTWdoMHVqNXlDRkl2WVZHUXN1ZUVTQ0xqMWs6");
//				connection.setRequestProperty("Accept-Charset", charset);
//				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
//				connection.setDoInput(true);
//
//				StringBuilder sb = new StringBuilder();
//				sb.append(search.toString());
//
//				OutputStreamWriter outputWriter = new OutputStreamWriter(connection.getOutputStream());
//				outputWriter.write(sb.toString());
//				outputWriter.flush();
//				outputWriter.close();
//				// handle response

				String result = new String();
				InputStream is = getInputStream("https://silverforge.east-us.azr.facetflow.io/cities/_search", "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k", "", search.getData(new Gson()));
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}


				// JSON objects
				JSONArray hitsArray = null;
				JSONObject hits = null;
				JSONObject source = null;
				JSONObject json = null;

				// parse the JSON response
				json = new JSONObject(result);
				hits = json.getJSONObject("hits");
				hitsArray = hits.getJSONArray("hits");

				for (int i=0; i<hitsArray.length(); i++) {
					JSONObject h = hitsArray.getJSONObject(i);
					source = h.getJSONObject("_source");
					//string object = (source.getString("the string you want to get"));
				}

//					SearchResult searchResult = client.execute(search);
//					List<SearchResult.Hit<City, Void>> hits = searchResult.getHits(City.class);
//
//					for (SearchResult.Hit<City, Void> cityHit : hits) {
//						subscriber.onNext(cityHit.source);
//					}

				subscriber.onCompleted();
//					client.shutdownClient();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				subscriber.onError(e);
			}
		});

			return observable;
	}

	private Search getSearch(String text) {
		InputStream is = context
			.getResources()
			.openRawResource(R.raw.city_name_prefix_query);

		String queryText = convertStreamToString(is);
		String query = queryText
			.replace("{{SIZE}}", "1000")
			.replace("{{NAME}}", text.toLowerCase());

		return new Search
			.Builder(query)
			// multiple index or types can be added.
			.setSearchType(SearchType.QUERY_AND_FETCH)
			.addIndex("cities")
			.addType("city")
			.addSort(new Sort("name", Sort.Sorting.DESC))
//			.setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
//			.setHeader("CSP", "active")
//			.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//			.setHeader("Content-Type", "application/json")
//			.setHeader("Content-Type", "application/x-www-form-urlencoded")
//			.refresh(true)
			.build();
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
//		conn.setRequestProperty("Authorization", "Basic WmpqbmtOTWdoMHVqNXlDRkl2WVZHUXN1ZUVTQ0xqMWs6");

		// Add any data you wish to post here
		StringBuilder sb = new StringBuilder();
		sb.append(query);

		OutputStreamWriter outputWriter = new OutputStreamWriter(conn.getOutputStream());
		outputWriter.write(sb.toString());
		outputWriter.flush();
		outputWriter.close();

		conn.connect();
		return conn.getInputStream();
	}
}
