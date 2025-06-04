package com.tianyu.property.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 费用规则实体
 */
@Data
@TableName(value = "t_fee_rule", autoResultMap = true)
public class FeeRule {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 规则名称
     */
    private String name;
    
    /**
     * 规则编码
     */
    private String code;
    
    /**
     * 计费类型：1-固定金额，2-面积计费，3-用量计费
     */
    private Integer type;
    
    /**
     * 计费周期：1-一次性，2-月付，3-季付，4-年付
     */
    private Integer period;
    
    /**
     * 计费规则配置（JSON格式）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private FeeRuleConfig config;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 项目ID
     */
    private Long projectId;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

/**
 * 费用规则配置
 */
@Data
public class FeeRuleConfig {
    /**
     * 基础费用（固定金额计费时使用）
     */
    private Double baseAmount;
    
    /**
     * 单价（面积计费时使用）
     */
    private Double unitPrice;
    
    /**
     * 阶梯计价规则
     */
    private List<TierRule> tiers;
}

/**
 * 阶梯计价规则
 */
@Data
public class TierRule {
    /**
     * 起始值（包含）
     */
    private Double start;
    
    /**
     * 结束值（不包含）
     */
    private Double end;
    
    /**
     * 单价
     */
    private Double price;
}