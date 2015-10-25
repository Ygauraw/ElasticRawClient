package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.common.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.List;
import static br.com.zbra.androidlinq.Linq.*;

public class CommonTermsQuery
        extends MinimumShouldMatchQuery {

    private QueryTypeArrayList<QueryTypeItem> queryTypeBag;

    protected CommonTermsQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        this.queryTypeBag = queryTypeBag;
    }

    @Override
    public String getQueryString() {
        List<QueryTypeItem> parentItems = stream(queryTypeBag)
            .where(i -> i.isParent())
            .toList();

        List<QueryTypeItem> nonParentItems = stream(queryTypeBag)
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
            queryTypeBag.addParentItem(Constants.FIELD_NAME, fieldName);
            return self();
        }

        public T query(String query) {
            queryTypeBag.addItem(Constants.QUERY, query);
            return self();
        }

        public T cutoffFrequency(ZeroToOneRangeOperator cutOffFrequencyOperator) {
            String value = cutOffFrequencyOperator.toString();
            queryTypeBag.addItem(Constants.CUTOFF_FREQUENCY, value);
            return self();
        }

        // region Low Freq

        public T lowFreq(int value) {
            queryTypeBag.addItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqPercentage(int value) {
            queryTypeBag.addPercentageItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqPercentage(float value) {
            queryTypeBag.addPercentageItem(Constants.LOW_FREQ, value);
            return self();
        }

        public T lowFreqCombination(String expression) {
            queryTypeBag.addItem(Constants.LOW_FREQ, expression);
            return self();
        }

        // endregion

        // region High Freq

        public T highFreq(int value) {
            queryTypeBag.addItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqPercentage(int value) {
            queryTypeBag.addPercentageItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqPercentage(float value) {
            queryTypeBag.addPercentageItem(Constants.HIGH_FREQ, value);
            return self();
        }

        public T highFreqCombination(String expression) {
            queryTypeBag.addItem(Constants.HIGH_FREQ, expression);
            return self();
        }

        // endregion

        public T lowFreqOperator(LogicOperator lowFreqOperator) {
            String value = lowFreqOperator.toString();
            queryTypeBag.addItem(Constants.LOW_FREQ_OPERATOR, value);
            return self();
        }

        public T highFreqOperator(LogicOperator highFreqOperator) {
            String value = highFreqOperator.toString();
            queryTypeBag.addItem(Constants.HIGH_FREQ_OPERATOR, value);
            return self();
        }

        public CommonTermsQuery build() {
            return new CommonTermsQuery(queryTypeBag);
        }
    }

    private void addMinimumShouldMatchItems(
        List<QueryTypeItem> nonParentItems,
        StringBuilder queryString,
        List<QueryTypeItem> list) {

        boolean haveFreqKeys = queryTypeBag.hasAtLeastOneKey(Constants.LOW_FREQ, Constants.HIGH_FREQ);
        boolean hasMinKey = queryTypeBag.hasKeys(Constants.MINIMUM_SHOULD_MATCH);

        if (hasMinKey) {
            if (list.size() > 0)
                queryString.append(",");

            QueryTypeItem minItem = queryTypeBag.getByKey(Constants.MINIMUM_SHOULD_MATCH);
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
