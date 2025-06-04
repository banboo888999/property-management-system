package com.tianyu.property.core.service.impl;

import com.tianyu.property.core.domain.FeeRule;
import com.tianyu.property.core.domain.FeeRuleConfig;
import com.tianyu.property.core.domain.TierRule;
import org.springframework.stereotype.Component;

/**
 * 费用计算器
 */
@Component
public class FeeCalculator {

    /**
     * 计算费用
     *
     * @param rule 费用规则
     * @param value 计费数值（面积或用量）
     * @return 费用金额
     */
    public double calculate(FeeRule rule, double value) {
        FeeRuleConfig config = rule.getConfig();
        
        switch (rule.getType()) {
            case 1: // 固定金额
                return config.getBaseAmount();
                
            case 2: // 面积计费
                if (config.getTiers() != null && !config.getTiers().isEmpty()) {
                    return calculateTieredFee(config, value);
                }
                return value * config.getUnitPrice();
                
            case 3: // 用量计费
                if (config.getTiers() != null && !config.getTiers().isEmpty()) {
                    return calculateTieredFee(config, value);
                }
                return value * config.getUnitPrice();
                
            default:
                throw new IllegalArgumentException("不支持的计费类型");
        }
    }
    
    /**
     * 计算阶梯计费费用
     */
    private double calculateTieredFee(FeeRuleConfig config, double value) {
        double totalFee = 0;
        double remainingValue = value;
        
        for (TierRule tier : config.getTiers()) {
            if (remainingValue <= 0) {
                break;
            }
            
            double tierStart = tier.getStart();
            double tierEnd = tier.getEnd();
            double tierPrice = tier.getPrice();
            
            // 计算当前阶梯的用量
            double tierValue;
            if (tierEnd > 0) {
                tierValue = Math.min(remainingValue, tierEnd - tierStart);
            } else {
                tierValue = remainingValue;
            }
            
            // 累加费用
            totalFee += tierValue * tierPrice;
            remainingValue -= tierValue;
        }
        
        return totalFee;
    }
}