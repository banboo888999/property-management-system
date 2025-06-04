package com.tianyu.property.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 收入占比VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeProportionVO {

    /**
     * 收入类型名称
     */
    private String name;

    /**
     * 收入金额
     */
    private BigDecimal value;
}