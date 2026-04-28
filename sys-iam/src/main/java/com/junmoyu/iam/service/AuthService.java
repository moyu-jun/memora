package com.junmoyu.iam.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.junmoyu.basic.exception.AuthException;
import com.junmoyu.basic.model.AuthErrorCode;
import com.junmoyu.basic.util.RedisUtils;
import com.junmoyu.iam.model.constant.AuthConst;
import com.junmoyu.iam.model.dto.AccessTokenCache;
import com.junmoyu.iam.model.dto.AuthSessionCache;
import com.junmoyu.iam.model.dto.RefreshTokenCache;
import com.junmoyu.iam.model.entity.PermissionEntity;
import com.junmoyu.iam.model.entity.UserEntity;
import com.junmoyu.iam.model.request.LoginRequest;
import com.junmoyu.iam.model.request.RefreshRequest;
import com.junmoyu.iam.model.response.PermissionTreeNode;
import com.junmoyu.iam.model.response.TokenResponse;
import com.junmoyu.iam.repository.UserRepository;
import com.junmoyu.iam.util.PermissionUtils;
import com.junmoyu.security.SecurityProperties;
import com.junmoyu.security.core.Authentication;
import com.junmoyu.security.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AuthService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RedisUtils redisUtils;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.getUserByAccount(request.getAccount());
        if (user == null) {
            throw new AuthException(AuthErrorCode.AUTH_FAILED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.AUTH_FAILED);
        }

        long accessExpire = securityProperties.getAccessExpire() + RandomUtil.randomInt(100, 200);
        long refreshExpire = securityProperties.getRefreshExpire() + RandomUtil.randomInt(200, 400);
        long now = System.currentTimeMillis();

        Long userId = user.getId();
        String sessionId = IdUtil.simpleUUID();
        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();

        List<String> roleCodes = userRepository.listRoleCodes(userId);
        List<String> permissionCodes = userRepository.listPermissionCodes(userId);

        // 清除该用户所有认证相关缓存
        clearAuthCache(userId);

        // 缓存新数据
        redisUtils.set(AuthConst.userSessionKey(userId), sessionId, refreshExpire);

        AuthSessionCache sessionCache = new AuthSessionCache(sessionId, userId, user.getUsername(), accessToken, refreshToken, roleCodes, permissionCodes, request.getIp(), request.getUserAgent(), now);
        redisUtils.set(AuthConst.sessionKey(sessionId), sessionCache, refreshExpire);

        AccessTokenCache accessTokenCache = new AccessTokenCache(sessionId, userId, user.getUsername(), request.getIp(), request.getUserAgent(), now, accessExpire, roleCodes, permissionCodes);
        redisUtils.set(AuthConst.accessKey(accessToken), accessTokenCache, accessExpire);

        RefreshTokenCache refreshTokenCache = new RefreshTokenCache(sessionId, now, refreshExpire);
        redisUtils.set(AuthConst.refreshKey(refreshToken), refreshTokenCache, refreshExpire);

        return new TokenResponse(accessToken, refreshToken, accessExpire);
    }

    public void logout() {
        Authentication authentication = SecurityContext.getAuthentication();
        clearAuthCache(authentication.userId());
    }

    public TokenResponse refresh(RefreshRequest request) {
        RefreshTokenCache oldRefreshCache = redisUtils.get(AuthConst.refreshKey(request.getRefreshToken()), RefreshTokenCache.class);
        if (oldRefreshCache == null) {
            throw new AuthException(AuthErrorCode.AUTH_FAILED);
        }
        AuthSessionCache oldSessionCache = redisUtils.get(AuthConst.sessionKey(oldRefreshCache.sid()), AuthSessionCache.class);
        if (oldSessionCache == null) {
            throw new AuthException(AuthErrorCode.AUTH_FAILED);
        }

        if (!request.getIp().equalsIgnoreCase(oldSessionCache.clientIp()) || !request.getUserAgent().equalsIgnoreCase(oldSessionCache.userAgent())) {
            log.error("客户端IP / User-Agent 发生变化，IP 变化：{} -> {}，User-Agent 变化：{} -> {}", oldSessionCache.clientIp(), request.getIp(), oldSessionCache.userAgent(), request.getUserAgent());
        }

        long accessExpire = securityProperties.getAccessExpire() + RandomUtil.randomInt(100, 200);
        long refreshExpire = securityProperties.getRefreshExpire() + RandomUtil.randomInt(200, 400);
        long now = System.currentTimeMillis();

        Long userId = oldSessionCache.userId();
        String sid = oldRefreshCache.sid();
        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();


        AuthSessionCache authSessionCache = new AuthSessionCache(sid, userId, oldSessionCache.username(), accessToken, refreshToken, oldSessionCache.roles(), oldSessionCache.permissions(), request.getIp(), request.getUserAgent(), oldSessionCache.loginAt());
        redisUtils.set(AuthConst.sessionKey(sid), authSessionCache, refreshExpire);

        AccessTokenCache accessTokenCache = new AccessTokenCache(sid, userId, authSessionCache.username(), request.getIp(), request.getUserAgent(), now, accessExpire, authSessionCache.roles(), authSessionCache.permissions());
        redisUtils.set(AuthConst.accessKey(accessToken), accessTokenCache, accessExpire);

        RefreshTokenCache refreshTokenCache = new RefreshTokenCache(sid, now, refreshExpire);
        redisUtils.set(AuthConst.refreshKey(refreshToken), refreshTokenCache, refreshExpire);

        // 清理旧数据
        redisUtils.delete(AuthConst.accessKey(oldSessionCache.accessToken()));
        redisUtils.delete(AuthConst.refreshKey(oldSessionCache.refreshToken()));

        return new TokenResponse(accessToken, refreshToken, accessExpire);
    }

    public List<PermissionTreeNode> menus() {
        Authentication authentication = SecurityContext.getAuthentication();
        List<PermissionEntity> permissions = userRepository.mapper().listPermissions(authentication.userId());

        List<PermissionTreeNode> permissionTreeNodes = PermissionUtils.buildTree(permissions);
        PermissionUtils.sortTree(permissionTreeNodes);
        return permissionTreeNodes;
    }

    private void clearAuthCache(Long userId) {
        String sid = redisUtils.get(AuthConst.userSessionKey(userId), String.class);

        if (StringUtils.isNotBlank(sid)) {
            AuthSessionCache sessionCache = redisUtils.get(AuthConst.sessionKey(sid), AuthSessionCache.class);
            if (sessionCache != null) {
                redisUtils.delete(AuthConst.sessionKey(sid));
                redisUtils.delete(AuthConst.accessKey(sessionCache.accessToken()));
                redisUtils.delete(AuthConst.refreshKey(sessionCache.refreshToken()));
                redisUtils.delete(AuthConst.userSessionKey(userId));
            }
        }
    }

}
