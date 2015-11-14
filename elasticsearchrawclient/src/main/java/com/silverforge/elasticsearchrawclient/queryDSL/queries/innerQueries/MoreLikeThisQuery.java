package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import android.app.VoiceInteractor;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.LikeDoc;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.Constants;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.generator.QueryFactory;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;
import com.silverforge.elasticsearchrawclient.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.*;

public class MoreLikeThisQuery
        implements Queryable {

    private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

    MoreLikeThisQuery(QueryTypeArrayList<QueryTypeItem> queryBag) {
        this.queryBag = queryBag;
    }

    @Override
    public String getQueryString() {
        return QueryFactory
            .moreLikeThisQueryGenerator()
            .generate(queryBag);
    }

    public static MoreLikeThisQueryBuilder builder() {
        return new MoreLikeThisQueryBuilder();
    }

    public static class MoreLikeThisQueryBuilder
            extends Init<MoreLikeThisQueryBuilder> {

        @Override
        protected MoreLikeThisQueryBuilder self() {
            return this;
        }
    }

    public static abstract class Init<T extends Init<T>> {
        private static final String TAG = MoreLikeThisQuery.Init.class.getName();
        private QueryTypeArrayList<QueryTypeItem> queryBag = new QueryTypeArrayList<>();

        protected abstract T self();

        public T fields(String... fieldNames) {
            queryBag.addItemsWithParenthesis(Constants.FIELDS, fieldNames);
            return self();
        }

        public T like(String like) {
            queryBag.addItem(Constants.LIKE, like);
            return self();
        }

        public T like(String[] likeTexts) {
            return like(null, likeTexts);
        }

        public T like(LikeDoc[] likeDocs) {
            return like(likeDocs, null);
        }

        public T like(LikeDoc[] likeDocs, String[] likeTexts) {
            Optional<LikeDoc[]> guardedLikeDocs = Optional.fromNullable(likeDocs);
            Optional<String[]> guardedLikeTexts = Optional.fromNullable(likeTexts);

            ObjectMapper mapper = new ObjectMapper();
            List<String> docJsons = stream(guardedLikeDocs.or(new LikeDoc[]{}))
                .select(ld -> {
                    String json = "{}";
                    try {
                        json = mapper.writeValueAsString(ld);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                    return json;
                })
                .toList();

            String joinedLikeTexts
                = StringUtils
                    .makeCommaSeparatedListWithQuotationMark(guardedLikeTexts.or(new String[]{}));
            docJsons.add(joinedLikeTexts);

            String value = "[" + StringUtils.makeCommaSeparatedList(docJsons) + "]";
            queryBag.addItem(Constants.LIKE, value);
            return self();
        }

        public T minTermFreq(int value) {
            queryBag.addItem(Constants.MIN_TERM_FREQ, value);
            return self();
        }

        public T minTermFreq(float value) {
            queryBag.addItem(Constants.MIN_TERM_FREQ, value);
            return self();
        }

        public T maxQueryTerms(int value) {
            queryBag.addItem(Constants.MAX_QUERY_TERMS, value);
            return self();
        }

        public T maxQueryTerms(float value) {
            queryBag.addItem(Constants.MAX_QUERY_TERMS, value);
            return self();
        }

        public MoreLikeThisQuery build()
                throws MandatoryParametersAreMissingException {

            if (!queryBag.containsKey(Constants.LIKE))
                throw new MandatoryParametersAreMissingException("like");

            return new MoreLikeThisQuery(queryBag);
        }
    }
}
