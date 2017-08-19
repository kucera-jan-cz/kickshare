package com.github.kickshare.security.permission;

/**
 * @author Jan.Kucera
 * @since 6.7.2017
 */
public final class PermissionConstants {
    public static final String IS_MEMBER = "isMember";
    public static final String IS_OWNER = "isOwner";

    public static final String ADMIN_AUTHORITY = "ADMIN";
    //@TODO - consider rename to loader
    public static final String LEADER_AUTHORITY = "CREATE_GROUP";

    private PermissionConstants() {}
}
