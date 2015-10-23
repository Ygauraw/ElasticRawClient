package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.commonquerytemplates.MinimumShouldMatchQuery;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.List;
import static br.com.zbra.androidlinq.Linq.*;

public class CommonTermsQuery
        extends MinimumShouldMatchQuery {

    private final static String MINIMUM_SHOULD_MATCH = "minimum_should_match";

    protected CommonTermsQuery(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        super(queryTypeBag);
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

    public static CommonTermsQueryBuilder builder() {
        return new CommonTermsQueryBuilder();
    }

    public static class CommonTermsQueryBuilder extends Init<CommonTermsQueryBuilder> {
        @Override
        protected CommonTermsQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> extends MinimumShouldMatchQuery.Init<T> {
        private final static String FIELD_NAME = "FIELDNAME";
        private final static String QUERY = "query";
        private final static String CUTOFF_FREQUENCY = "cutoff_frequency";

        private final static String LOW_FREQ = "low_freq";
        private final static String HIGH_FREQ = "high_freq";

        private final static String LOW_FREQ_OPERATOR = "low_freq_operator";
        private final static String HIGH_FREQ_OPERATOR = "high_freq_operator";

        public T field(String fieldName) {
            if (!queryTypeBag.containsKey(FIELD_NAME))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(FIELD_NAME)
                    .value(fieldName)
                    .isParent(true)
                    .build());
            return self();
        }

        public T query(String query) {
            if (!queryTypeBag.containsKey(QUERY))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(QUERY)
                    .value(StringUtils.ensureNotNull(query))
                    .build());
            return self();
        }

        public T cutoffFrequency(ZeroToOneRangeOperator cutOffFrequencyOperator) {
            if (!queryTypeBag.containsKey(CUTOFF_FREQUENCY))
                queryTypeBag.add(QueryTypeItem.builder().name(CUTOFF_FREQUENCY).value(cutOffFrequencyOperator.toString()).build());
            return self();
        }

        // region Low Freq

        public T lowFreq(int value) {
            if (!queryTypeBag.containsKey(LOW_FREQ))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(LOW_FREQ)
                    .value(Integer.toString(value))
                    .build());
            return self();
        }

        public T lowFreqPercentage(int value) {
            if (!queryTypeBag.containsKey(LOW_FREQ))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(LOW_FREQ)
                    .value(value + "%")
                    .build());
            return self();
        }

        public T lowFreqPercentage(float value) {
            if (!queryTypeBag.containsKey(LOW_FREQ)) {
                String percentage = (value * 100) + "%";
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(LOW_FREQ)
                    .value(percentage)
                    .build());
            }
            return self();
        }

        public T lowFreqCombination(String expression) {
            if (!queryTypeBag.containsKey(LOW_FREQ))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(LOW_FREQ)
                    .value(StringUtils.ensureNotNull(expression))
                    .build());
            return self();
        }

        // endregion

        // region High Freq

        public T highFreq(int value) {
            if (!queryTypeBag.containsKey(HIGH_FREQ))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(HIGH_FREQ)
                    .value(Integer.toString(value))
                    .build());
            return self();
        }

        public T highFreqPercentage(int value) {
            if (!queryTypeBag.containsKey(HIGH_FREQ))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(HIGH_FREQ)
                    .value(value + "%")
                    .build());
            return self();
        }

        public T highFreqPercentage(float value) {
            if (!queryTypeBag.containsKey(HIGH_FREQ)) {
                String percentage = (value * 100) + "%";
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(LOW_FREQ)
                    .value(percentage)
                    .build());
            }
            return self();
        }

        public T highFreqCombination(String expression) {
            if (!queryTypeBag.containsKey(HIGH_FREQ))
                queryTypeBag.add(QueryTypeItem
                    .builder()
                    .name(HIGH_FREQ)
                    .value(StringUtils.ensureNotNull(expression))
                    .build());
            return self();
        }

        // endregion

        public T lowFreqOperator(LogicOperator lowFreqOperator) {
            if (!queryTypeBag.containsKey(LOW_FREQ_OPERATOR))
                queryTypeBag.add(QueryTypeItem.builder().name(LOW_FREQ_OPERATOR).value(lowFreqOperator.toString()).build());
            return self();
        }

        public T highFreqOperator(LogicOperator highFreqOperator) {
            if (!queryTypeBag.containsKey(HIGH_FREQ_OPERATOR))
                queryTypeBag.add(QueryTypeItem.builder().name(HIGH_FREQ_OPERATOR).value(highFreqOperator.toString()).build());
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

        boolean haveFreqKeys = queryTypeBag.hasAtLeastOneKey(Init.LOW_FREQ, Init.HIGH_FREQ);
        boolean hasMinKey = queryTypeBag.hasKeys(MINIMUM_SHOULD_MATCH);

        if (hasMinKey) {
            if (list.size() > 0)
                queryString.append(",");

            QueryTypeItem minItem = queryTypeBag.getByKey(MINIMUM_SHOULD_MATCH);
            queryString
                .append("\"")
                .append(minItem.getName())
                .append("\":\"")
                .append(minItem.getValue())
                .append("\"");
        } else if (haveFreqKeys) {
            if (list.size() > 0)
                queryString.append(",");

            queryString.append("\"").append(MINIMUM_SHOULD_MATCH).append("\":{");

            List<QueryTypeItem> freqItems = stream(nonParentItems)
                .where(i -> i.getName().equals(Init.LOW_FREQ) || i.getName().equals(Init.HIGH_FREQ))
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
            .where(i -> !i.getName().equals(MINIMUM_SHOULD_MATCH))
            .where(i -> !i.getName().equals(Init.LOW_FREQ))
            .where(i -> !i.getName().equals(Init.HIGH_FREQ))
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
