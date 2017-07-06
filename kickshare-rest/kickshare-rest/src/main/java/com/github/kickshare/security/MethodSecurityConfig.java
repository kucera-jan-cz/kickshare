package com.github.kickshare.security;

import com.github.kickshare.db.JooqConfiguration;
import com.github.kickshare.security.permission.GroupPermission;
import com.github.kickshare.service.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author Jan.Kucera
 * @since 3.7.2017
 */
@Configuration
@Import({ JooqConfiguration.class })
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final GroupServiceImpl groupService;

    @Autowired
    public MethodSecurityConfig(final GroupServiceImpl groupService) {
        this.groupService = groupService;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new GroupPermission(groupService));
        return expressionHandler;
    }
}