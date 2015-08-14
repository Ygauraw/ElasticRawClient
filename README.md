# README #


Contents
--------

- [What is this repository for?](#what-is-this-repository-for)
- [How do I get set up?](#how-do-i-get-set-up)
  - [Set up](#set-up)
    - [Gradle build.config](#gradle-buildconfig)
    - [Issues/contact](#issuescontact)
  - [Configuration](#configuration)
- [Search/Get document(s)](#searchget-documents)
  - [Search](#search)
  - [Get](#get)
- [Add new document](#add-new-document)
- [Update document](#update-document)
- [Remove document](#remove-document)
- [Remove documents query](#remove-documents-query)
- [Bulk document](#bulk-document)
- [Create index](#create-index)
- [Remove index](#remove-index)
- [Create alias](#create-alias)
- [Remove alias](#remove-alias)
- ["Raw" requests](#raw-requests)


## What is this repository for? ##

The ElasticSearchRawClient is a lightweight [Elastic](https://www.elastic.co/blog/no-sql-yes-search) (formerly ElasticSearch) client module for Android devs.

It's built upon HttpsURLConnection. [Connector.java](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/main/java/com/silverforge/elasticsearchrawclient/connector/Connector.java)

The Connector applies a retry pattern, by default it tries three times to get the response from server. 

Current version : 1.0.0

## How do I get set up? ##

### Set up ###

##### Gradle build.config #####

You can find elasticrawclient in jcenter so add the following row to your dependencies in build.gradle (Module):

```groovy
dependencies {
    ...
    compile 'com.silverforge.elastic:elasticsearchrawclient:1.0.0'
}
```

##### Issues/contact #####

Please do not hesitate to raise any issue you find related to ElasticRawClient [here](https://github.com/silverforge/ElasticRawClient/issues)


### Configuration ###

Currently the ElasticClient **works only with https urls**.
Instantiate a ConnectorSettings via builder and pass it to the ElasticClient instance.

```java
private final static String ELASTIC_URL = "https://my.custom.url.to.elastic.search.io";
private final static String[] ELASTIC_INDICES = new String[] {"myindex", "mysecondindex"};
private final static String[] ELASTIC_TYPES = new String[] {"mytype", "mysecondtype"};
private final static String[] ELASTIC_APIKEY = "mySpec1alAPIK3y";
...
try {
    ConnectorSettings settings 
        = ConnectorSettings
            .builder()
            .baseUrl(ELASTIC_URL)
            .indices(ELASTIC_INDICES)
            .types(ELASTIC_TYPES)
            .userName(ELASTIC_APIKEY)
            .build();

     client = new ElasticClient(settings);
} catch (URISyntaxException e) {
     e.printStackTrace();
     Log.e(TAG, e.getMessage());
}
```

Once you have set up the client you can use this **client** instance for operating with Elastic server.

If you have defined index name(s) and/or type name(s) at *ConnectorSettings* builder, they are used for some methods under mentioned in "You have index defined in ConnectorSettings", so if you are working always on the same indices I'd suggest to set those index names in *ConnectorSettings*, but if you are working on several indices which can't be determined at the beggining of your workflow I'd suggest let the index names and type names undefined at *ConnectorSettings* and use methods mentioned at "You don't have index defined in ConnectorSettings". 

## Search/Get document(s) ##

### Search ###

You can search documents via *search* method. You have to pass the query string and the type reference for mapping. The result is a List<T> :

```java
List<City> cities = client.search("{\"query\":{\"match_all\": {}}}", City.class);
```

You can find further doc about Elastic Query Language here : [Query Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html)


You can find *search* tests on [SearchTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientSearchTest.java)


### Get ###

You can receive document(s) from elastic index/indices via *getDocument* method in many ways.

#### You have index defined in ConnectorSettings ####

If you have index defined in ConnectorSettings passed to ElasticClient, you need only to use either 

```java
<T> List<T> getDocument(String[] ids, Class<T> classType)
    throws IndexCannotBeNullException;
```
or

```java
<T> List<T> getDocument(String type, String[] ids, Class<T> classType)
    throws IndexCannotBeNullException;
```

methods like this:

```java
String[] docIds = {
        "karcag",
        "customCity"};

List<City> cities = client.getDocument(docIds, City.class);
```

or

```java
String[] docIds ={
        "karcag",
        "customCity"};

List<City> cities = customClient.getDocument("city", docIds, City.class);
```

The *type* parameter is the document type of elastic document in the index, for example index : *cities*, type : *city*.


#### You don't have index defined in ConnectorSettings ####

If you don't have index defined at ConnectorSettings you can use either

```java
<T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType);
```

or

```java
<T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType);
```

methods like this:

```java
List<City> cities = client.getDocument("testcities", "testcity", new String[]{cityId}, City.class);
```

or

```java
List<City> cities = client.getDocument(new String[] {"cities","testcities"}, "testcity", new String[]{cityId}, City.class);
```


You can find *getDocument* tests on [GetDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientGetDocumentTest.java)


## Add new document ##

The *addDocument* methods retrieve with the id of the document either it's generated by elastic or you passed it via *id* parameter.

### You have index defined in ConnectorSettings ###

If you have index defined in ConnectorSettings passed to ElasticClient, you can use either

```java
<T> String addDocument(T entity)
        throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException;
```

or

```java
<T> String addDocument(String id, T entity)
        throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException;
```

methods, like this:

```java
City city = new City(cityname, population, location);
String id = client.addDocument(city);
```

or

```java
City city = new City(cityname, population, location);
String id = client.addDocument("mySunnyCityTextAsId", city);
```


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

```java
<T> String addDocument(String index, String type, String id, T entity)
        throws IllegalArgumentException;
```

method like this:

```java
client.addDocument("cities", "city", "budapest", new City("Budapest"));
```


You can find *addDocument* tests on [AddDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientAddDocumentTest.java)


## Update document ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

```java
<T> void updateDocument(String id, T entity)
        throws IndexCannotBeNullException, TypeCannotBeNullException;
```

method like this:

```java
String cityId = "karcag";
City city = new City("karcagTest");
client.updateDocument(cityId, city);
```


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

```java
<T> void updateDocument(String index, String type, String id, T entity)
        throws IllegalArgumentException;
```

method like this:

```java
String cityId = "customCity";
City city = new City("customCityForTestingTest");
client.updateDocument("testcities", "testcity", cityId, city);
```


You can find *updateDocument* tests on [UpdateDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientUpdateDocumentTest.java)


## Remove document ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

```java
void removeDocument(String id)
        throws IllegalArgumentException, IndexCannotBeNullException, TypeCannotBeNullException;
```

method like this:

```java
String docId = "mydeldoc";
client.removeDocument(docId);
```


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

```java
void removeDocument(String index, String type, String id)
        throws IllegalArgumentException;
```

method like this:

```java
String docId = "mydeldoc";
client.removeDocument("testcities", "testcity", docId);
```


You can find *removeDocument* tests on [RemoveDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientRemoveDocumentTest.java)


## Remove documents query ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

```java
void removeDocumentsQuery(String query);
```

method like this:

```java
client.removeDocumentsQuery(query);
```


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

```java
void removeDocumentsQuery(String[] indices, String[] types, String query);
```

method like this:

```java
client.removeDocumentsQuery(new String[] {"testcities"}, new String[] {"testcity"}, query);
```


The above mentioned query string could be any query defined with [Elastic Query Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html)

For example:

```json
{
  "query": {
    "term": {
      "name": {
        "value": "MySunnyCity"
      }
    }
  }
}
```


You can find *removeDocument* tests on [RemoveDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientRemoveDocumentTest.java)


## Bulk document ##

The bulk document allows you to send multiple create/update/index/delete operations in a single call making the process cheaper. See [Cheaper in Bulk](https://www.elastic.co/guide/en/elasticsearch/guide/current/bulk.html)

You can use the following method for bulk operation : 

```java
List<BulkActionResult> bulk(List<BulkTuple> bulkItems);
```

like this:

###### Preparing operations for bulk load ######

You definitely need a list of *BulkTuple* :

```java
ArrayList<BulkTuple> bulkItems = new ArrayList<>();

bulkItems.add(BulkTuple
                .builder()
                .indexName("cities")
                .typeName("city")
                .id("szekesfehervar")
                .entity(new City("Székesfehérvár"))
                .operationType(OperationType.CREATE)
                .build()
```

* The *indexName* is the index where the operation should execute
* The *typeName* is the Elastic type of the document
* The *id* is the id of the document. It is not mandatory in case of *CREATE/INDEX*. The *id* of the document will be generated if it's not given
* The *entity* is the POJO which will be loaded to Elastic
* The *operationType* identifies the operation, such as *CREATE/UPDATE/INDEX/DELETE* 

You can add as many *BulkTuple* as you want but keep in mind that currently it will be loaded once. Currently no load balance added to the *bulk* method, so in case of huge amount of load the load can be slow. I think in case of ~100 doc it is okay, but currently you have to determine the capacity of the server by practice/experience/measurement.

###### Using the *bulk* and the result ######

If you have prepared list of bulk actions (see previous chapter) you just call *bulk* method:

```java
List<BulkActionResult> actionResults = client.bulk(bulkItems);
```

The result of the bulk load is the responses from Elastic server combined with the original *BulkTupleItem*, so you always have the connection between the original action and the result within the *actionResult*.

For example:

![BulkActionResultScreenShot](https://raw.githubusercontent.com/silverforge/ElasticRawClient/master/assets/images/BulkActionResults.png)


You can find *bulk* tests on [BulkTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientBulkTest.java)


## Create index ##

For index creation you can use this method: 

```java
boolean createIndex(String indexName, String data);
```

like this:

```java
boolean result = client.createIndex("cities", createCityData);
```

If the *result* is true the index creation was successfull.

The data can be any json string represents the mapping and/or settings of the index. 
For example:

```json
{
  "mappings": {
    "city": {
      "properties": {
        "description": {
          "type": "string"
        },
        "location": {
          "type": "geo_point",
          "fielddata": {
            "precision": "1cm",
            "format": "compressed"
          }
        },
        "name": {
          "type": "string"
        }
      }
    }
  }
}
```


For index definition please check the Elastic documentation: [Elastic Create Index](https://www.elastic.co/guide/en/elasticsearch/reference/1.6/indices-create-index.html)


You can find *createIndex* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)


## Remove index ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

```java
void removeIndices();
```

method like this:

```java
client.removeIndices();
```

It will remove all indices defined in ConnectorSettings.

Actually I was wondering the practical usage of this method, but I'd like to let it decide by you if you wanted to set index names at ConnectorSettings and later on you erase the indices all from server.

### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

```java
void removeIndices(String[] indexNames);
```

method like this:

```java
client.removeIndices(new String[] {"cities", "testcities"});
```


You can find *removeIndices* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)


## Create alias ##

You can add alias to an index with this method:

```java
void addAlias(String indexName, String aliasName);
```

like this:

```java
String aliasName = "myFunnyCities";
String indexName = "cities";
client.addAlias(indexName, aliasName);
```


You can get all added aliases on index with this method:

```java
List<String> getAliases(String index);
```

like this:

```java
String indexName = "cities";
List<String> cities = client.getAliases(indexName);
```


You can find *addAlias*, *getAliases* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)


## Remove alias ##

You can remove alias from an index with this method:

```java
void removeAlias(String indexName, String aliasName);
```

like this:

```java
String aliasName = "myFunnyCities";
String indexName = "cities";
client.removeAlias(indexName, aliasName);
```

You can find *removeAlias* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)



## "Raw" requests ##

In that case when you wanted to execute GET/POST/PUT/DELETE/HEAD requests directly against the elastic endpoint, you can use the ElasticClient *executeRawRequest* method in following way:

```java
myClient.executeRawRequest().post("/cities/city", postData);

InvokeResult head = client.executeRawRequest().head("/thereisnosuchindex");
```

The result type of the "raw" requests is the *InvokeResult* which has four properties:

```java
@Getter
@Setter
private boolean isSuccess;

@Getter
@Setter
private String statusCode;

@Getter
@Setter
private String result;

@Getter
private List<Exception> aggregatedExceptions = new ArrayList<>();
```


* The *isSuccess* is true if the request executed successfully and there is response from server.
* The *statusCode* is the response HTTP code, for example: 200, 404, 500.
* The *result* is the response from server, usually the json response from Elastic.
* The *aggregatedExceptions* list contains all exceptions happened during the request.





