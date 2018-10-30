package com.bfd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:自定义用户
 */
public class User implements UserDetails {

    private Long id;

    private String account;

    @JsonIgnore
    private String password;

    private String name;

    private boolean enabled;
    /**
     * 用户有权访问的所有url
     */
    @JsonIgnore
    private List<Resource> authenticateList = new ArrayList<>();

    private List<Resource> menuList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Resource> getAuthenticateList() {
        return authenticateList;
    }

    public void setAuthenticateList(List<Resource> authenticateList) {
        this.authenticateList = authenticateList;
    }

    public List<Resource> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Resource> menuList) {
        this.menuList = menuList;
    }

    @Override
    public String toString() {
        return this.account;
    }

    @Override
    public int hashCode() {
        return account.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

}
