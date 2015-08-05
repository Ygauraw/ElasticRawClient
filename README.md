# README #

## What is this repository for? ##

The ElasticSearchRawClient is a lightweight [Elastic](https://www.elastic.co/blog/no-sql-yes-search) (formerly ElasticSearch) client module for Android devs.

It's built upon HttpsURLConnection. [Connector.java](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/main/java/com/silverforge/elasticsearchrawclient/connector/Connector.java)

The Connector applies a retry pattern, by default it tries three times to get the response from server. 

Current version : 1.0

## How do I get set up? ##

### Set up ###
Currently there is no gradle or maven setup for this repo (I'm working on it, and will be :)) so  currently download this module and add

	compile project(':elasticsearchrawclient')

to your build.gradle.

### Configuration ###

Currently the ElasticClient **works only with https urls**.
Instantiate a ConnectorSettings via builder and pass it to the ElasticClient instance.

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


Once you have set up the client you can use this **client** instance for operating with Elastic server.

If you have defined index name(s) and/or type name(s) at *ConnectorSettings* builder, they are used for some methods under mentioned in "You have index defined in ConnectorSettings", so if you are working always on the same indices I'd suggest to set those index names in *ConnectorSettings*, but if you are working on several indices which can't be determined at the beggining of your workflow I'd suggest let the index names and type names undefined at *ConnectorSettings* and use methods mentioned at "You don't have index defined in ConnectorSettings". 

## Search/Get document(s) ##

### Search ###

You can search documents via *search* method. You have to pass the query string and the type reference for mapping. The result is a List<T> :

        List<City> cities = client.search("{\"query\":{\"match_all\": {}}}", City.class);

You can find further doc about Elastic Query Language here : [Query Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html)


You can find *search* tests on [SearchTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientSearchTest.java)


### Get ###

You can receive document(s) from elastic index/indices via *getDocument* method in many ways.

#### You have index defined in ConnectorSettings ####

If you have index defined in ConnectorSettings passed to ElasticClient, you need only to use either 

        <T> List<T> getDocument(String[] ids, Class<T> classType)
                throws IndexCannotBeNullException;
or

        <T> List<T> getDocument(String type, String[] ids, Class<T> classType)
                throws IndexCannotBeNullException;

methods like this:

        String[] docIds = {
                "karcag",
                "customCity"};

        List<City> cities = client.getDocument(docIds, City.class);


or

        String[] docIds ={
                "karcag",
                "customCity"};

        List<City> cities = customClient.getDocument("city", docIds, City.class);

The *type* parameter is the document type of elastic document in the index, for example index : *cities*, type : *city*.


#### You don't have index defined in ConnectorSettings ####

If you don't have index defined at ConnectorSettings you can use either

        <T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType);
or

        <T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType);

methods like this:

        List<City> cities = client.getDocument("testcities", "testcity", new String[]{cityId}, City.class);
or

        List<City> cities = client.getDocument(new String[] {"cities","testcities"}, "testcity", new String[]{cityId}, City.class);


You can find *getDocument* tests on [GetDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientGetDocumentTest.java)


## Add new document ##

The *addDocument* methods retrieve with the id of the document either it's generated by elastic or you passed it via *id* parameter.

### You have index defined in ConnectorSettings ###

If you have index defined in ConnectorSettings passed to ElasticClient, you can use either

	<T> String addDocument(T entity)
		throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException;
or

	<T> String addDocument(String id, T entity)
                throws IndexCannotBeNullException, IllegalArgumentException, TypeCannotBeNullException;

methods, like this:

        City city = new City(cityname, population, location);
        String id = client.addDocument(city);
or

        City city = new City(cityname, population, location);
        String id = client.addDocument("mySunnyCityTextAsId", city);


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

        <T> String addDocument(String index, String type, String id, T entity)
                throws IllegalArgumentException;

method like this:

        client.addDocument("cities", "city", "budapest", new City("Budapest"));


You can find *addDocument* tests on [AddDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientAddDocumentTest.java)


## Update document ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

        <T> void updateDocument(String id, T entity)
                throws IndexCannotBeNullException, TypeCannotBeNullException;

