package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.Nuxeo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NuxeoRepository extends ElasticsearchRepository<Nuxeo, String> {
}
