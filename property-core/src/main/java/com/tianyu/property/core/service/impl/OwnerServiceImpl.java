package com.tianyu.property.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.common.exception.BusinessException;
import com.tianyu.property.core.domain.Owner;
import com.tianyu.property.core.mapper.OwnerMapper;
import com.tianyu.property.core.service.OwnerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业主信息服务实现
 */
@Service
public class OwnerServiceImpl implements OwnerService {

    // ... 其他方法保持不变 ...

    @Override
    public List<Owner> list(Long projectId, String name, String phone) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<Owner>()
                .eq(Owner::getProjectId, projectId)
                .like(name != null, Owner::getName, name)
                .like(phone != null, Owner::getPhone, phone);
        return ownerMapper.selectList(wrapper);
    }
}