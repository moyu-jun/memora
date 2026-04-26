package com.junmoyu.iam.controller;

import com.junmoyu.basic.model.R;
import com.junmoyu.basic.util.HttpUtils;
import com.junmoyu.iam.model.request.LoginRequest;
import com.junmoyu.iam.model.request.RefreshRequest;
import com.junmoyu.iam.model.response.PermissionTreeNode;
import com.junmoyu.iam.model.response.TokenResponse;
import com.junmoyu.iam.service.AuthService;
import com.junmoyu.security.annotation.PreAuthorize;
import com.junmoyu.security.core.Authentication;
import com.junmoyu.security.core.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安全认证接口
 */
@Tag(name = "安全认证接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    @Operation(summary = "用户注册")
    public R<Boolean> register(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        return R.success();
    }

    @PostMapping("login")
    @Operation(summary = "密码登录")
    public R<TokenResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        request.setIp(HttpUtils.getClientIp(httpServletRequest));
        request.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        return R.success(authService.login(request));
    }

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "退出登录")
    public R<Boolean> logout() {
        authService.logout();
        return R.success(true);
    }

    @PostMapping("refresh")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "刷新访问令牌")
    public R<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request, HttpServletRequest httpServletRequest) {
        request.setIp(HttpUtils.getClientIp(httpServletRequest));
        request.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        return R.success(authService.refresh(request));
    }

    @GetMapping("me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取当前登录用户信息")
    public R<Authentication> me() {
        return R.success(SecurityContext.getAuthentication());
    }

    @GetMapping("menus")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取当前用户可访问的菜单树")
    public R<List<PermissionTreeNode>> menus() {
        return R.success(authService.menus());
    }
}
