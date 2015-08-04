package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.Connectable;
import com.silverforge.elasticsearchrawclient.connector.Connector;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.AliasParser;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.AddDocumentResult;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

// TODO : create interface for elasticclient once the methods are implemented
// TODO : QueryManagr should be created to maintain and reuse certain queries by user
public class ElasticClient {
	private Connectable connector;

	/**
	 * Proxy class for connector in order to have "raw" access to ElasticSearch
	 */
	public Raw raw = new Raw();

	/**
	 * Constructor of ElasticClient
	 * @param connector the Connector instance
	 * @throws MalformedURLException
	 * @see com.silverforge.elasticsearchrawclient.connector.Connector
	 */
	public ElasticClient(Connectable connector)
		throws MalformedURLException {
		this.connector = connector;
	}

	/**
	 * Constructor of ElasticClient
	 *<strong><pre>
	 *ConnectorSettings settings = ConnectorSettings
	 *    .builder()
	 *    .baseUrl(ELASTIC_URL)
	 *    .indices(ELASTIC_INDICES)
	 *    .types(ELASTIC_TYPES)
	 *    .userName(ELASTIC_APIKEY)
	 *    .build();
	 *
	 *    try {
	 *        client = new ElasticClient(settings);
	 *    } catch (URISyntaxException e) {
	 *        e.printStackTrace();
	 *        Log.e(TAG, e.getMessage());
	 *        fail(e.getMessage());
	 *    }
	 *</pre></strong>
	 * @param settings the settings of the ElasticClient
	 * @throws URISyntaxException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 */
	public ElasticClient(ConnectorSettings settings)
		throws URISyntaxException {
		connector = new Connector(settings);
	}

	/**
	 * Creates index based on indexName and the data
	 * @param indexName the name of the index
	 * @param data the json string defines structure of the index for example
	 * <pre>
	 * {<br/>
	 *   "mappings" : {<br/>
	 *     "testcity" : {<br/>
	 *       "properties" : {<br/>
	 *         "name" : { "type": "string"}<br/>
	 *       }<br/>
	 *     }<br/>
	 *   }<br/>
	 * }
	 * </pre>
	 * @return true : if success
	 */
	public boolean createIndex(String indexName, String data) {
		String path = StringUtils.ensurePath(indexName);
		InvokeResult result = connector.put(path, data);
		return result.isSuccess();
	}

	/**
	 * Adds alias to an index
	 * @param indexName the index name, it could be a group as well e.g.: "testind*"
	 * @param aliasName the alias
	 */
	public void addAlias(String indexName, String aliasName) {
		String addAliasTemplate
			= StreamUtils.getRawContent(ElasticClientApp.getAppContext(), R.raw.add_alias);

		String data = addAliasTemplate
				.replace("{{INDEXNAME}}", indexName)
				.replace("{{ALIASNAME}}", aliasName);

		connector.post("/_aliases", data);
	}

	/**
	 * Removes all the indices defined in the ConnectorSettings
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public void removeIndices() {
		removeIndices(connector.getSettings().getIndices());
	}

	/**
	 * Removes all indices given by parameter
	 * @param indexNames the indices
	 */
	public void removeIndices(String[] indexNames) {
		for (String indexName : indexNames) {
			String path = StringUtils.ensurePath(indexName);
			connector.delete(path);
		}
	}

	/**
	 * Checks if the index already exists
	 * @param indexName The name of the index
	 * @return true if exists
	 */
	public boolean indexExists(String indexName) {
		String path = StringUtils.ensurePath(indexName);
		InvokeResult result = connector.head(path);
		return result.isSuccess();
	}

	/**
	 * Retrieves the aliases of the given index
	 * @param index the index
	 * @return List of aliases
	 */
	public List<String> getAliases(String index) {
		ArrayList<String> retValue = new ArrayList<>();
		String getPath = String.format("/%s/_aliases", index);

		InvokeResult invokeResult = connector.get(getPath);
		if (invokeResult.isSuccess()) {
			List<String> aliasesFromJson = AliasParser.getAliasesFromJson(index, invokeResult.getResult());
			retValue.addAll(aliasesFromJson);
		}
		return retValue;
	}

