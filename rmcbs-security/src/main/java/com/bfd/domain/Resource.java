package com.bfd.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/7/31 14:37
 * @Description:
 */
public class Resource implements Serializable {

    private static final long serialVersionUID = 3854609232831660303L;

    private Long id;

    private String url;

    private String name;

    private Long parentId;

    private List<Resource> children;

    private String path;

    private String icon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Resource> getChildren() {
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
