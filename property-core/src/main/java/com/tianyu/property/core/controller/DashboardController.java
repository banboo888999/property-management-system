package com.tianyu.property.core.controller;

import com.tianyu.property.common.api.CommonResult;
import com.tianyu.property.core.service.DashboardService;
import com.tianyu.property.core.vo.DashboardDataVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/data")
    public CommonResult<DashboardDataVO> getDashboardData(
            @RequestParam Long projectId,
            @RequestParam String timeRange) {
        return CommonResult.success(dashboardService.getDashboardData(projectId, timeRange));
    }
}