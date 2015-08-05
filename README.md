# README #

### What is this repository for? ###

The ElasticSearchRawClient is a lightweight [Elastic](https://www.elastic.co/blog/no-sql-yes-search) (formerly ElasticSearch) client module for Android devs.

It's built upon HttpsURLConnection. [Connector.java](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/main/java/com/silverforge/elasticsearchrawclient/connector/Connector.java)


Current version : 1.0

### How do I get set up? ###

#### Set up ####
Currently there is no gradle or maven setup for this repo (I'm working on it, and will be :)) so  currently download this module and add

	compile project(':elasticsearchrawclient')

to your build.gradle.

#### Configuration ####

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


### Search/Get document(s) ###

#### Search ####

You can search documents via *search* method. You have to pass the query string and the type reference for mapping. The result is a List<T> :

        List<City> cities = client.search("{\"query\":{\"match_all\": {}}}", City.class);

You can find further doc about Elastic Query language here : https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html


You can find *search* tests on [SearchTests](https://github.com/silverforge/ElasticRawClient/blob/master/elasticsearchrawclient/src/test/java/com/silverforge/elasticsearchrawclient/elasticFacade/ElasticClientSearchTest.java)


#### Get ###

You can receive document(s) from elastic index/indices via *getDocument* method in many ways.

##### You have defined index in ConnctionSettings #####

If you have defined index in ConnectionSettings passed to ElasticClient, you need only to use either 

	<T> List<T> getDocument(String[] ids, Class<T> classType)
                            throws IndexCannotBeNullException;
or
	<T> List<T> getDocument(String type, String[] ids, Class<T> classType)
                                    throws IndexCannotBeNullException;

methods like this:

        try {
            String[] docIds = {
                    "karcag",
                    "customCity"};

            List<City> cities = client.getDocument(docIds, City.class);

            assertThat(cities, notNullValue());
            assertThat(cities.size(), greaterThan(0));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


or

        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .indices(new String[]{"cities", "testcities"})
                .userName(ELASTIC_APIKEY)
                .build();
        try {
            ElasticRawClient customClient = new ElasticClient(customSettings);

            List<City> cities = customClient.getDocument("city", docIds, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
        } catch (URISyntaxException | IndexCannotBeNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }

The *type* parameter is the document type of elastic document in the index, for example index : *cities*, type : *city*.


##### You don't have defined index in ConnectionSettings #####

TBC

