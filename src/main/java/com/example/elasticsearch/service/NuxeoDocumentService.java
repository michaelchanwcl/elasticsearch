package com.example.elasticsearch.service;

import com.example.elasticsearch.document.NuxeoDocument;
import com.example.elasticsearch.helper.Indices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NuxeoDocumentService {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final RestHighLevelClient client;

    @Autowired
    public NuxeoDocumentService(RestHighLevelClient client) {
        this.client = client;
    }

    public NuxeoDocument getById(final String documentId) {
        try {
            final GetResponse documentFields = client.get(new GetRequest(Indices.NUXEO_DOCUMENT_INDEX, "doc", documentId),
                    RequestOptions.DEFAULT);
            if (documentFields == null) {
                return null;
            }

            NuxeoDocument nuxeoDocument =  MAPPER.readValue(documentFields.getSourceAsString(), NuxeoDocument.class);
            nuxeoDocument.set_id(documentFields.getId());
            nuxeoDocument.set_type(documentFields.getType());
            nuxeoDocument.set_version(documentFields.getVersion());
            return nuxeoDocument;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public List<NuxeoDocument> getAll() {
        List<NuxeoDocument> nuxeoDocumentList = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(Indices.NUXEO_DOCUMENT_INDEX);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.size(100);

            QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("*尼樂園度假區*").defaultField("*");
//            QueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("all_field", "");
            sourceBuilder.query(queryBuilder);
            searchRequest.source(sourceBuilder);
            log.info("Search JSON query: {}", searchRequest.source().toString());
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();
            log.info("Total Hits=[" + totalHits + "]");
            log.info("Total Score=[" + maxScore + "]");

            SearchHit[] searchHits = hits.getHits();
            log.info("Search Result Size=[" + searchHits.length + "]");
            for (SearchHit hit : searchHits) {
                String sourceAsString = hit.getSourceAsString();
                NuxeoDocument nuxeoDocument =  MAPPER.readValue(sourceAsString, NuxeoDocument.class);
                nuxeoDocument.set_id(hit.getId());
                nuxeoDocument.set_type(hit.getType());
                nuxeoDocument.set_version(hit.getVersion());
                nuxeoDocumentList.add(nuxeoDocument);
            }
            return nuxeoDocumentList;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}