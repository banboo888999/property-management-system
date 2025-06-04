package com.tianyu.property.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianyu.property.core.domain.Owner;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业主信息Mapper
 */
@Mapper
public interface OwnerMapper extends BaseMapper<Owner> {
}