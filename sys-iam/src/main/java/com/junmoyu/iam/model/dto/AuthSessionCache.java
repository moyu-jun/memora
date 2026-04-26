package com.junmoyu.iam.model.dto;

import java.util.List;

/**
 * Redis 缓存对象
 * auth:session:{sid}
 */
public record AuthSessionCache(
        String sid,
        Long userId,
        String username,
        String accessToken,
        String refreshToken,
        List<String> roles,
        List<String> permissions,
        String clientIp,
        String userAgent,
        Long loginAt) {
}
