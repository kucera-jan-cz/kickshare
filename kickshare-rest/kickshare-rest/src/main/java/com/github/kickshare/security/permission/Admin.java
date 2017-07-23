package com.github.kickshare.security.permission;

import static com.github.kickshare.security.permission.PermissionConstants.ADMIN_AUTHORITY;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Jan.Kucera
 * @since 6.7.2017
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + ADMIN_AUTHORITY + "')")
public @interface Admin {
}
