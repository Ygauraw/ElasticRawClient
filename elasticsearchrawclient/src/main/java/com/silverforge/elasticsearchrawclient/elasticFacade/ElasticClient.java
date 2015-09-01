package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.text.TextUtils;

import com.silverforge.elasticsearchrawclient.connector.Connectable;
import com.silverforge.elasticsearchrawclient.connector.Connector;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkActionResult;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkTuple;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult;
import com.silverforge.elasticsearchrawclient.elasticFacade.operations.BulkOperations;
import com.silverforge.elasticsearchrawclient.elasticFacade.operations.DocumentOperations;
import com.silverforge.elasticsearchrawclient.elasticFacade.operations.IndexOperations;
import com.silverforge.elasticsearchrawclient.elasticFacade.operations.QueryOperations;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;

import java.net.URISyntaxException;
import java.util.List;

import rx.Observable;

public class ElasticClient implements ElasticRawClient {
    private Connectable connector;
	private Raw raw = new Raw();
    private IndexOperations indexOperations;
    private DocumentOperations documentOperations;
    private BulkOperations bulkOperations;
    private QueryOperations queryOperations;

    // region Constructors

	/**
	 * Constructor of ElasticClient
	 * @param connector the Connector instance
	 * @see com.silverforge.elasticsearchrawclient.connector.Connector
	 */
	public ElasticClient(Connectable connector) {
		this.connector = connector;
        indexOperations = new IndexOperations(connector);
        documentOperations = new DocumentOperations(connector);
        bulkOperations = new BulkOperations(connector);
        queryOperations = new QueryOperations(connector);
    }

	/**
	 * Constructor of ElasticClient
	 *<pre>
     *<code>
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
     *</code>
	 *</pre>
	 * @param settings the settings of the ElasticClient
	 * @throws URISyntaxException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 */
	public ElasticClient(ConnectorSettings settings)
            throws URISyntaxException {
        this(new Connector(settings));
	}

    // endregion

    /**
     * Proxy method for connector in order to have "raw" access to ElasticSearch
     */
    public Raw executeRawRequest() {
        return raw;
    }

    // region Index operations

	/**
	 * Creates index based on indexName and the data
	 * @param indexName the name of the index
	 * @param data the json string defines structure of the index for example
	 *<pre>
     *{@code
	 * {
	 *   "mappings" : {
	 *     "testcity" : {
	 *       "properties" : {
	 *         "name" : { "type": "string"}
	 *       }
	 *     }
	 *   }
	 * }
	 *}
     *</pre>
	 * @return true : if success
	 */
	@Override
	public boolean createIndex(String indexName, String data) {
		return indexOperations.createIndex(indexName, data);
	}

	/**
	 * Adds alias to an index
	 * @param indexName the index name, it could be a group as well e.g.: "testind*"
	 * @param aliasName the alias
	 */
	@Override
	public void addAlias(String indexName, String aliasName) {
        indexOperations.addAlias(indexName, aliasName);
	}

	/**
	 * Removes all the indices defined in the ConnectorSettings
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
	public void removeIndices() {
		removeIndices(connector.getSettings().getIndices());
	}

	/**
	 * Removes all indices given by parameter
	 * @param indexNames the indices
	 */
	@Override
	public void removeIndices(String[] indexNames) {
        indexOperations.removeIndices(indexNames);
	}

	/**
	 * Checks if the index already exists
	 * @param indexName The name of the index
	 * @return true if exists
	 */
	@Override
	public boolean indexExists(String indexName) {
        return indexOperations.indexExists(indexName);
	}

	/**
	 * Retrieves the aliases of the given index
	 * @param index the index
	 * @return List of aliases
	 */
	@Override
	public List<String> getAliases(String index) {
        return indexOperations.getAliases(index);
	}

	/**
	 * Removes alias from index
	 * @param indexName the index name
	 * @param aliasName the alias name
	 */
	@Override
	public void removeAlias(String indexName, String aliasName) {
        indexOperations.removeAlias(indexName, aliasName);
	}

    // endregion

    // region Add document operations

