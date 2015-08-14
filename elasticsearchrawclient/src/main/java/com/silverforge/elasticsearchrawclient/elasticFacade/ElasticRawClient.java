package com.silverforge.elasticsearchrawclient.elasticFacade;

import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkActionResult;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.BulkTuple;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;

import java.util.List;

import rx.Observable;

public interface ElasticRawClient {
	ElasticClient.Raw executeRawRequest();


	boolean createIndex(String indexName, String data);
	void removeIndices();
	void removeIndices(String[] indexNames);
	boolean indexExists(String indexName);


    void addAlias(String indexName, String aliasName);
	List<String> getAliases(String index);
	void removeAlias(String indexName, String aliasName);


	<T> String addDocument(T entity) throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException;
	<T> String addDocument(String id, T entity) throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException;
	<T> String addDocument(String index, String type, String id, T entity) throws IllegalArgumentException;


	void removeDocument(String id) throws IllegalArgumentException, IndexCannotBeNullException, TypeCannotBeNullException;
	void removeDocument(String index, String type, String id) throws IllegalArgumentException;
	void removeDocumentsQuery(String query);
	void removeDocumentsQuery(String[] indices, String[] types, String query);


	<T> void updateDocument(String id, T entity) throws IndexCannotBeNullException, TypeCannotBeNullException;
	<T> void updateDocument(String index, String type, String id, T entity) throws IllegalArgumentException;


    List<BulkActionResult> bulk(List<BulkTuple> bulkItems);


	<T> List<T> getDocument(String[] ids, Class<T> classType) throws IndexCannotBeNullException;
	<T> List<T> getDocument(String type, String[] ids, Class<T> classType) throws IndexCannotBeNullException;
	<T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType);
	<T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType);
    <T> Observable<T> getDocumentAsync(String[] ids, Class<T> classType);
    <T> Observable<T> getDocumentAsync(String type, String[] ids, Class<T> classType);
    <T> Observable<T> getDocumentAsync(String index, String type, String[] ids, Class<T> classType);
    <T> Observable<T> getDocumentAsync(String[] indices, String type, String[] ids, Class<T> classType);


	<T> List<T> search(String query, Class<T> classType);
	<T> List<T> search(String index, String query, Class<T> classType);
    <T> Observable<T> searchAsync(String query, Class<T> classType);
}
