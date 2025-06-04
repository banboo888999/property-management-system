package com.tianyu.property.core.interceptor;

import com.tianyu.property.common.context.ProjectContext;
import com.tianyu.property.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 项目拦截器
 */
@Component
public class ProjectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String projectId = request.getHeader("X-Project-Id");
        if (projectId == null) {
            throw new BusinessException(400, "请选择项目");
        }
        
        try {
            ProjectContext.setCurrentProjectId(Long.parseLong(projectId));
            return true;
        } catch (NumberFormatException e) {
            throw new BusinessException(400, "项目ID格式错误");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ProjectContext.clear();
    }
}