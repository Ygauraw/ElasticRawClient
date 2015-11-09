package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.LikeDoc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MoreLikeThisQueryTest {

    // region Happy path

    @Test
    public void when_simple_like_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = MoreLikeThisQuery
            .builder()
            .fields("apple", "banana", "cherry", "date")
            .like("Quick brown fox jumps over the lazy dog.")
            .minTermFreq(3)
            .maxQueryTerms(6)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"more_like_this\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"fields\":[\"apple\",\"banana\",\"cherry\",\"date\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"like\":\"Quick brown fox jumps over the lazy dog.\""), greaterThan(0));
        assertThat(queryString.indexOf("\"min_term_freq\":\"3\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_query_terms\":\"6\""), greaterThan(0));
    }

    @Test
    public void when_complicated_like_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        LikeDoc likeDoc1 = LikeDoc
            .builder()
            .index("zeta")
            .type("teta")
            .id("ty-87")
            .build();

        LikeDoc likeDoc2 = LikeDoc
            .builder()
            .index("jotta")
            .type("peta")
            .doc("{\"name\":{\"first\":\"Janos Gedeon\", \"last\":\"Murvai-Gaal\"}, \"age\":37}")
            .build();

        String queryString = MoreLikeThisQuery
            .builder()
            .fields("apple", "banana", "cherry", "date")
            .like(new LikeDoc[] { likeDoc1, likeDoc2 }, new String[] { "Free text 1", "Free text 2" })
            .minTermFreq(3.67f)
            .maxQueryTerms(6.2134f)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"fields\":[\"apple\",\"banana\",\"cherry\",\"date\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"min_term_freq\":\"3.67\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_query_terms\":\"6.2134\""), greaterThan(0));

        assertThat(queryString.indexOf("\"like\":["), greaterThan(0));

        assertThat(queryString.indexOf("\"_index\":\"zeta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_type\":\"teta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_id\":\"ty-87\""), greaterThan(0));

        assertThat(queryString.indexOf("\"_index\":\"jotta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_type\":\"peta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"doc\":{\"name\":{\"first\":\"Janos Gedeon\", \"last\":\"Murvai-Gaal\"}, \"age\":37}"), greaterThan(0));

        assertThat(queryString.indexOf("\"Free text 1\""), greaterThan(0));
        assertThat(queryString.indexOf("\"Free text 2\""), greaterThan(0));
    }

    @Test
    public void when_complicated_like_added_without_like_docs_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = MoreLikeThisQuery
            .builder()
            .fields("apple", "banana", "cherry", "date")
            .like(new String[]{"Free text 1", "Free text 2"})
            .minTermFreq(3.67f)
            .maxQueryTerms(6.2134f)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"fields\":[\"apple\",\"banana\",\"cherry\",\"date\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"min_term_freq\":\"3.67\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_query_terms\":\"6.2134\""), greaterThan(0));

        assertThat(queryString.indexOf("\"like\":["), greaterThan(0));

        assertThat(queryString.indexOf("\"Free text 1\""), greaterThan(0));
        assertThat(queryString.indexOf("\"Free text 2\""), greaterThan(0));


        assertThat(queryString.indexOf("\"_index\""), is(-1));
        assertThat(queryString.indexOf("\"_type\""), is(-1));
        assertThat(queryString.indexOf("\"_id\""), is(-1));
        assertThat(queryString.indexOf("\"doc\""), is(-1));
    }

    @Test
    public void when_complicated_like_added_without_free_text_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        LikeDoc likeDoc1 = LikeDoc
            .builder()
            .index("zeta")
            .type("teta")
            .id("ty-87")
            .build();

        LikeDoc likeDoc2 = LikeDoc
            .builder()
            .index("jotta")
            .type("peta")
            .doc("{\"name\":{\"first\":\"Janos Gedeon\", \"last\":\"Murvai-Gaal\"}, \"age\":37}")
            .build();

        String queryString = MoreLikeThisQuery
            .builder()
            .fields("apple", "banana", "cherry", "date")
            .like(new LikeDoc[] { likeDoc1, likeDoc2 })
            .minTermFreq(3.67f)
            .maxQueryTerms(6.2134f)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"fields\":[\"apple\",\"banana\",\"cherry\",\"date\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"min_term_freq\":\"3.67\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_query_terms\":\"6.2134\""), greaterThan(0));

        assertThat(queryString.indexOf("\"like\":["), greaterThan(0));

        assertThat(queryString.indexOf("\"_index\":\"zeta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_type\":\"teta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_id\":\"ty-87\""), greaterThan(0));

        assertThat(queryString.indexOf("\"_index\":\"jotta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_type\":\"peta\""), greaterThan(0));
        assertThat(queryString.indexOf("\"doc\":{\"name\":{\"first\":\"Janos Gedeon\", \"last\":\"Murvai-Gaal\"}, \"age\":37}"), greaterThan(0));

        assertThat(queryString.indexOf("\"Free text 1\""), is(-1));
        assertThat(queryString.indexOf("\"Free text 2\""), is(-1));
    }

    //endregion

    // region Sad path

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test()
    public void when_no_params_defined_then_query_throws_exception()
            throws MandatoryParametersAreMissingException {

        expectedException.expect(MandatoryParametersAreMissingException.class);
        expectedException.expectMessage("The following parameters are missing : like");

        MoreLikeThisQuery
            .builder()
            .build()
            .getQueryString();
    }

    @Test()
    public void when_only_the_like_param_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = MoreLikeThisQuery
            .builder()
            .like("apple")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"like\":\"apple\""), greaterThan(0));
    }

    // endregion
}
