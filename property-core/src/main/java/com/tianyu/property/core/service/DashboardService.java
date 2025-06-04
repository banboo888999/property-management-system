package com.tianyu.property.core.service;

import com.tianyu.property.core.vo.DashboardDataVO;

/**
 * 仪表盘服务接口
 */
public interface DashboardService {

    /**
     * 获取仪表盘数据
     *
     * @param projectId 项目ID
     * @param timeRange 时间范围（week/month/year）
     * @return 仪表盘数据
     */
    DashboardDataVO getDashboardData(Long projectId, String timeRange);
}