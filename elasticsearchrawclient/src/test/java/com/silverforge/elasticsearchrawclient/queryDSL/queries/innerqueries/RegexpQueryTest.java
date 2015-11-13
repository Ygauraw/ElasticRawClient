package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FlagOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class RegexpQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        RegexpQuery query = RegexpQuery
                .builder()
                .fieldName("name")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"regexp\":{\"name\":\"\"}"), greaterThan(0));
    }

    @Test
    public void when_all_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        RegexpQuery query = RegexpQuery
                .builder()
                .fieldName("name")
                .value("s.*")
                .flags(FlagOperator.INTERSECTION, FlagOperator.COMPLEMENT, FlagOperator.EMPTY)
                .boost(2)
                .max_determinized_states(10000)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"regexp\":{\"name\":"), greaterThan(0));
        assertThat(queryString.indexOf("\"value\":\"s.*\""), greaterThan(0));
        assertThat(queryString.indexOf("\"flags\":\"INTERSECTION|COMPLEMENT|EMPTY\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_determinized_states\":\"10000\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown() throws MandatoryParametersAreMissingException {
        RegexpQuery query = RegexpQuery
                .builder()
                .build();
    }

    @Test
    public void when_empty_nameField_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        RegexpQuery query = RegexpQuery
                .builder()
                .fieldName("")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"regexp\":{\"_all\":\"\"}"), greaterThan(0));
    }

    @Test
    public void when_only_value_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        RegexpQuery query = RegexpQuery
                .builder()
                .fieldName("")
                .value("s.*")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"regexp\":{\"_all\":\"s.*\"}"), greaterThan(0));
    }

    // endregion Sad path

}
