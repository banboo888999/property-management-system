package com.tianyu.property.auth.aspect;

import com.tianyu.property.auth.annotation.RequirePermission;
import com.tianyu.property.common.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 权限校验切面
 */
@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(com.tianyu.property.auth.annotation.RequirePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequirePermission requirePermission = signature.getMethod().getAnnotation(RequirePermission.class);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(403, "未登录或登录已过期");
        }

        @SuppressWarnings("unchecked")
        Set<String> authorities = (Set<String>) authentication.getAuthorities();
        List<String> requiredPermissions = Arrays.asList(requirePermission.value());

        boolean hasPermission = requirePermission.requireAll()
            ? authorities.containsAll(requiredPermissions)
            : requiredPermissions.stream().anyMatch(authorities::contains);

        if (!hasPermission) {
            throw new BusinessException(403, "无访问权限");
        }
    }
}