package com.tianyu.property.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.common.exception.BusinessException;
import com.tianyu.property.core.domain.Project;
import com.tianyu.property.core.mapper.ProjectMapper;
import com.tianyu.property.core.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 项目服务实现类
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public Project createProject(Project project) {
        // 检查项目编码是否重复
        if (isProjectCodeExists(project.getCode())) {
            throw new BusinessException("项目编码已存在");
        }
        
        project.setStatus(1);
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        
        projectMapper.insert(project);
        return project;
    }

    @Override
    public boolean updateProject(Project project) {
        Project existingProject = projectMapper.selectById(project.getId());
        if (existingProject == null) {
            throw new BusinessException("项目不存在");
        }
        
        // 检查项目编码是否重复（排除自身）
        if (!existingProject.getCode().equals(project.getCode()) 
            && isProjectCodeExists(project.getCode())) {
            throw new BusinessException("项目编码已存在");
        }
        
        project.setUpdateTime(LocalDateTime.now());
        return projectMapper.updateById(project) > 0;
    }

    @Override
    public boolean deleteProject(Long id) {
        return projectMapper.deleteById(id) > 0;
    }

    @Override
    public Project getProject(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    public Page<Project> listProjects(String keyword, Integer pageNum, Integer pageSize) {
        Page<Project> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(Project::getName, keyword)
                   .or()
                   .like(Project::getCode, keyword);
        }
        
        wrapper.orderByDesc(Project::getCreateTime);
        return projectMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean toggleProjectStatus(Long id, Integer status) {
        Project project = new Project();
        project.setId(id);
        project.setStatus(status);
        project.setUpdateTime(LocalDateTime.now());
        return projectMapper.updateById(project) > 0;
    }

    private boolean isProjectCodeExists(String code) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getCode, code);
        return projectMapper.selectCount(wrapper) > 0;
    }
}