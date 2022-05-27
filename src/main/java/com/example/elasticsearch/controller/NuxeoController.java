package com.example.elasticsearch.controller;

import com.example.elasticsearch.document.Nuxeo;
import com.example.elasticsearch.document.NuxeoDocument;
import com.example.elasticsearch.service.NuxeoDocumentService;
import com.example.elasticsearch.service.NuxeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nuxeo")
public class NuxeoController {
    private final NuxeoService nuxeoService;

    @Autowired
    public NuxeoController(NuxeoService nuxeoService) {
        this.nuxeoService = nuxeoService;
    }

//    @GetMapping("/{id}")
//    public Nuxeo getById(@PathVariable final String id) {
//        return nuxeoService.getById(id);
//    }

    @GetMapping("/getAll")
    public List<Nuxeo> getAll() { return nuxeoService.getAll();}

    @PostMapping("/getAll")
    public List<Nuxeo> getAllByPage(@RequestBody NuxeoRequestDTO nuxeoRequestDTO) {
        return nuxeoService.getAllByPage(nuxeoRequestDTO.getPage(), nuxeoRequestDTO.getSize());
    }

    @PostMapping("/search")
    public List<Nuxeo> search(@RequestBody NuxeoRequestDTO nuxeoRequestDTO) {
        return nuxeoService.searchPageWithHighlight(nuxeoRequestDTO.getSearch());
    }

    @PostMapping("/aggregation")
    public void aggregation(@RequestBody NuxeoRequestDTO nuxeoRequestDTO) {
        nuxeoService.searchAggregations();
    }
}
