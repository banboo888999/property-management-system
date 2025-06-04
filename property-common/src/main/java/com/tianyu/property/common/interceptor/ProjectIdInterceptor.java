package com.tianyu.property.common.interceptor;

import com.tianyu.property.common.context.ProjectContext;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * 项目ID数据隔离拦截器
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class ProjectIdInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        
        // 获取SQL
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        
        // 获取当前项目ID
        Long projectId = ProjectContext.getCurrentProjectId();
        if (projectId == null) {
            return invocation.proceed();
        }
        
        // 注入项目ID条件
        if (sql.toLowerCase().contains("where")) {
            sql = sql.replaceFirst("(?i)where", "WHERE project_id = " + projectId + " AND");
        } else {
            sql = sql + " WHERE project_id = " + projectId;
        }
        
        // 重置SQL
        metaObject.setValue("delegate.boundSql.sql", sql);
        
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}