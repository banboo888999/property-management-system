package com.tianyu.property.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.auth.annotation.RequirePermission;
import com.tianyu.property.common.api.CommonResult;
import com.tianyu.property.core.domain.Project;
import com.tianyu.property.core.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 项目管理控制器
 */
@RestController
@RequestMapping("/api/project")
@Api(tags = "项目管理接口")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @PostMapping
    @ApiOperation("创建项目")
    @RequirePermission("project:create")
    public CommonResult<Project> createProject(@RequestBody Project project) {
        return CommonResult.success(projectService.createProject(project));
    }

    @PutMapping
    @ApiOperation("更新项目")
    @RequirePermission("project:update")
    public CommonResult<Boolean> updateProject(@RequestBody Project project) {
        return CommonResult.success(projectService.updateProject(project));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除项目")
    @RequirePermission("project:delete")
    public CommonResult<Boolean> deleteProject(@PathVariable Long id) {
        return CommonResult.success(projectService.deleteProject(id));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取项目详情")
    @RequirePermission("project:read")
    public CommonResult<Project> getProject(@PathVariable Long id) {
        return CommonResult.success(projectService.getProject(id));
    }

    @GetMapping("/list")
    @ApiOperation("分页查询项目")
    @RequirePermission("project:read")
    public CommonResult<Page<Project>> listProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return CommonResult.success(projectService.listProjects(keyword, pageNum, pageSize));
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("切换项目状态")
    @RequirePermission("project:update")
    public CommonResult<Boolean> toggleProjectStatus(
            @PathVariable Long id,
            @PathVariable Integer status) {
        return CommonResult.success(projectService.toggleProjectStatus(id, status));
    }
}