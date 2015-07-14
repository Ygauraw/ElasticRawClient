package com.silverforge.elasticsearchrawclient.ElasticFacade;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.Connector.Connectable;
import com.silverforge.elasticsearchrawclient.Connector.Connector;
import com.silverforge.elasticsearchrawclient.Connector.ConnectorSettings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ElasticClient {
	private Connectable connector;
	public Raw raw = new Raw();

	public ElasticClient(Connectable connector)
		throws MalformedURLException {
		this.connector = connector;
	}

	public ElasticClient(ConnectorSettings settings)
		throws URISyntaxException {
		connector = new Connector(settings);
	}

	public void createIndex() {

	}

	public void createAlias() {

	}

	public void removeIndex() {

	}

	public void addDocument() {

	}

	public void removeDocument() {

	}

	public void bulkDocuments() {

	}

	public void getDocument() {

	}

	public String search(String query)
			throws NoSuchAlgorithmException,
			IOException,
			KeyManagementException {

		boolean indicesAreEmpty = true;

		StringBuilder pathBuilder = new StringBuilder();

		String[] indices = connector.getSettings().getIndices();
		if (indices != null && indices.length > 0) {
			indicesAreEmpty = false;
			pathBuilder.append("/");
			String indicesPath = makeCommaSeparatedList(indices);
			pathBuilder.append(indicesPath);
		}

		String[] types = connector.getSettings().getTypes();
		if (types != null && types.length > 0) {
			if (indicesAreEmpty)
				pathBuilder.append("/_all");
			else
				pathBuilder.append("/");
			String typesPath = makeCommaSeparatedList(types);
			pathBuilder.append(typesPath);
		}
		pathBuilder.append("/_search");

		String retValue = connector.post(pathBuilder.toString(), query);
		return retValue;
	}

	private String makeCommaSeparatedList(String[] list) {
		if (list == null || list.length == 0)
			return "";

		if (list.length == 0)
			return list[0];

		String commaSeparatedList = TextUtils.join(",", list);
		return commaSeparatedList;
	}

	public final class Raw {

		private Raw() {}

		public String get(String path)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.get(path);
		}

		public String post(String path, String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.post(path, data);
		}

		public String put(String path, String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.put(path, data);
		}

		public String delete(String path, String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.delete(path, data);
		}
	}
}
