package com.example.elasticsearch.document;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(indexName = "nuxeo", type="doc")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Nuxeo implements Serializable {
    @Id
    private String id;
    @JSONField(name = "ecm:uuid")
    private String uuid;

    @JSONField(name = "ecm:repository")
    private String repository;

    @JSONField(name = "ecm:name")
    private String name;

    @JSONField(name = "ecm:title")
    private String title;

    @JSONField(name = "ecm:path")
    private String path;

    @JSONField(name = "ecm:parentId")
    private String parentId;

    @JSONField(name = "ecm:isTrashed")
    private String isTrashed;

    @JSONField(name = "ecm:isVersion")
    private String isVersion;

    @JsonProperty("ecm:mixinType")
    @JSONField(name = "ecm:mixinType")
    private List<String> mixinType;

    @JSONField(name = "ecm:tag")
    private List<String> tag;

    @JsonProperty("ecm:acl")
    @JSONField(name = "ecm:acl")
    private List<String> acl;

    @JSONField(name = "ecm:binarytext")
    private String binarytext;

    @JSONField(name = "dc:title")
    private String dcTitle;

    @JSONField(name = "dc:title.fulltext")
    private String dcTitleFullText;

    @JSONField(name = "dc:created")
    private Date dcCreated;

    @JSONField(name = "ecm:primaryType")
    private String type;
}