package com.bfd.domain;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: bing.shen
 * @date: 2018/7/31 15:24
 * @Description:
 */
public class ResourceRowMapper implements RowMapper<Resource> {


    @Override
    public Resource mapRow(ResultSet resultSet, int i) throws SQLException {
        Resource resource = new Resource();
        resource.setId(resultSet.getLong("id"));
        resource.setUrl(resultSet.getString("url"));
        resource.setName(resultSet.getString("name"));
        resource.setParentId(resultSet.getLong("parent_id"));
        resource.setPath(resultSet.getString("path"));
        resource.setIcon(resultSet.getString("icon"));
        return resource;
    }
}
