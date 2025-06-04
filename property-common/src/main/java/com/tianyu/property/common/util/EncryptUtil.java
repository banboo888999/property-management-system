package com.tianyu.property.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * 加密工具类
 */
@Component
public class EncryptUtil {

    @Value("${encrypt.key:tianyu-property-default-key}")
    private String encryptKey;

    private AES aes;

    @PostConstruct
    public void init() {
        // 使用32位密钥初始化AES
        byte[] key = SecureUtil.sha256(encryptKey).substring(0, 32).getBytes(StandardCharsets.UTF_8);
        aes = SecureUtil.aes(key);
    }

    /**
     * 加密字符串
     *
     * @param content 待加密内容
     * @return 加密后的字符串
     */
    public String encrypt(String content) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        return aes.encryptHex(content);
    }

    /**
     * 解密字符串
     *
     * @param encrypted 已加密的内容
     * @return 解密后的字符串
     */
    public String decrypt(String encrypted) {
        if (StrUtil.isBlank(encrypted)) {
            return encrypted;
        }
        return aes.decryptStr(encrypted);
    }

    /**
     * 加密身份证号
     * 保留前6位和后4位，中间部分加密
     */
    public String encryptIdCard(String idCard) {
        if (StrUtil.isBlank(idCard)) {
            return idCard;
        }
        String prefix = idCard.substring(0, 6);
        String suffix = idCard.substring(idCard.length() - 4);
        String middle = idCard.substring(6, idCard.length() - 4);
        return prefix + encrypt(middle) + suffix;
    }

    /**
     * 解密身份证号
     */
    public String decryptIdCard(String encryptedIdCard) {
        if (StrUtil.isBlank(encryptedIdCard)) {
            return encryptedIdCard;
        }
        String prefix = encryptedIdCard.substring(0, 6);
        String suffix = encryptedIdCard.substring(encryptedIdCard.length() - 4);
        String encryptedMiddle = encryptedIdCard.substring(6, encryptedIdCard.length() - 4);
        return prefix + decrypt(encryptedMiddle) + suffix;
    }

    /**
     * 掩码处理（保留前n位和后m位，中间用*代替）
     */
    public String mask(String content, int prefixLength, int suffixLength) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        if (content.length() <= prefixLength + suffixLength) {
            return content;
        }
        String prefix = content.substring(0, prefixLength);
        String suffix = content.substring(content.length() - suffixLength);
        int maskLength = content.length() - prefixLength - suffixLength;
        return prefix + StrUtil.repeat('*', maskLength) + suffix;
    }
}