package com.tianyu.property.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘数据VO
 */
@Data
public class DashboardDataVO {

    /**
     * 统计数据
     */
    private StatisticsVO statistics;

    /**
     * 收入趋势数据
     */
    private IncomeData incomeData;

    /**
     * 收入占比数据
     */
    private List<IncomeProportionVO> incomeProportion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncomeData {
        private List<String> xAxis;
        private List<BigDecimal> propertyFee;
        private List<BigDecimal> parkingFee;
        private List<BigDecimal> otherIncome;
    }
}