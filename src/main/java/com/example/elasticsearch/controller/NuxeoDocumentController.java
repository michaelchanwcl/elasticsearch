package com.example.elasticsearch.controller;

import com.example.elasticsearch.document.NuxeoDocument;
import com.example.elasticsearch.service.NuxeoDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nuxeo")
public class NuxeoDocumentController {
    private final NuxeoDocumentService nuxeoDocumentService;

    @Autowired
    public NuxeoDocumentController(NuxeoDocumentService nuxeoDocumentService) {
        this.nuxeoDocumentService = nuxeoDocumentService;
    }

    @GetMapping("/{id}")
    public NuxeoDocument getById(@PathVariable final String id) {
        return nuxeoDocumentService.getById(id);
    }

    @GetMapping("/getAll")
    public List<NuxeoDocument> getAll() { return nuxeoDocumentService.getAll();}
}
