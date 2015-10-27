package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.util.List;
import static br.com.zbra.androidlinq.Linq.*;

public class CommonTermsQuery
        extends MinimumShouldMatchQuery {

    private QueryTypeArrayList<QueryTypeItem> queryBag;

    protected CommonTermsQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        List<QueryTypeItem> parentItems = stream(queryBag)
            .where(i -> i.isParent())
            .toList();

        List<QueryTypeItem> nonParentItems = stream(queryBag)
            .where(i -> !i.isParent())
            .toList();

        StringBuilder queryString = new StringBuilder();
        queryString.append("{\"common\":{\"");

        if (parentItems.size() == 0)
            queryString.append("_all");
        else {
            String value = parentItems.get(0).getValue();
            queryString.append(value);
        }
        queryString.append("\":{");

        List<QueryTypeItem> list = addCommonQueryTypeItems(nonParentItems, queryString);
        addMinimumShouldMatchItems(nonParentItems, queryString, list);

        queryString.append("}}}");
        return queryString.toString();
    }

    public static Init<?> builder() {
        return new CommonTermsQueryBuilder();
    }

    public static class CommonTermsQueryBuilder extends Init<CommonTermsQueryBuilder> {
        @Override
        protected CommonTermsQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.MinimumShouldMatchInit<T> {

        public T field(String fieldName) {
            queryBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T query(String query) {
            queryBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T cutoffFrequency(ZeroToOneRangeOperator cutOffFrequencyOperator) {
            String value = cutOffFrequencyOperator.toString();
            queryBag.addItem(Constants.CUTOFF_FREQUENCY, value);
            return self();
        }

        // region Low Freq

        public T lowFreq(int value) {
            queryBag.addItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqPercentage(int value) {
            queryBag.addPercentageItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqPercentage(float value) {
            queryBag.addPercentageItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqCombination(String expression) {
            queryBag.addItem(Constants.LOW_FREQ, expression);
            return self();
        }

        // endregion

        // region High Freq

        public T highFreq(int value) {
            queryBag.addItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqPercentage(int value) {
            queryBag.addPercentageItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqPercentage(float value) {
            queryBag.addPercentageItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqCombination(String expression) {
            queryBag.addItem(Constants.HIGH_FREQ, expression);
            return self();
        }

        // endregion

        public T lowFreqOperator(LogicOperator lowFreqOperator) {
            String value = lowFreqOperator.toString();
            queryBag.addItem(Constants.LOW_FREQ_OPERATOR, value);
            return self();
        }

        public T highFreqOperator(LogicOperator highFreqOperator) {
            String value = highFreqOperator.toString();
            queryBag.addItem(Constants.HIGH_FREQ_OPERATOR, value);
            return self();
        }

        public CommonTermsQuery build() {
            return new CommonTermsQuery(queryBag);
        }
    }

    private void addMinimumShouldMatchItems(
        List<QueryTypeItem> nonParentItems,
        StringBuilder queryString,
        List<QueryTypeItem> list) {

        boolean haveFreqKeys = queryBag.hasAtLeastOneKey(Constants.LOW_FREQ, Constants.HIGH_FREQ);
        boolean hasMinKey = queryBag.hasKeys(Constants.MINIMUM_SHOULD_MATCH);

        if (hasMinKey) {
            if (list.size() > 0)
                queryString.append(",");

            QueryTypeItem minItem = queryBag.getByKey(Constants.MINIMUM_SHOULD_MATCH);
            queryString
                .append("\"")
                .append(minItem.getName())
                .append("\":\"")
                .append(minItem.getValue())
                .append("\"");
        } else if (haveFreqKeys) {
            if (list.size() > 0)
                queryString.append(",");

            queryString.append("\"").append(Constants.MINIMUM_SHOULD_MATCH).append("\":{");

            List<QueryTypeItem> freqItems = stream(nonParentItems)
                .where(i -> i.getName().equals(Constants.LOW_FREQ) || i.getName().equals(Constants.HIGH_FREQ))
                .toList();

            for(int i = 0; i < freqItems.size(); i++) {
                if (i > 0)
                    queryString.append(",");

                QueryTypeItem item = freqItems.get(i);
                queryString
                    .append("\"")
                    .append(item.getName())
                    .append("\":\"")
                    .append(item.getValue())
                    .append("\"");
            }

            queryString.append("}");
        }
    }

    private List<QueryTypeItem> addCommonQueryTypeItems(
        List<QueryTypeItem> nonParentItems,
        StringBuilder queryString) {

        List<QueryTypeItem> list = stream(nonParentItems)
            .where(i -> !i.getName().equals(Constants.MINIMUM_SHOULD_MATCH))
            .where(i -> !i.getName().equals(Constants.LOW_FREQ))
            .where(i -> !i.getName().equals(Constants.HIGH_FREQ))
            .toList();

        for(int i = 0; i < list.size(); i++) {
            if (i > 0)
                queryString.append(",");

            QueryTypeItem item = list.get(i);
            queryString
                .append("\"")
                .append(item.getName())
                .append("\":\"")
                .append(item.getValue())
                .append("\"");
        }

        return list;
    }
}
