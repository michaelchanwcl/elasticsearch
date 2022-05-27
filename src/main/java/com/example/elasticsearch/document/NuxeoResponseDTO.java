package com.example.elasticsearch.document;

import lombok.Data;

@Data
public class NuxeoResponseDTO {
    private String id;
    private String title;
    private String highlightedMessage;
}