	/**
	 * Removes alias from index
	 * @param indexName the index name
	 * @param aliasName the alias name
	 */
	public void removeAlias(String indexName, String aliasName) {
		String addAliasTemplate
			= StreamUtils.getRawContent(ElasticClientApp.getAppContext(), R.raw.remove_alias);

		String data = addAliasTemplate
				.replace("{{INDEXNAME}}", indexName)
				.replace("{{ALIASNAME}}", aliasName);

		connector.post("/_aliases", data);
	}

	/**
	 * Adds a document to the index defined in ConnectorSettings
	 * @param entity the entity should be added to index (will be json serialized)
	 * @param <T> the type of the entity
	 * @return the id of the newly created document
	 * @throws IndexCannotBeNullException
	 * @throws IllegalArgumentException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public <T> String addDocument(T entity)
			throws IndexCannotBeNullException, IllegalArgumentException {

		return addDocument(null, entity);
	}

	/**
	 * Adds a document to the index defined in ConnectorSettings
	 * @param id the id of the document
	 * @param entity the entity should be added to index (will be json serialized)
	 * @param <T> the type of the entity
	 * @return the id of the newly created document
	 * @throws IndexCannotBeNullException
	 * @throws IllegalArgumentException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public <T> String addDocument(String id, T entity)
			throws IndexCannotBeNullException, IllegalArgumentException {

		if (entity == null)
			throw new IllegalArgumentException("entity cannot be null");

		String entityJson = ElasticClientMapper.mapToJson(entity);
		String addPath = getOperationPath(id, OperationType.CREATE);

		InvokeResult result = connector.post(addPath, entityJson);
		AddDocumentResult addDocumentResult
				= ElasticClientMapper.mapToEntity(result.getResult(), AddDocumentResult.class);

		return addDocumentResult.getId();
	}

	/**
	 * Adds a document to the index, in case of IOException empty id will be retrieved
	 * @param index the index (Elastic)
	 * @param type the type of the document (Elastic)
	 * @param id the id of the document
	 * @param entity the entity
	 * @param <T> the type of the entity
	 * @return the id of the newly created document, it's equal to the param id
	 * @throws IllegalArgumentException
	 */
	public <T> String addDocument(String index, String type, String id, T entity)
			throws IllegalArgumentException {

		if (entity == null)
			throw new IllegalArgumentException("entity cannot be null");

		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		if (TextUtils.isEmpty(type))
			throw new IllegalArgumentException("type cannot be null or empty");

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		String entityJson = ElasticClientMapper.mapToJson(entity);
		String addPath = String.format("/%s/%s/%s", index, type, id);

		InvokeResult result = connector.post(addPath, entityJson);
		AddDocumentResult addDocumentResult
                = ElasticClientMapper.mapToEntity(result.getResult(), AddDocumentResult.class);

		return addDocumentResult.getId();
    }

	/**
	 * Removes document from index defined in ConnectorSettings based on the given id
	 * @param id the id
	 * @throws IllegalArgumentException
	 * @throws IndexCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public void removeDocument(String id)
			throws IllegalArgumentException, IndexCannotBeNullException {

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		String deletePath = getOperationPath(id, OperationType.DELETE);
		connector.delete(deletePath);
	}

	/**
	 * Removes document from index given by parameters
	 * @param index the index (Elastic)
	 * @param type the type (Elastic)
	 * @param id the id (Elastic)
	 * @throws IllegalArgumentException
	 */
	public void removeDocument(String index, String type, String id)
			throws IllegalArgumentException{

		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		if (TextUtils.isEmpty(type))
			throw new IllegalArgumentException("type cannot be null or empty");

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		String deletePath = String.format("/%s/%s/%s", index, type, id);
		connector.delete(deletePath);
	}

