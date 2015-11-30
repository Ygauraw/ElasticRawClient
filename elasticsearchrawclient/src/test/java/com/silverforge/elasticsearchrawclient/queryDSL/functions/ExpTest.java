package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.FunctionTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MultiValueModeOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = FunctionTest.class)
public class ExpTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_function_generated_well()
            throws MandatoryParametersAreMissingException {

        String functionString = Exp
            .builder()
            .fieldName("established")
            .origin("2013-09-17")
            .scale("10d")
            .offset("5d")
            .decay(0.5f)
            .multiValueMode(MultiValueModeOperator.MIN)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{\"exp\":{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"established\":{"), greaterThan(0));
        assertThat(functionString.indexOf("\"origin\":\"2013-09-17\""), greaterThan(0));
        assertThat(functionString.indexOf("\"scale\":\"10d\""), greaterThan(0));
        assertThat(functionString.indexOf("\"offset\":\"5d\""), greaterThan(0));
        assertThat(functionString.indexOf("\"decay\":\"0.5\""), greaterThan(0));
        assertThat(functionString.indexOf("\"multi_value_mode\":\"min\""), greaterThan(0));
    }

    @Test
    public void when_minimum_required_parameters_defined_then_function_generated_well()
            throws MandatoryParametersAreMissingException {

        String functionString = Exp
            .builder()
            .fieldName("established")
            .origin("2013-09-17")
            .scale("10d")
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{\"exp\":{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"established\":{"), greaterThan(0));
        assertThat(functionString.indexOf("\"origin\":\"2013-09-17\""), greaterThan(0));
        assertThat(functionString.indexOf("\"scale\":\"10d\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_none_of_required_parameters_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        Exp
            .builder()
            .build()
            .getFunctionString();
    }

    @Test
    public void when_origin_parameter_is_not_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        expectedException.expect(MandatoryParametersAreMissingException.class);
        expectedException.expectMessage("The following parameters are missing : origin");

        Exp
            .builder()
            .fieldName("established")
            .scale("10d")
            .build()
            .getFunctionString();
    }

    @Test
    public void when_scale_parameter_is_not_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        expectedException.expect(MandatoryParametersAreMissingException.class);
        expectedException.expectMessage("The following parameters are missing : scale");

        Exp
            .builder()
            .fieldName("established")
            .origin("2013-09-17")
            .build()
            .getFunctionString();
    }

    @Test
    public void when_field_parameter_is_not_defined_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        expectedException.expect(MandatoryParametersAreMissingException.class);
        expectedException.expectMessage("The following parameters are missing : FIELDNAME");

        Exp
            .builder()
            .origin("2013-09-17")
            .scale("10d")
            .build()
            .getFunctionString();
    }

    // endregion
}
