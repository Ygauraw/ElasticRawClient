# README #

### What is this repository for? ###

The ElasticSearchRawClient is a lightweight ElasticSearch client module for Android devs.

Current version : 1.0

### How do I get set up? ###

#### Set up ####
Currently there is no gradle or maven setup for this repo (I'm working on it, and will be :)) so  currently download this module and add

	compile project(':elasticsearchrawclient')

to your build.gradle.

#### Configuration ####

Currently the ElasticClient works only with https urls.
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


Once you have set up the client you can use this **client** instance for operating with ElasticSearch server.
