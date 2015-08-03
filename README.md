# README #

### What is this repository for? ###

The ElasticSearchRawClient is a lightweight ElasticSearch client module for Android devs.

Current version : 1.0

### How do I get set up? ###

[TBD]

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

#### Set up ####
Currently there is no gradle or maven setup for this repo (I'm working on it, and will be :)) so  currently download this module and add

	compile project(':elasticsearchrawclient')

to your build.gradle.

#### Configuration ####

Currently the ElasticClient works only with https urls.
Instantiate a ConnectorSettings via builder and pass it to the ElasticClient instance.

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