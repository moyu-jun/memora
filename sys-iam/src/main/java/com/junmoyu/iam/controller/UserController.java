package com.junmoyu.iam.controller;

import com.junmoyu.basic.model.PageResult;
import com.junmoyu.basic.model.R;
import com.junmoyu.basic.model.SearchPageQuery;
import com.junmoyu.iam.model.request.UserCreateRequest;
import com.junmoyu.iam.model.request.UserPageQuery;
import com.junmoyu.iam.model.request.UserUpdatePasswordRequest;
import com.junmoyu.iam.model.request.UserUpdateRequest;
import com.junmoyu.iam.model.response.UserDetailResponse;
import com.junmoyu.iam.model.response.UserListResponse;
import com.junmoyu.iam.model.response.UserResponse;
import com.junmoyu.iam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理接口
 */
@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    @Operation(summary = "新增用户")
    public R<Long> create(@Valid @RequestBody UserCreateRequest request) {
        return R.success();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除用户")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.success();
    }

    @PutMapping("{id}")
    @Operation(summary = "更新基础用户信息")
    public R<Boolean> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return R.success();
    }

    @GetMapping("{id}")
    @Operation(summary = "获取用户详情")
    public R<UserDetailResponse> detail(@PathVariable Long id) {
        return R.success();
    }

    @GetMapping("page")
    @Operation(summary = "分页查询用户列表")
    public R<PageResult<UserResponse>> page(UserPageQuery query) {
        return R.success();
    }

    @PutMapping("{id}/disable")
    @Operation(summary = "用户禁用")
    public R<Boolean> disable(@PathVariable Long id) {
        return R.success();
    }

    @PutMapping("{id}/enable")
    @Operation(summary = "用户启用")
    public R<Boolean> enable(@PathVariable Long id) {
        return R.success();
    }

    @PutMapping("{id}/password")
    @Operation(summary = "重置密码")
    public R<Boolean> updatePassword(@PathVariable Long id, @Valid @RequestBody UserUpdatePasswordRequest request) {
        return R.success();
    }
}
