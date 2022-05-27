package com.example.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.elasticsearch.document.Nuxeo;
import com.example.elasticsearch.repository.NuxeoRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class NuxeoService {
    @Autowired
    private NuxeoRepository nuxeoRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<Nuxeo> getAll() {
        Iterable<Nuxeo> nuxeoIterable = nuxeoRepository.findAll();
        List<Nuxeo> nuxeoList = new ArrayList<>();
        nuxeoIterable.forEach(p -> {
            nuxeoList.add(p);
        });
        return nuxeoList;
    }

    public List<Nuxeo> getAllByPage(int page, int size) {
        Sort sort = Sort.by("ecm:title");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Iterable<Nuxeo> nuxeoIterable = nuxeoRepository.findAll(pageRequest);
        List<Nuxeo> nuxeoList = new ArrayList<>();
        nuxeoIterable.forEach(p -> {
            nuxeoList.add(p);
        });
        return nuxeoList;
    }

    public void searchAggregations() {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("top_creators").field("dc:creator");
        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withIndices("nuxeo").
                withQuery(QueryBuilders.matchAllQuery()).addAggregation(termsAggregationBuilder).build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {

            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
        StringTerms topCreator = (StringTerms) aggregationMap.get("top_creators");
        List<StringTerms.Bucket> bucketList = topCreator.getBuckets();
        for (StringTerms.Bucket a : bucketList) {
            System.out.println("Key=[" + a.getKeyAsString() + "], Count=[" + a.getDocCount() + "]");
        }
    }

    public List<Nuxeo> searchPageWithHighlight(String searchText) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(searchText))
                .withHighlightFields(
                        new HighlightBuilder.Field("dc:title.fulltext"),
                        new HighlightBuilder.Field("ecm:binarytext"),
                        new HighlightBuilder.Field("ecm:tag"))
                .build();
        Page<Nuxeo> nuxeoAggregatedPage = elasticsearchTemplate.queryForPage(searchQuery, Nuxeo.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<T> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }

                    Map<String, Object> searchHitSourceAsMap = searchHit.getSourceAsMap();
                    Map<String, HighlightField> highlightFieldMap = searchHit.getHighlightFields();
                    for (Map.Entry<String, HighlightField> highlightField : highlightFieldMap.entrySet()) {
                        String key = highlightField.getKey();
                        HighlightField value = highlightField.getValue();
                        StringBuilder sb = new StringBuilder();
                        for (Text text : value.getFragments()) {
                            sb.append(text);
                        }
                        searchHitSourceAsMap.put(key, sb);
                    }

                    T nuxeo = JSON.parseObject(JSONObject.toJSONString(searchHitSourceAsMap), clazz);
                    chunk.add(nuxeo);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk);
                }
                return new AggregatedPageImpl<T>(new ArrayList<T>());
            }
        });
        return nuxeoAggregatedPage.getContent();
    }

}

// https://stackoverflow.com/questions/37049764/how-to-provide-highlighting-with-spring-data-elasticsearch
// https://blog.csdn.net/qq_40885085/article/details/105024625
