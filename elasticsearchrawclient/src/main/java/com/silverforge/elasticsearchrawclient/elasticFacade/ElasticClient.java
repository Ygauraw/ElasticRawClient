package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.Connectable;
import com.silverforge.elasticsearchrawclient.connector.Connector;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.model.AddDocumentResult;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

// TODO : create interface for elasticclient once the methods are implemented
public class ElasticClient {
	private static final String TAG = ElasticClient.class.getName();
	private static final String STRING_EMPTY = "";
	private Connectable connector;
	private ObjectMapper mapper = new ObjectMapper();

	public Raw raw = new Raw();

	public ElasticClient(Connectable connector)
		throws MalformedURLException {
		this.connector = connector;
	}

	public ElasticClient(ConnectorSettings settings)
		throws URISyntaxException {
		connector = new Connector(settings);
	}

	public void createIndex(String indexName, String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

		if (indexName.startsWith("/"))
			connector.put(indexName, data);
		else
			connector.put("/" + indexName, data);
	}

	public void createAlias(String indexName, String aliasName) {

	}

	public void removeIndices()
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

		removeIndices(connector.getSettings().getIndices());
	}

	public void removeIndices(String[] indexNames)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

		for (String indexName : indexNames) {

			if (indexName.startsWith("/"))
				connector.delete(indexName);
			else
				connector.delete("/" + indexName);
		}
	}

	public void removeAlias(String aliasName) {

	}

	public <T> String addDocument(T entity)
			throws IndexCannotBeNullException, IllegalArgumentException {

		return addDocument(null, entity);
	}


	public <T> String addDocument(String id, T entity)
			throws IndexCannotBeNullException, IllegalArgumentException {

		if (entity == null)
			throw new IllegalArgumentException("entity cannot be null");

		String retValue = "";
		try {
			String entityJson = mapper.writeValueAsString(entity);
			String addPath = getOperationPath(id);

			String result = connector.post(addPath, entityJson);
			AddDocumentResult addDocumentResult = mapper.readValue(result, AddDocumentResult.class);
			retValue = addDocumentResult.getId();
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		} catch (IndexCannotBeNullException ie) {
			throw ie;
		}

		return retValue;
	}

	public <T> String addDocument(String index, String type, String id, T entity) {
		if (entity == null)
			throw new IllegalArgumentException("entity cannot be null");

		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		if (TextUtils.isEmpty(type))
			throw new IllegalArgumentException("type cannot be null or empty");

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

        String retValue = "";
        try {
            String entityJson = mapper.writeValueAsString(entity);
            String addPath = String.format("/%s/%s/%s", index, type, id);

            String result = connector.post(addPath, entityJson);
            AddDocumentResult addDocumentResult = mapper.readValue(result, AddDocumentResult.class);
            retValue = addDocumentResult.getId();
        } catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return retValue;
    }

	public void removeDocument(String id)
			throws IndexCannotBeNullException, NoSuchAlgorithmException, IOException, KeyManagementException {

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		String deletePath = getOperationPath(id);
		connector.delete(deletePath);
	}

	public void removeDocument(String index, String type, String id)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {
		
		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		if (TextUtils.isEmpty(type))
			throw new IllegalArgumentException("type cannot be null or empty");

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		String deletePath = String.format("/%s/%s/%s", index, type, id);
		connector.delete(deletePath);
	}

	public void removeDocumentsQuery(String query) {

	}

	public void removeDocumentsQuery(String index, String type, String query) {

	}

    public <T> void updateDocument(String id, T entity) {

    }

    public <T> void updateDocument(String index, String type, String id, T entity) {

    }

    public void bulk() {

    }

	public String getDocument(String[] ids)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

		return getDocument(ids, null);
	}

	public String getDocument(String[] ids, String type)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

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
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

		String queryPath = getQueryPath();
		return connector.post(queryPath, query);
	}

	protected String getOperationPath(String id)
			throws IndexCannotBeNullException {

		boolean indicesAreEmpty = true;

		StringBuilder pathBuilder = new StringBuilder();

		String[] indices = connector.getSettings().getIndices();
		if (indices != null && indices.length > 0) {
			indicesAreEmpty = false;
			pathBuilder.append("/");
			String indicesPath = StringUtils.makeCommaSeparatedList(indices);
			pathBuilder.append(indicesPath);
		}

		if (indicesAreEmpty)
			throw new IndexCannotBeNullException();

		String[] types = connector.getSettings().getTypes();
		if (types != null && types.length > 0) {
			pathBuilder.append("/");
			String typesPath = StringUtils.makeCommaSeparatedList(types);
			pathBuilder.append(typesPath);
		}

		if (!TextUtils.isEmpty(id))
			pathBuilder.append("/").append(id);

		return pathBuilder.toString();
	}

	protected String getQueryPath() {
		boolean indicesAreEmpty = true;
		boolean typesAreEmpty = true;

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
			typesAreEmpty = false;
			if (indicesAreEmpty)
				pathBuilder.append("/_all");

			pathBuilder.append("/");
			String typesPath = StringUtils.makeCommaSeparatedList(types);
			pathBuilder.append(typesPath);
		}

		if (indicesAreEmpty && typesAreEmpty)
			pathBuilder.append("/_all");

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