	/**
	 * Adds a document to the index defined in ConnectorSettings
	 * @param entity the entity should be added to index (will be json serialized)
     * @param <T> the type of the entity
	 * @return the id of the newly created document
	 * @throws IndexCannotBeNullException
	 * @throws IllegalArgumentException
	 * @throws TypeCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
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
	 * @throws TypeCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
	public <T> String addDocument(String id, T entity)
			throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException {

        return documentOperations.addDocument(id, entity);
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
	@Override
	public <T> String addDocument(String index, String type, String id, T entity)
			throws IllegalArgumentException {

        return documentOperations.addDocument(index, type, id, entity);
    }

    // endregion

    // region Remove document methods

	/**
	 * Removes document from index defined in ConnectorSettings based on the given id
	 * @param id the id
	 * @throws IllegalArgumentException
	 * @throws IndexCannotBeNullException
	 * @throws TypeCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
	public void removeDocument(String id)
			throws IllegalArgumentException, IndexCannotBeNullException, TypeCannotBeNullException {
        documentOperations.removeDocument(id);
	}

	/**
	 * Removes document from index given by parameters
	 * @param index the index (Elastic)
	 * @param type the type (Elastic)
	 * @param id the id (Elastic)
	 * @throws IllegalArgumentException
	 */
	@Override
	public void removeDocument(String index, String type, String id)
			throws IllegalArgumentException {
        documentOperations.removeDocument(index, type, id);
	}

	/**
	 * Removes documents by query from index defined in ConnectorSettings
	 * @param query the query, e.g.:
	 *<pre>
     *{@code
	 *{
	 *  "query": {
	 *    "term": {
	 *      "name": {
	 *        "value": "myCityName"
	 *      }
	 *    }
	 *  }
	 *}
     *}
	 *</pre>
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
	public void removeDocumentsQuery(String query) {
        documentOperations.removeDocumentsQuery(query);
	}

	/**
	 * Removes documents based on the given query on the given indices
	 * @param indices the indices
	 * @param types the types of the indices
	 * @param query the query, e.g.:
	 *<pre>
     *{@code
	 *{
	 *  "query": {
	 *    "term": {
	 *      "name": {
	 *        "value": "myCityName"
	 *      }
	 *    }
	 *  }
	 *}
     *}
	 *</pre>
	 */
	@Override
	public void removeDocumentsQuery(String[] indices, String[] types, String query) {
        documentOperations.removeDocumentsQuery(indices, types, query);
	}

    // endregion

    // region Update document methods

	/**
	 * Updates document based on the given parameters in index defined in ConnectorSettings
	 * @param id the id
	 * @param entity the entity (will be json serialized)
	 * @param <T> the type of the entity
	 * @throws IndexCannotBeNullException
	 * @throws TypeCannotBeNullException
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
	public <T> void updateDocument(String id, T entity)
			throws IndexCannotBeNullException, TypeCannotBeNullException {
        documentOperations.updateDocument(id, entity);
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
    @Override
	public <T> void updateDocument(String index, String type, String id, T entity)
			throws IllegalArgumentException {
        documentOperations.updateDocument(index, type, id, entity);
	}

    // endregion

    // region Bulk document method

    /**
     * Bulk processor for create/update/index/delete documents
     * @param bulkItems the action items for bulk processor
     * @return the result of every single action compared with the initial bulkitem
     */
    public List<BulkActionResult> bulk(List<BulkTuple> bulkItems) {
        return bulkOperations.bulk(bulkItems);
    }

    // endregion

    // region Get document methods

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
     * @throws IndexCannotBeNullException
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	@Override
	public <T> List<T> getDocument(String[] ids, Class<T> classType)
			throws IndexCannotBeNullException {

		String[] indices = connector.getSettings().getIndices();
		if (indices == null || indices.length == 0)
			throw new IndexCannotBeNullException();

		return queryOperations.getDocument(indices, null, ids, classType);
	}

	/**
	 * Retrieves with document(s) based on the given parameters
	 * @param type the type of the document (Elastic)
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
     * @throws IndexCannotBeNullException
	 * @return List of entity/entities retrieved by the given id(s) and type
	 */
	@Override
	public <T> List<T> getDocument(String type, String[] ids, Class<T> classType)
			throws IndexCannotBeNullException {

		String[] indices = connector.getSettings().getIndices();
		if (indices == null || indices.length == 0)
			throw new IndexCannotBeNullException();

		return queryOperations.getDocument(indices, type, ids, classType);
	}

