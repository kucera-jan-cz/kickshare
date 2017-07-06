package com.github.kickshare.security.permission;

import java.io.Serializable;

import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

/**
 * @author Jan.Kucera
 * @since 30.6.2017
 */
public class GroupPermission implements PermissionEvaluator {
    private final GroupServiceImpl groupService;

    @Autowired
    public GroupPermission(final GroupServiceImpl groupService) {
        this.groupService = groupService;
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Object targetDomainObject, final Object permission) {
        BackerDetails backer = (BackerDetails) authentication.getPrincipal();
        Long groupId = (Long) targetDomainObject;
        switch (String.valueOf(permission)) {
            case PermissionConstants.IS_MEMBER:
                return groupService.isGroupMember(backer.getId(), groupId);
            case PermissionConstants.IS_OWNER:
                return groupService.isGroupOwner(backer.getId(), groupId);
            default:
                return false;
        }
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Serializable targetId, final String targetType, final Object permission) {
        throw new IllegalStateException("Id and Class permissions are not supperted by " + this.getClass().toString());
    }

    public enum AccessRight {
        READ,
        UPDATE
    }

    public void hasPermission(BackerDetails backer, long groupId, AccessRight right) {
        switch (right) {
            case UPDATE:
                if (!groupService.isGroupMember(backer.getId(), groupId)) {
                    throw new RuntimeException("User does not have update right for group: " + groupId);
                }
                return;
            case READ:
                if (!groupService.isGroupMember(backer.getId(), groupId)) {
                    throw new RuntimeException("User does not have read right for group: " + groupId);
                }

        }

    }
}
