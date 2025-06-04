package com.tianyu.property.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianyu.property.core.domain.Project;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目数据访问层
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}