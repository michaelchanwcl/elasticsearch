package com.example.elasticsearch.service;

import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Util;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class IndexService {
    private final List<String> INDEXES_TO_CREATE = Arrays.asList(Indices.VEHICLE_INDEX);
    private final RestHighLevelClient client;

    @Autowired
    public IndexService(RestHighLevelClient client) {
        this.client = client;
    }

    @PostConstruct
    public void tryToCreateIndices() {
        final String settings = Util.loadAsString("static/es-settings.json");
        for (final String indexName : INDEXES_TO_CREATE)
            try {
                GetIndexRequest indexRequest = new GetIndexRequest();
                indexRequest = indexRequest.indices(indexName);
                boolean indexExists = client.indices().exists(indexRequest, RequestOptions.DEFAULT);
                if (indexExists) {
                    continue;
                }

                final String mappings = Util.loadAsString("static/mappings/" + indexName + ".json");
                if (settings == null || mappings == null) {
                    log.error("Filed to create index with name '{}'", indexName);
                    continue;
                }
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(settings, XContentType.JSON);
                createIndexRequest.mapping("_doc", mappings, XContentType.JSON);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }
    }
}
