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
        ConnectorSettings settings = ConnectorSettings
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


## Search/Get document(s) ##

### Search ###

You can search documents via *search* method. You have to pass the query string and the type reference for mapping. The result is a List<T> :

        List<City> cities = client.search("{\"query\":{\"match_all\": {}}}", City.class);

You can find further doc about Elastic Query language here : https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html


You can find *search* tests on [SearchTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientSearchTest.java)


### Get ###

You can receive document(s) from elastic index/indices via *getDocument* method in many ways.

#### You have defined index in ConnectorSettings ####

If you have defined index in ConnectorSettings passed to ElasticClient, you need only to use either 

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


#### You don't have defined index in ConnectorSettings ####

If you don't have defined index at ConnecorSettings you can use either

	<T> List<T> getDocument(String index, String type, String[] ids, Class<T> classType);
or

	<T> List<T> getDocument(String[] indices, String type, String[] ids, Class<T> classType);

methods like this:

        List<City> cities = client.getDocument("testcities", "testcity", new String[]{cityId}, City.class);
or

        List<City> cities = client.getDocument(new String[] {"cities","testcities"}, "testcity", new String[]{cityId}, City.class);


You can find *getDocument* tests on [GetDocumentTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientGetDocumentTest.java)


## Add new document to index ##

## Update document ##

## Remove document ##

## Bulk document ##

## Create index ##

## Remove index ##

## Create alias ##

## Remove alias ##

## "Raw" requests ##


