package com.junmoyu.iam.model.constant;

/**
 * 认证授权相关常量
 */
public class AuthConst {

    /**
     * auth:session:{sid} -> 本次登录信息
     */
    public static final String SESSION_PREFIX = "auth:session:";

    /**
     * auth:access:{token} -> 用户信息/权限
     */
    public static final String ACCESS_TOKEN_PREFIX = "auth:access:";

    /**
     * auth:refresh:{token} -> token 信息
     */
    public static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    /**
     * auth:user:{uid}:session -> sid
     */
    public static final String USER_SESSION_KEY = "auth:user:{uid}:session";


    public static String accessKey(String accessToken) {
        return ACCESS_TOKEN_PREFIX + accessToken;
    }

    public static String refreshKey(String refreshToken) {
        return REFRESH_TOKEN_PREFIX + refreshToken;
    }

    public static String sessionKey(String sessionId) {
        return SESSION_PREFIX + sessionId;
    }

    public static String userSessionKey(Long userId) {
        return USER_SESSION_KEY.replace("{uid}", String.valueOf(userId));
    }
}
