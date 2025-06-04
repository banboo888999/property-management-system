package com.tianyu.property.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.tianyu.property.common.annotation.Sensitive;
import com.tianyu.property.common.util.EncryptUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 敏感数据序列化器
 */
@Component
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private Sensitive sensitive;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        EncryptUtil encryptUtil = applicationContext.getBean(EncryptUtil.class);
        String maskedValue = encryptUtil.mask(value, sensitive.prefixLength(), sensitive.suffixLength());
        gen.writeString(maskedValue);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        Sensitive sensitive = property.getAnnotation(Sensitive.class);
        if (sensitive != null) {
            SensitiveSerializer serializer = new SensitiveSerializer();
            serializer.sensitive = sensitive;
            return serializer;
        }
        return this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SensitiveSerializer.applicationContext = applicationContext;
    }
}