method like this:

        String cityId = "karcag";
        City city = new City("karcagTest");
        client.updateDocument(cityId, city);



### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

        <T> void updateDocument(String index, String type, String id, T entity)
                throws IllegalArgumentException;

method like this:

        String cityId = "customCity";
        City city = new City("customCityForTestingTest");
        client.updateDocument("testcities", "testcity", cityId, city);


You can find *updateDocument* tests on [UpdateDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientUpdateDocumentTest.java)


## Remove document ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

        void removeDocument(String id)
                throws IllegalArgumentException, IndexCannotBeNullException, TypeCannotBeNullException;

method like this:

        String docId = "mydeldoc";
        client.removeDocument(docId);


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

        void removeDocument(String index, String type, String id)
                throws IllegalArgumentException;

method like this:

        String docId = "mydeldoc";
        client.removeDocument("testcities", "testcity", docId);


You can find *removeDocument* tests on [RemoveDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientRemoveDocumentTest.java)


## Remove documents query ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

        void removeDocumentsQuery(String query);

method like this:

        client.removeDocumentsQuery(query);


### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

        void removeDocumentsQuery(String[] indices, String[] types, String query);

method like this:

        client.removeDocumentsQuery(new String[] {"testcities"}, new String[] {"testcity"}, query);


The above mentioned query string could be any query defined with [Elastic Query Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html)

For example:

        {
          "query": {
            "term": {
              "name": {
                "value": "MySunnyCity"
              }
            }
          }
        }


You can find *removeDocument* tests on [RemoveDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientRemoveDocumentTest.java)


## Bulk document ##

**Dev in progress**

## Create index ##

For index creation you can use this method: 

        boolean createIndex(String indexName, String data);

like this:

        boolean result = client.createIndex("cities", createCityData);

If the *result* is true the index creation was successfull.

The data can be any json string represents the mapping and/or settings of the index. 
For example:

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


For index definition please check the Elastic documentation: [Elastic Create Index](https://www.elastic.co/guide/en/elasticsearch/reference/1.6/indices-create-index.html)


You can find *createIndex* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)


## Remove index ##

### You have index defined in ConnectorSettings ###

If you have index defined at ConnectorSettings you can use:

        void removeIndices();

method like this:

        client.removeIndices();

It will remove all indices defined in ConnectorSettings.

Actually I was wondering the practical usage of this method, but I'd like to let it decide by you if you wanted to set index names at ConnectorSettings and later on you erase the indices all from server.

### You don't have index defined in ConnectorSettings ###

If you don't have index defined at ConnectorSettings you can use:

        void removeIndices(String[] indexNames);

method like this:

        client.removeIndices(new String[] {"cities", "testcities"});


You can find *removeIndices* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)


## Create alias ##

You can add alias to an index with this method:

        void addAlias(String indexName, String aliasName);

like this:

        String aliasName = "myFunnyCities";
        String indexName = "cities";
        client.addAlias(indexName, aliasName);


You can get all added aliases on index with this method:

        List<String> getAliases(String index);

like this:

        String indexName = "cities";
        List<String> cities = client.getAliases(indexName);


You can find *addAlias*, *getAliases* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)


## Remove alias ##

You can remove alias from an index with this method:

        void removeAlias(String indexName, String aliasName);

like this:

        String aliasName = "myFunnyCities";
        String indexName = "cities";
        client.removeAlias(indexName, aliasName);

You can find *removeAlias* tests on [CreateIndexTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientCreateIndexTest.java)



## "Raw" requests ##

In that case when you wanted to execute GET/POST/PUT/DELETE/HEAD requests directly against the elastic endpoint, you can use the ElasticClient *executeRawRequest* method in following way:

        myClient.executeRawRequest().post("/cities/city", postData);

        InvokeResult head = client.executeRawRequest().head("/thereisnosuchindex");

The result type of the "raw" requests is the *InvokeResult* which has four properties:

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


* The *isSuccess* is true if the request executed successfully and there is response from server.
* The *statusCode* is the response HTTP code, for example: 200, 404, 500.
* The *result* is the response from server, usually the json response from Elastic.
* The *aggregatedExceptions* list contains all exceptions happened during the request.





