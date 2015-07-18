package com.silverforge.elasticsearchrawclient.ElasticFacade;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.Connector.Connectable;
import com.silverforge.elasticsearchrawclient.Connector.Connector;
import com.silverforge.elasticsearchrawclient.Connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.Utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.Utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ElasticClient {
	private static final String STRING_EMPTY = "";
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

	public void removeAlias() {

	}

	public void addDocument() {

	}

	public void removeDocument() {

	}

	public void bulkDocuments() {

	}

	public String getDocumentById(String[] ids)
			throws NoSuchAlgorithmException,
			IOException,
			KeyManagementException {

		return getDocumentByIdAndType(ids, null);
	}

	public String getDocumentByIdAndType(String[] ids, String type)
			throws NoSuchAlgorithmException,
			IOException,
			KeyManagementException {

		InputStream inputStream;
		if (TextUtils.isEmpty(type)) {
			inputStream = ElasticClientApp
					.getAppContext()
					.getResources()
					.openRawResource(R.raw.search_by_ids);
		} else {
			inputStream = ElasticClientApp
					.getAppContext()
					.getResources()
					.openRawResource(R.raw.search_by_ids_and_type);
		}

		String queryIds = StringUtils.makeCommaSeparatedListWithQuotationMark(ids);
		String queryTemplate = StreamUtils.convertStreamToString(inputStream);

		String query;
		if (TextUtils.isEmpty(type))
			query = queryTemplate.replace("{{IDS}}", queryIds);
		else
			query = queryTemplate
						.replace("{{IDS}}", queryIds)
						.replace("{{TYPE}}", type);

		String queryPath = getQueryPath();
		return connector.post(queryPath, query);
	}

	public String search(String query)
			throws NoSuchAlgorithmException,
			IOException,
			KeyManagementException {

		String queryPath = getQueryPath();
		return connector.post(queryPath, query);
	}

	private String getQueryPath() {
		boolean indicesAreEmpty = true;

		StringBuilder pathBuilder = new StringBuilder();

		String[] indices = connector.getSettings().getIndices();
		if (indices != null && indices.length > 0) {
			indicesAreEmpty = false;
			pathBuilder.append("/");
			String indicesPath = StringUtils.makeCommaSeparatedList(indices);
			pathBuilder.append(indicesPath);
		}

		String[] types = connector.getSettings().getTypes();
		if (types != null && types.length > 0) {
			if (indicesAreEmpty)
				pathBuilder.append("/_all");
			else
				pathBuilder.append("/");
			String typesPath = StringUtils.makeCommaSeparatedList(types);
			pathBuilder.append(typesPath);
		}
		pathBuilder.append("/_search");
		return pathBuilder.toString();
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
