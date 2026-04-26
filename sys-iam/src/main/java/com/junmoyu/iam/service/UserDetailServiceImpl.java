package com.junmoyu.iam.service;

import com.junmoyu.basic.util.RedisUtils;
import com.junmoyu.iam.model.constant.AuthConst;
import com.junmoyu.iam.model.dto.AccessTokenCache;
import com.junmoyu.security.core.Authentication;
import com.junmoyu.security.core.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * UserDetailServiceImpl
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetail {

    private final RedisUtils redisUtils;

    @Override
    public Authentication authentication(String token) {

        AccessTokenCache accessTokenCache = redisUtils.get(AuthConst.accessKey(token), AccessTokenCache.class);

        if (accessTokenCache == null) {
            // Token 不存在或已过期
            return null;
        }

        return new Authentication(
                accessTokenCache.userId(),
                accessTokenCache.username(),
                accessTokenCache.roles(),
                accessTokenCache.permissions(),
                Map.of()
        );
    }
}
