package com.tianyu.property.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.core.domain.Owner;

/**
 * 业主信息服务接口
 */
public interface OwnerService {

    /**
     * 创建业主信息
     */
    void create(Owner owner);

    /**
     * 更新业主信息
     */
    void update(Owner owner);

    /**
     * 删除业主信息
     */
    void delete(Long id);

    /**
     * 获取业主信息
     */
    Owner get(Long id);

    /**
     * 分页查询业主信息
     */
    Page<Owner> page(Long projectId, String name, String phone, Integer pageNum, Integer pageSize);
}