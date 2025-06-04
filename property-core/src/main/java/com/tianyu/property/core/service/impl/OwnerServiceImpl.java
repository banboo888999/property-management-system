package com.tianyu.property.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.common.exception.BusinessException;
import com.tianyu.property.core.domain.Owner;
import com.tianyu.property.core.mapper.OwnerMapper;
import com.tianyu.property.core.service.OwnerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 业主信息服务实现
 */
@Service
public class OwnerServiceImpl implements OwnerService {

    @Resource
    private OwnerMapper ownerMapper;

    @Override
    public void create(Owner owner) {
        // 检查身份证号是否重复
        if (isDuplicateIdCard(owner.getIdCard(), null)) {
            throw new BusinessException("身份证号已存在");
        }
        ownerMapper.insert(owner);
    }

    @Override
    public void update(Owner owner) {
        // 检查身份证号是否重复
        if (isDuplicateIdCard(owner.getIdCard(), owner.getId())) {
            throw new BusinessException("身份证号已存在");
        }
        ownerMapper.updateById(owner);
    }

    @Override
    public void delete(Long id) {
        ownerMapper.deleteById(id);
    }

    @Override
    public Owner get(Long id) {
        return ownerMapper.selectById(id);
    }

    @Override
    public Page<Owner> page(Long projectId, String name, String phone, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<Owner>()
                .eq(Owner::getProjectId, projectId)
                .like(name != null, Owner::getName, name)
                .like(phone != null, Owner::getPhone, phone);
        return ownerMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 检查身份证号是否重复
     *
     * @param idCard 身份证号
     * @param excludeId 排除的ID（更新时使用）
     * @return 是否重复
     */
    private boolean isDuplicateIdCard(String idCard, Long excludeId) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<Owner>()
                .eq(Owner::getIdCard, idCard)
                .ne(excludeId != null, Owner::getId, excludeId);
        return ownerMapper.selectCount(wrapper) > 0;
    }
}