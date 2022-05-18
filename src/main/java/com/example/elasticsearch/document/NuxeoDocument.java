package com.example.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NuxeoDocument {
    private String _id;
    private String _type;
    private long _version;
    @JsonProperty("ecm:uuid")
    @Field(type = FieldType.Keyword)
    private String uuid;

    @JsonProperty("ecm:repository")
    @Field(type = FieldType.Keyword)
    private String repository;

    @JsonProperty("ecm:name")
    private String name;

    @JsonProperty("ecm:title")
    private String title;

    @JsonProperty("ecm:path")
    private String path;

    @JsonProperty("ecm:path@level1")
    private String path_level1;

    @JsonProperty("ecm:path@level2")
    private String path_level2;

    @JsonProperty("ecm:path@level3")
    private String path_level3;

    @JsonProperty("ecm:path@level4")
    private String path_level4;

    @JsonProperty("ecm:primaryType")
    private String primaryType;

    @JsonProperty("ecm:parentId")
    private String parentId;

    @JsonProperty("ecm:currentLifeCycleState")
    private String currentLifeCycleState;

    @JsonProperty("ecm:isCheckedIn")
    private String isCheckedIn;

    @JsonProperty("ecm:isProxy")
    private String isProxy;

    @JsonProperty("ecm:isTrashed")
    private String isTrashed;

    @JsonProperty("ecm:isVersion")
    private String isVersion;

    @JsonProperty("ecm:mixinType")
    private List<String> mixinType;

    @JsonProperty("ecm:tag")
    private List<String> tag;

    @JsonProperty("ecm:acl")
    private List<String> acl;

    @JsonProperty("ecm:binarytext")
    private String binarytext;
}

//{"ecm:repository":"default","ecm:uuid":"1e0c4fcd-f8ba-49a0-ab2e-2fdc782ab288","ecm:name":"Michael",
//        "ecm:title":"Michael","ecm:path":"/default-domain/workspaces/Michael","ecm:path@level1":"default-domain","ecm:path@level2":"workspaces",
//        "ecm:path@level3":"Michael","ecm:path@depth":4,"ecm:primaryType":"Workspace","ecm:parentId":"3eec7582-9c48-4f73-b07b-55d8b6b9b13b",
//        "ecm:currentLifeCycleState":"project",
//        "ecm:isCheckedIn":false,"ecm:isProxy":false,"ecm:isTrashed":false,"ecm:isVersion":false,"ecm:isLatestVersion":false,
//        "ecm:isLatestMajorVersion":false,"ecm:isRecord":false,
//        "ecm:hasLegalHold":false,"ecm:mixinType":["Folderish","NXTag","SuperSpace"],
//        "ecm:tag":[],"ecm:changeToken":"0-0","ecm:acl":["Administrator","members"],
//        "webc:themePage":"workspace","webc:theme":"sites","webc:moderationType":"aposteriori",
//        "common:icon":"/icons/workspace.gif","dc:creator":"Administrator","dc:modified":"2022-04-19T09:45:27.120Z",
//        "dc:lastContributor":"Administrator","dc:created":"2022-04-19T09:45:27.120Z",
//        "dc:title":"Michael","dc:contributors":["Administrator"]}}