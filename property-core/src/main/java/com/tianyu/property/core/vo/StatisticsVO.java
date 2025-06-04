package com.tianyu.property.core.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计数据VO
 */
@Data
public class StatisticsVO {

    /**
     * 本月收入
     */
    private BigDecimal monthlyIncome;

    /**
     * 待缴费用户数
     */
    private Integer unpaidUsers;

    /**
     * 本月新增业主数
     */
    private Integer newOwners;

    /**
     * 待处理投诉数
     */
    private Integer complaints;
}