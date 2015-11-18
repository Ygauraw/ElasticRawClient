package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.FunctionFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

public class Exp
        extends BaseFunction {

    Exp(QueryTypeArrayList<QueryTypeItem> queryBag) {
        super(queryBag);
    }

    @Override
    public String getFunctionString() {
        return FunctionFactory
            .expGenerator()
            .generate(queryBag);
    }

    public static Init<?> builder() {
        return new ExpBuilder();
    }

    public static class ExpBuilder
            extends Init<ExpBuilder> {

        @Override
        protected ExpBuilder self() {
            return this;
        }
    }

    public abstract static class Init<T extends Init<T>>
            extends DecayFunction.Init<T> {

        public Exp build()
            throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.FIELD_NAME))
                throw new MandatoryParametersAreMissingException(Constants.FIELD_NAME);

            if (!queryBag.containsKey(Constants.ORIGIN))
                throw new MandatoryParametersAreMissingException(Constants.ORIGIN);

            if (!queryBag.containsKey(Constants.SCALE))
                throw new MandatoryParametersAreMissingException(Constants.SCALE);

            return new Exp(queryBag);
        }
    }
}