	/**
	 * Removes documents by query from index defined in ConnectorSettings
	 * @param query the query, e.g.:
	 *<pre>
	 *{<br/>
	 *  "query": {<br/>
	 *    "term": {<br/>
	 *      "name": {<br/>
	 *        "value": "myCityName"<br/>
	 *      }<br/>
	 *    }<br/>
	 *  }<br/>
	 *}
	 *</pre>
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public void removeDocumentsQuery(String query) {
		String deletePath
			= getDeleteQueryPath(connector.getSettings().getIndices(),
				connector.getSettings().getTypes());
		connector.delete(deletePath, query);
	}

	/**
	 * Removes documents based on the given query on the given indices
	 * @param indices the indices
	 * @param types the types of the indices
	 * @param query the query, e.g.:
	 *<pre>
	 *{<br/>
	 *  "query": {<br/>
	 *    "term": {<br/>
	 *      "name": {<br/>
	 *        "value": "myCityName"<br/>
	 *      }<br/>
	 *    }<br/>
	 *  }<br/>
	 *}
	 *</pre>
	 */
	public void removeDocumentsQuery(String[] indices, String[] types, String query) {
		String deleteQueryPath = getDeleteQueryPath(indices, types);
		connector.delete(deleteQueryPath, query);
	}

	/**
	 * Updates document based on the given parameters in index defined in ConnectorSettings
	 * @param id the id
	 * @param entity the entity (will be json serialized)
	 * @param <T> the type of the entity
	 * @throws IndexCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public <T> void updateDocument(String id, T entity)
			throws IndexCannotBeNullException {

		if (entity == null)
			throw new IllegalArgumentException("entity cannot be null");

		String entityJson = ElasticClientMapper.mapToJson(entity);
		String updatePath = getOperationPath(id, OperationType.UPDATE);

		String updateTemplate
                = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                            R.raw.update_template);
		String data = updateTemplate.replace("{{ENTITYJSON}}", entityJson);

		connector.post(updatePath, data);
	}

	/**
	 * Updates document based on the given parameters
	 * @param index the index (Elastic)
	 * @param type the type (Elastic)
	 * @param id the id of the document (Elastic)
	 * @param entity the entity (will be json serialized)
	 * @param <T> the tpe of the entity
	 * @throws IllegalArgumentException
	 */
    public <T> void updateDocument(String index, String type, String id, T entity)
			throws IllegalArgumentException {

		if (entity == null)
			throw new IllegalArgumentException("entity cannot be null");

		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		if (TextUtils.isEmpty(type))
			throw new IllegalArgumentException("type cannot be null or empty");

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		String entityJson = ElasticClientMapper.mapToJson(entity);
		String updatePath = String.format("/%s/%s/%s/_update", index, type, id);

		String updateTemplate
                = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                            R.raw.update_template);
		String data = updateTemplate.replace("{{ENTITYJSON}}", entityJson);

