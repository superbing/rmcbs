package com.bfd.controller;

import com.bfd.bean.RoleBean;
import com.bfd.common.vo.Result;
import com.bfd.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
@RestController
@RequestMapping(RoleController.REQ_URL)
@Api(value = RoleController.REQ_URL, description = "角色管理")
public class RoleController {
    
    public static final String REQ_URL = "/role";
    
    @Autowired
    private SystemService systemService;
    
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加角色")
    public Result<Object> add(@RequestBody RoleBean roleBean) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.addRole(roleBean));
    }
    
    @GetMapping(value = "/get")
    @ApiOperation(value = "获取角色")
    public Result<Object> get(@RequestParam Long id) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.getRole(id));
    }
    
    @PutMapping(value = "/update")
    @ApiOperation(value = "更新角色")
    public Result<Object> update(@RequestBody RoleBean roleBean) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.updateRole(roleBean));
    }
    
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除角色")
    public Result<Object> delete(@RequestParam Long id) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.deleteRole(id));
    }
    
    @ApiOperation(value = "获取某角色下的资源列表")
    @GetMapping(value = "/resourceListByRoleId")
    public Result<Object> resourceListByRoleId(@RequestParam Long id) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.queryResourceByRoleId(id));
    }
    
    @PostMapping(value = "/authorize")
    @ApiOperation(value = "角色授权")
    public Result<Object> authorize(@RequestParam Long id, Long[] resourceIds) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.authorize(id, resourceIds));
    }
    
    @ApiOperation(value = "分页获取角色列表")
    @GetMapping(value = "/page")
    public Result<Object> page(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String name) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.rolePage(pageNum, pageSize, name));
    }
    
    @ApiOperation(value = "获取所有角色列表")
    @GetMapping(value = "/list")
    public Result<Object> list() {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), systemService.queryAllRole());
    }
}
