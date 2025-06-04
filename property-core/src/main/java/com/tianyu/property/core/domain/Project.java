package com.tianyu.property.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目实体
 */
@Data
@TableName("t_project")
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 项目名称
     */
    private String name;
    
    /**
     * 项目编码
     */
    private String code;
    
    /**
     * 项目地址
     */
    private String address;
    
    /**
     * 项目描述
     */
    private String description;
    
    /**
     * 项目状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}