		connector.post(updatePath, data);
	}

	/**
	 * In progress
	 */
    public void bulk() {

    }

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	public <T> List<T> getDocument(String[] ids, Class<T> classType) {
		return getDocument(null, ids, classType);
	}

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param type the type of the document (Elastic)
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	public <T> List<T> getDocument(String type, String[] ids, Class<T> classType) {
		return getDocument(null, type, ids, classType);
	}

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param index the index (Elastic)
	 * @param type the type of the document (Elastic)
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	public <T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType) {
		String queryTemplate;
		String queryIds = StringUtils.makeCommaSeparatedListWithQuotationMark(ids);
		String query;
		String queryPath;
		if (TextUtils.isEmpty(type)) {
			queryTemplate = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
														R.raw.search_by_ids);
			query = queryTemplate.replace("{{IDS}}", queryIds);
			queryPath = getQueryPath();
		} else {
			queryTemplate = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
					R.raw.search_by_ids_and_type);
			query = queryTemplate
					.replace("{{IDS}}", queryIds)
					.replace("{{TYPE}}", type);
			if (TextUtils.isEmpty(index))
				queryPath = "/_all/_search";
			else
				queryPath = String.format("/%s/_search", index);
		}

		String documents = connector.post(queryPath, query).getResult();
		return ElasticClientMapper.mapToHitList(documents, classType);
	}

	/**
	 * Searches in index based on the query and retrieves with the list of entities from index defined in ConnectorSettings
	 * @param query the query, e.g.:
	 *<pre>
	 *{<br/>
	 *  "query": {<br/>
	 *    "match_all": {}<br/>
	 *  }<br/>
	 *}<br/>
	 *</pre>
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by query
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	public <T> List<T> search(String query, Class<T> classType) {
		String queryPath = getQueryPath();
		String documents = connector.post(queryPath, query).getResult();

		return ElasticClientMapper.mapToHitList(documents, classType);
	}

	/**
	 * Searches in index based on the query and retrieves with the list of entities
	 * @param index the index
	 * @param query the query, e.g.:
	 *<pre>
	 *{<br/>
	 *  "query": {<br/>
	 *    "match_all": {}<br/>
	 *  }<br/>
	 *}<br/>
	 *</pre>
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by query
	 */
	public <T> List<T> search(String index, String query, Class<T> classType) {
		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		String queryPath = String.format("/%s/_search", index);
		String documents = connector.post(queryPath, query).getResult();

		return ElasticClientMapper.mapToHitList(documents, classType);
	}


	/**
	 * Retrives with the path of the operation defined in OperationType based on ConnectorSettings
	 * @param id the id of the document. could be <strong>null</strong>
	 * @param operationType the type of the operation
	 * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/2
	 * @throws IndexCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.elasticFacade.OperationType
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	protected String getOperationPath(String id, OperationType operationType)
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

		if (!TextUtils.isEmpty(id))	{
			pathBuilder.append("/").append(id);

			if (operationType != null) {
				String operationPath = operationType.getOperationTypePath();
				if (!TextUtils.isEmpty(operationPath))
					pathBuilder.append("/").append(operationPath);
			}
		}

		return pathBuilder.toString();
	}

	/**
	 * Retrieves with the query path based on ConnectorSettings
	 * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/_search or if no indices/types defined with /_all/search
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
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

	/**
	 * Retrieves with path for delete
	 * @param indices the indices
	 * @param types the types
	 * @return the path, e.g.: /myindex,yourindex/mytype,yourtype/_query
	 */
	@NonNull
	protected String getDeleteQueryPath(String[] indices, String[] types) {
		StringBuilder pathBuilder = new StringBuilder();
		if (indices == null || indices.length == 0)
			pathBuilder.append("/_all");
		else {
			String indexList = StringUtils.makeCommaSeparatedList(indices);
			pathBuilder
					.append("/")
					.append(indexList);
		}

		if (types != null && types.length > 0) {
			String typeList = StringUtils.makeCommaSeparatedList(types);
			pathBuilder
					.append("/")
					.append(typeList);
		}

		pathBuilder.append("/_query");
		return pathBuilder.toString();
	}

	/**
	 * Proxy class for connector in order to have "raw" access to ElasticSearch
	 */
	public final class Raw {

		private Raw() {}

		/**
		 * HTTP method HEAD on given path. The url is defined in ConnectorSettings
		 * @param path the path, e.g.: /apple/pear/1
		 * @return the result of the invoke
		 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
		 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
		 * @see com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult
		 */
		public InvokeResult head(String path) {
			return connector.head(path);
		}

		/**
		 * HTTP method GET on given path. The url is defined in ConnectorSettings
		 * @param path the path, e.g.: /apple/pear/1
		 * @return the result of the invoke
		 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
		 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
		 * @see com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult
		 */
		public InvokeResult get(String path) {
			return connector.get(path);
		}

		/**
		 * HTTP method POST on given path. The url is defined in ConnectorSettings
		 * @param path the path, e.g.: /apple/pear/1
		 * @param data the request data
		 * @return the result of the invoke
		 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
		 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
		 * @see com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult
		 */
		public InvokeResult post(String path, String data) {
			return connector.post(path, data);
		}

		/**
		 * HTTP method PUT on given path. The url is defined in ConnectorSettings
		 * @param path the path, e.g.: /apple/pear/1
		 * @param data the request data
		 * @return the result of the invoke
		 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
		 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
		 * @see com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult
		 */
		public InvokeResult put(String path, String data) {
			return connector.put(path, data);
		}

		/**
		 * HTTP method DELETE on given path. The url is defined in ConnectorSettings
		 * @param path the path, e.g.: /apple/pear/1
		 * @param data the request data
		 * @return the result of the invoke
		 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
		 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
		 * @see com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult
		 */
		public InvokeResult delete(String path, String data) {
			return connector.delete(path, data);
		}
	}
}
