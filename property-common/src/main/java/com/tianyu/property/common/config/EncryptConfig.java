package com.tianyu.property.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tianyu.property.common.util.EncryptUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 加密配置
 */
@Configuration
public class EncryptConfig implements MetaObjectHandler {

    @Resource
    private EncryptUtil encryptUtil;

    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动加密身份证号
        Object idCard = getFieldValByName("idCard", metaObject);
        if (idCard instanceof String) {
            setFieldValByName("idCard", encryptUtil.encryptIdCard((String) idCard), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动加密身份证号
        Object idCard = getFieldValByName("idCard", metaObject);
        if (idCard instanceof String) {
            setFieldValByName("idCard", encryptUtil.encryptIdCard((String) idCard), metaObject);
        }
    }
}