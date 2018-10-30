package com.bfd.controller;

import com.bfd.config.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.UserBean;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.ChangePasswordQO;
import com.bfd.service.SystemService;
import com.bfd.utils.ValidatorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
@RestController
@RequestMapping(UserController.REQ_URL)
@Api(value = UserController.REQ_URL, description = "账户管理")
public class UserController {
    
    public static final String REQ_URL = "/user";
    
    @Autowired
    private SystemService systemService;
    
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加用户")
    public Result<Object> add(@RequestBody UserBean userBean) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.addUser(userBean));
    }
    
    @PatchMapping(value = "/resetPassword")
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPassword(@RequestParam Long id) {
        systemService.resetPassword(id);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
    }
    
    @PostMapping(value = "/changePassword")
    @ApiOperation(value = "修改密码")
    public Result<Object> changePassword(@RequestBody ChangePasswordQO changePasswordQO) {
        ValidatorUtils.validateEntity(changePasswordQO);
        systemService.changePassword(changePasswordQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
    }
    
    @GetMapping(value = "/get")
    @ApiOperation(value = "获取用户")
    public Result<Object> get(@RequestParam Long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.getUser(id));
    }
    
    @PutMapping(value = "/update")
    @ApiOperation(value = "更新用户")
    public Result<Object> update(@RequestBody UserBean userBean) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.updateUser(userBean));
    }
    
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除用户")
    public Result<Object> delete(@RequestParam Long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.deleteUser(id));
    }
    
    @ApiOperation(value = "分页获取用户列表")
    @GetMapping(value = "/page")
    public Result<Object> page(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String account) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.userPage(pageNum, pageSize, account));
    }
}
