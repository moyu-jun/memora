package com.junmoyu.iam.model.dto;

import java.util.List;

/**
 * Redis 缓存对象
 * auth:access:{token}
 */
public record AccessTokenCache(
        String sid,
        Long userId,
        String username,
        String clientIp,
        String userAgent,
        Long issuedAt,
        Long expiresIn,
        List<String> roles,
        List<String> permissions) {
}
