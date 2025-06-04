package com.tianyu.property.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.core.domain.Project;

/**
 * 项目服务接口
 */
public interface ProjectService {
    /**
     * 创建项目
     */
    Project createProject(Project project);

    /**
     * 更新项目
     */
    boolean updateProject(Project project);

    /**
     * 删除项目
     */
    boolean deleteProject(Long id);

    /**
     * 获取项目详情
     */
    Project getProject(Long id);

    /**
     * 分页查询项目
     */
    Page<Project> listProjects(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 切换项目状态
     */
    boolean toggleProjectStatus(Long id, Integer status);
}