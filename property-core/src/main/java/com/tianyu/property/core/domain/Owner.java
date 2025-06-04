package com.tianyu.property.core.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tianyu.property.common.annotation.Sensitive;
import com.tianyu.property.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业主信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_owner")
public class Owner extends BaseEntity {

    /**
     * 业主姓名
     */
    private String name;

    /**
     * 身份证号（加密存储）
     */
    @Sensitive(prefixLength = 6, suffixLength = 4)
    private String idCard;

    /**
     * 手机号（脱敏显示）
     */
    @Sensitive(prefixLength = 3, suffixLength = 4)
    private String phone;

    /**
     * 邮箱（脱敏显示）
     */
    @Sensitive(prefixLength = 2, suffixLength = 8)
    private String email;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 状态（0-禁用 1-启用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}