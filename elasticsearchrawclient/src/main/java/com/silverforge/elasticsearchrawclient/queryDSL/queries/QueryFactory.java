package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.silverforge.elasticsearchrawclient.model.QueryTypeItem;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Generator;
import com.silverforge.elasticsearchrawclient.utils.QueryTypeArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;

public final class QueryFactory {
    public static MatchQueryGenerator matchQueryGenerator(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
        return new MatchQueryGenerator(queryTypeBag);
    }

    public final static class MatchQueryGenerator
            implements Generator {

        private QueryTypeArrayList<QueryTypeItem> queryTypeBag;

        public MatchQueryGenerator(QueryTypeArrayList<QueryTypeItem> queryTypeBag) {
            this.queryTypeBag = queryTypeBag;
        }

        public String generate() {
            Map<String, String> childItems = stream(queryTypeBag)
                .where(q -> !q.isParent())
                .toMap(q -> q.getName(), q -> q.getValue());

            QueryTypeItem parent = stream(queryTypeBag)
                .firstOrNull(q -> q.isParent());

            String retValue = "";

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                JsonFactory jsonFactory = new JsonFactory();
                JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);

                jsonGenerator.writeStartObject();

                jsonGenerator.writeObjectFieldStart("match");

                if (parent != null
                        && stream(childItems).any(i -> i.getKey().equals(Constants.VALUE))) {

                    jsonGenerator.writeStringField(parent.getValue(),
                            stream(childItems).first(i -> i.getKey().equals(Constants.VALUE))
                        .getValue());
                } else {

                    jsonGenerator.writeObjectFieldStart(parent.getValue());

                    for (Map.Entry<String, String> entry : childItems.entrySet()) {
                        jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
                    }

                    jsonGenerator.writeEndObject();
                }

                jsonGenerator.writeEndObject();

                jsonGenerator.writeEndObject();
                jsonGenerator.close();

                retValue = outputStream.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return retValue;
        }
    }
}
