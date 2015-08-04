package com.silverforge.elasticsearchrawclient.elasticFacade;

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
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
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
			throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException {

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
			throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException {

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

		String retValue = "";
		try {
			String entityJson = ElasticClientMapper.mapToJson(entity);
			String addPath = getOperationPath(index, type, id, OperationType.CREATE);

			InvokeResult result = connector.post(addPath, entityJson);

			AddDocumentResult addDocumentResult
					= ElasticClientMapper.mapToEntity(result.getResult(),
														AddDocumentResult.class);

			retValue = addDocumentResult.getId();

		} catch	(IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}

		return retValue;
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
			throws IllegalArgumentException, IndexCannotBeNullException, TypeCannotBeNullException {

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
			throws IllegalArgumentException {

		if (TextUtils.isEmpty(index))
			throw new IllegalArgumentException("index cannot be null or empty");

		if (TextUtils.isEmpty(type))
			throw new IllegalArgumentException("type cannot be null or empty");

		if (TextUtils.isEmpty(id))
			throw new IllegalArgumentException("id cannot be null or empty");

		try {
			String deletePath = getOperationPath(index, type, id, OperationType.DELETE);
			connector.delete(deletePath);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}
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

		try {
			String deletePath = getOperationPath(OperationType.QUERY);
			connector.delete(deletePath, query);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}
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

		try {
			String deletePath = getOperationPath(indices, types, OperationType.QUERY);
			connector.delete(deletePath, query);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}

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
			throws IndexCannotBeNullException, TypeCannotBeNullException {

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

		try {
			String entityJson = ElasticClientMapper.mapToJson(entity);
			String updatePath = getOperationPath(index, type, id, OperationType.UPDATE);

			String updateTemplate
					= StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
					R.raw.update_template);
			String data = updateTemplate.replace("{{ENTITYJSON}}", entityJson);

			connector.post(updatePath, data);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}
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
	public <T> List<T> getDocument(String[] ids, Class<T> classType)
			throws IndexCannotBeNullException {

		String[] indices = connector.getSettings().getIndices();
		if (indices == null || indices.length == 0)
			throw new IndexCannotBeNullException();

		return getDocument(indices, null, ids, classType);
	}

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param type the type of the document (Elastic)
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	public <T> List<T> getDocument(String type, String[] ids, Class<T> classType)
			throws IndexCannotBeNullException {

		String[] indices = connector.getSettings().getIndices();
		if (indices == null || indices.length == 0)
			throw new IndexCannotBeNullException();

		return getDocument(indices, type, ids, classType);
	}

	public <T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType) {

		String[] indexParam = null;

		if (!TextUtils.isEmpty(index))
			indexParam = new String[] {index};

		return getDocument(indexParam, type, ids, classType);
	}

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param type the type of the document (Elastic)
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	public <T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType) {

		List<T> retValue = new ArrayList<>();
		try {
			String queryTemplate;
			String queryIds = StringUtils.makeCommaSeparatedListWithQuotationMark(ids);
			String query;
			String queryPath;
			if (TextUtils.isEmpty(type)) {
				queryTemplate = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
						R.raw.search_by_ids);
				query = queryTemplate.replace("{{IDS}}", queryIds);
			} else {
				queryTemplate = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
						R.raw.search_by_ids_and_type);
				query = queryTemplate
						.replace("{{IDS}}", queryIds)
						.replace("{{TYPE}}", type);
			}

			queryPath = getOperationPath(indices, null, OperationType.SEARCH);

			String documents = connector.post(queryPath, query).getResult();
			retValue = ElasticClientMapper.mapToHitList(documents, classType);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}

		return retValue;
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

		List<T> retValue = new ArrayList<>();

		try {
			String queryPath = getOperationPath(OperationType.SEARCH);
			String documents = connector.post(queryPath, query).getResult();

			retValue = ElasticClientMapper.mapToHitList(documents, classType);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}

		return retValue;
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

		List<T> retValue = new ArrayList<>();

		try	{

			String queryPath = getOperationPath(index, null, OperationType.SEARCH);
			String documents = connector.post(queryPath, query).getResult();

			retValue = ElasticClientMapper.mapToHitList(documents, classType);
		} catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
			e.printStackTrace();
		}

		return retValue;
	}


	protected String getOperationPath(OperationType operationType)
			throws IndexCannotBeNullException, TypeCannotBeNullException {
		return getOperationPath(null, operationType);
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
			throws IndexCannotBeNullException, TypeCannotBeNullException {

		return getOperationPath(connector.getSettings().getIndices(), connector.getSettings().getTypes(), id, operationType);
	}

	protected String getOperationPath(String index, String type, OperationType operationType)
			throws IndexCannotBeNullException, TypeCannotBeNullException {

		String[] indexParam = null;
		String[] typeParam = null;

		if (!TextUtils.isEmpty(index))
			indexParam = new String[] {index};

		if (!TextUtils.isEmpty(type))
			typeParam = new String[] {type};

		return getOperationPath(indexParam, typeParam, null, operationType);
	}

	protected String getOperationPath(String index, String type, String id,OperationType operationType)
			throws IndexCannotBeNullException, TypeCannotBeNullException {

		String[] indexParam = null;
		String[] typeParam = null;

		if (!TextUtils.isEmpty(index))
			indexParam = new String[] {index};

		if (!TextUtils.isEmpty(type))
			typeParam = new String[] {type};

		return getOperationPath(indexParam, typeParam, id, operationType);
	}

	protected String getOperationPath(String[] indices, String[] types, OperationType operationType)
			throws IndexCannotBeNullException, TypeCannotBeNullException {

		return getOperationPath(indices, types, null, operationType);
	}

	protected String getOperationPath(String[] indices, String[] types, String id, OperationType operationType)
			throws IndexCannotBeNullException, TypeCannotBeNullException {

		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append("/");
		switch (operationType) {
			case CREATE:
			case DELETE:
			case UPDATE:

				if (indices == null || indices.length == 0)
					throw new IndexCannotBeNullException();

				String index = indices[0];
				pathBuilder.append(index).append("/");

				if (types == null || types.length == 0)
					throw new TypeCannotBeNullException();

				String type = types[0];
				pathBuilder.append(type);

				if (!TextUtils.isEmpty(id))	{
					pathBuilder.append("/").append(id);

					String operationPath = operationType.getOperationTypePath();
					if (!TextUtils.isEmpty(operationPath))
                        pathBuilder.append("/").append(operationPath);
				}

				break;
			case QUERY:
			case SEARCH:
				if (indices == null || indices.length == 0)
					pathBuilder.append("_all/");
				else
					pathBuilder.append(StringUtils.makeCommaSeparatedList(indices)).append("/");

				if (types != null && types.length > 0)
					pathBuilder.append(StringUtils.makeCommaSeparatedList(types));

				String operationPath = operationType.getOperationTypePath();
				if (!TextUtils.isEmpty(operationPath))
					pathBuilder.append("/").append(operationPath);

				break;
		}

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