    /**
     * Retrieves with document(s) based on the given parameters
     * @param index the index name (Elastic)
     * @param type the type of the document (Elastic)
     * @param ids the id(s) of the document(s) (Elastic)
     * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
     * @param <T> the type of the entity
     * @return List of entity/entities retrieved by the given id(s) and type
     */
	@Override
	public <T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType) {

		String[] indexParam = null;

		if (!TextUtils.isEmpty(index))
			indexParam = new String[] {index};

		return queryOperations.getDocument(indexParam, type, ids, classType);
	}

	/**
	 * Retrieves with document(s) based on the given parameters
     * @param indices the indices for search (Elastic)
	 * @param type the type of the document (Elastic)
	 * @param ids the id(s) of the document(s) (Elastic)
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by the given id(s)
	 */
	@Override
	public <T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType) {
        return queryOperations.getDocument(indices, type, ids, classType);
	}

    public <T> Observable<T> getDocumentAsync(String[] ids, Class<T> classType) {
        return Observable.create(subscriber -> {
            try {
                List<T> documents = getDocument(ids, classType);
                Observable
                    .from(documents)
                    .subscribe(subscriber::onNext);
            } catch (IndexCannotBeNullException e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    public <T> Observable<T> getDocumentAsync(String type, String[] ids, Class<T> classType) {
        return Observable.create(subscriber -> {
            try {
                List<T> documents = getDocument(type, ids, classType);
                Observable
                    .from(documents)
                    .subscribe(subscriber::onNext);
            } catch (IndexCannotBeNullException e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    public <T> Observable<T> getDocumentAsync(String index, String type, String[] ids, Class<T> classType) {
        return Observable.create(subscriber -> {
            try {
                List<T> documents = getDocument(index, type, ids, classType);
                Observable
                    .from(documents)
                    .subscribe(subscriber::onNext);
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    public <T> Observable<T> getDocumentAsync(String[] indices, String type, String[] ids, Class<T> classType) {
        return Observable.create(subscriber -> {
            try {
                List<T> documents = getDocument(indices, type, ids, classType);
                Observable
                        .from(documents)
                        .subscribe(subscriber::onNext);
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    // endregion

    // region Search methods

	/**
	 * Searches in index based on the query and retrieves with the list of entities from index defined in ConnectorSettings
	 * @param query the query, e.g.:
	 *<pre>
     *{@code
	 *{
	 *  "query": {
	 *    "match_all": {}
	 *  }
	 *}
     *}
	 *</pre>
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
	 * @return List of entity/entities retrieved by query
	 * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
	 * @see ElasticClient#ElasticClient(ConnectorSettings settings)
	 */
	@Override
	public <T> List<T> search(String query, Class<T> classType) {
        return queryOperations.search(query, classType);
	}

	/**
	 * Searches in index based on the query and retrieves with the list of entities
	 * @param index the index
	 * @param query the query, e.g.:
	 *<pre>
     *{@code
	 *{
	 *  "query": {
	 *    "match_all": {}
	 *  }
	 *}
     *}
	 *</pre>
	 * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
	 * @param <T> the type of the entity
     * @throws IllegalArgumentException
	 * @return List of entity/entities retrieved by query
	 */
	@Override
	public <T> List<T> search(String index, String query, Class<T> classType)
            throws IllegalArgumentException{
        return queryOperations.search(index, query, classType);
	}

    /**
     * Searches in index based on the query and retrieves with the data sequenc of entities from index defined in ConnectorSettings
     * @param query the query, e.g.:
     *<pre>
     *{@code
     *{
     *  "query": {
     *    "match_all": {}
     *  }
     *}
     *}
     *</pre>
     * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
     * @param <T> the type of the entity
     * @return List of entity/entities retrieved by query
     * @see com.silverforge.elasticsearchrawclient.connector.ConnectorSettings
     * @see ElasticClient#ElasticClient(ConnectorSettings settings)
     */
    public <T> Observable<T> searchAsync(String query, Class<T> classType) {
        return Observable.create(subscriber -> {
            try {
                List<T> searchResult = search(query, classType);
                Observable
                    .from(searchResult)
                    .subscribe(subscriber::onNext);
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    /**
     * Searches in index based on the query and retrieves with the data sequence of entities
     * @param index the index
     * @param query the query, e.g.:
     *<pre>
     *{@code
     *{
     *  "query": {
     *    "match_all": {}
     *  }
     *}
     *}
     *</pre>
     * @param classType the type of the entity will be retrieved for mapping, e.g.: <strong>City.class</strong>
     * @param <T> the type of the entity
     * @throws IllegalArgumentException check onError
     * @return List of entity/entities retrieved by query
     */
    public <T> Observable<T> searchAsync(String index, String query, Class<T> classType) {
        return Observable.create(subscriber -> {
            if (TextUtils.isEmpty(index)) {
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("index cannot be null or empty");
                subscriber.onError(illegalArgumentException);
                subscriber.onCompleted();
            } else {
                try {
                    List<T> searchResult = search(index, query, classType);
                    Observable
                            .from(searchResult)
                            .subscribe(subscriber::onNext);

                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    // endregion

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
