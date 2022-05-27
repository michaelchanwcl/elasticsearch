package com.example.elasticsearch.controller;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data public class NuxeoRequestDTO {
    private int page;
    private int size;
    private Sort.Direction sort;
    private String search;
}
