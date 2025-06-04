package com.tianyu.property.common.context;

/**
 * 项目上下文
 */
public class ProjectContext {
    private static final ThreadLocal<Long> CURRENT_PROJECT_ID = new ThreadLocal<>();

    public static void setCurrentProjectId(Long projectId) {
        CURRENT_PROJECT_ID.set(projectId);
    }

    public static Long getCurrentProjectId() {
        return CURRENT_PROJECT_ID.get();
    }

    public static void clear() {
        CURRENT_PROJECT_ID.remove();
    }
}