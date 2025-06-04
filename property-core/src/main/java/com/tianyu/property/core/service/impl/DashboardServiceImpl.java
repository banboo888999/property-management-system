package com.tianyu.property.core.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianyu.property.core.domain.Owner;
import com.tianyu.property.core.domain.PaymentOrder;
import com.tianyu.property.core.mapper.OwnerMapper;
import com.tianyu.property.core.mapper.PaymentOrderMapper;
import com.tianyu.property.core.service.DashboardService;
import com.tianyu.property.core.vo.DashboardDataVO;
import com.tianyu.property.core.vo.IncomeProportionVO;
import com.tianyu.property.core.vo.StatisticsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 仪表盘服务实现
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private PaymentOrderMapper paymentOrderMapper;

    @Resource
    private OwnerMapper ownerMapper;

    @Override
    public DashboardDataVO getDashboardData(Long projectId, String timeRange) {
        Date startDate = getStartDate(timeRange);
        Date endDate = new Date();

        DashboardDataVO dashboardData = new DashboardDataVO();

        // 统计数据
        StatisticsVO statistics = new StatisticsVO();
        // 本月收入
        BigDecimal monthlyIncome = getMonthlyIncome(projectId);
        statistics.setMonthlyIncome(monthlyIncome);
        // 待缴费用户数
        Integer unpaidUsers = getUnpaidUsers(projectId);
        statistics.setUnpaidUsers(unpaidUsers);
        // 本月新增业主数
        Integer newOwners = getNewOwners(projectId, DateUtil.beginOfMonth(new Date()));
        statistics.setNewOwners(newOwners);
        // 待处理投诉数
        Integer complaints = getUnhandledComplaints(projectId);
        statistics.setComplaints(complaints);
        dashboardData.setStatistics(statistics);

        // 收入趋势数据
        List<String> xAxis = new ArrayList<>();
        List<BigDecimal> propertyFee = new ArrayList<>();
        List<BigDecimal> parkingFee = new ArrayList<>();
        List<BigDecimal> otherIncome = new ArrayList<>();

        // 根据时间范围生成X轴数据和对应的收入数据
        List<Date> dateList = generateDateList(startDate, endDate, timeRange);
        for (Date date : dateList) {
            xAxis.add(formatDate(date, timeRange));
            propertyFee.add(getIncomeByType(projectId, date, "property_fee"));
            parkingFee.add(getIncomeByType(projectId, date, "parking_fee"));
            otherIncome.add(getIncomeByType(projectId, date, "other"));
        }

        dashboardData.setIncomeData(new DashboardDataVO.IncomeData(xAxis, propertyFee, parkingFee, otherIncome));

        // 收入占比数据
        List<IncomeProportionVO> incomeProportion = new ArrayList<>();
        BigDecimal totalPropertyFee = getTotalIncomeByType(projectId, startDate, endDate, "property_fee");
        BigDecimal totalParkingFee = getTotalIncomeByType(projectId, startDate, endDate, "parking_fee");
        BigDecimal totalOtherIncome = getTotalIncomeByType(projectId, startDate, endDate, "other");

        incomeProportion.add(new IncomeProportionVO("物业费", totalPropertyFee));
        incomeProportion.add(new IncomeProportionVO("车位费", totalParkingFee));
        incomeProportion.add(new IncomeProportionVO("其他收入", totalOtherIncome));
        dashboardData.setIncomeProportion(incomeProportion);

        return dashboardData;
    }

    /**
     * 获取开始日期
     */
    private Date getStartDate(String timeRange) {
        Date now = new Date();
        switch (timeRange) {
            case "week":
                return DateUtil.beginOfWeek(now);
            case "month":
                return DateUtil.beginOfMonth(now);
            case "year":
                return DateUtil.beginOfYear(now);
            default:
                return DateUtil.beginOfMonth(now);
        }
    }

    /**
     * 获取本月收入
     */
    private BigDecimal getMonthlyIncome(Long projectId) {
        Date startDate = DateUtil.beginOfMonth(new Date());
        Date endDate = new Date();

        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getProjectId, projectId)
                .eq(PaymentOrder::getStatus, "paid")
                .between(PaymentOrder::getPayTime, startDate, endDate);

        return paymentOrderMapper.selectTotalAmount(wrapper);
    }

    /**
     * 获取待缴费用户数
     */
    private Integer getUnpaidUsers(Long projectId) {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getProjectId, projectId)
                .eq(PaymentOrder::getStatus, "unpaid");

        return paymentOrderMapper.selectCount(wrapper).intValue();
    }

    /**
     * 获取新增业主数
     */
    private Integer getNewOwners(Long projectId, Date startDate) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<Owner>()
                .eq(Owner::getProjectId, projectId)
                .ge(Owner::getCreateTime, startDate);

        return ownerMapper.selectCount(wrapper).intValue();
    }

    /**
     * 获取待处理投诉数
     */
    private Integer getUnhandledComplaints(Long projectId) {
        // TODO: 实现投诉工单统计
        return 0;
    }

    /**
     * 根据收费类型获取收入
     */
    private BigDecimal getIncomeByType(Long projectId, Date date, String type) {
        Date startDate = DateUtil.beginOfDay(date);
        Date endDate = DateUtil.endOfDay(date);

        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getProjectId, projectId)
                .eq(PaymentOrder::getStatus, "paid")
                .eq(PaymentOrder::getFeeType, type)
                .between(PaymentOrder::getPayTime, startDate, endDate);

        return paymentOrderMapper.selectTotalAmount(wrapper);
    }

    /**
     * 获取时间范围内某类型的总收入
     */
    private BigDecimal getTotalIncomeByType(Long projectId, Date startDate, Date endDate, String type) {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getProjectId, projectId)
                .eq(PaymentOrder::getStatus, "paid")
                .eq(PaymentOrder::getFeeType, type)
                .between(PaymentOrder::getPayTime, startDate, endDate);

        return paymentOrderMapper.selectTotalAmount(wrapper);
    }

    /**
     * 生成日期列表
     */
    private List<Date> generateDateList(Date startDate, Date endDate, String timeRange) {
        List<Date> dateList = new ArrayList<>();
        Date current = startDate;

        while (!current.after(endDate)) {
            dateList.add(current);
            switch (timeRange) {
                case "week":
                    current = DateUtil.offsetDay(current, 1);
                    break;
                case "month":
                    current = DateUtil.offsetDay(current, 1);
                    break;
                case "year":
                    current = DateUtil.offsetMonth(current, 1);
                    break;
            }
        }

        return dateList;
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date, String timeRange) {
        switch (timeRange) {
            case "week":
            case "month":
                return DateUtil.format(date, "MM-dd");
            case "year":
                return DateUtil.format(date, "yyyy-MM");
            default:
                return DateUtil.format(date, "yyyy-MM-dd");
        }
    }
}