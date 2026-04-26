package com.junmoyu.iam.model.dto;

/**
 * Redis 缓存对象
 * auth:refresh:{token}
 */
public record RefreshTokenCache(
        String sid,
        Long issuedAt,
        Long expiresIn) {
}
