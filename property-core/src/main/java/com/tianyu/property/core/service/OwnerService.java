package com.tianyu.property.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.core.domain.Owner;

import java.util.List;

/**
 * 业主信息服务接口
 */
public interface OwnerService {

    // ... 其他方法保持不变 ...

    /**
     * 查询业主信息列表
     */
    List<Owner> list(Long projectId, String name, String phone);
}