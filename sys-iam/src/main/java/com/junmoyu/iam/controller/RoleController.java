package com.junmoyu.iam.controller;

import com.junmoyu.basic.model.PageResult;
import com.junmoyu.basic.model.R;
import com.junmoyu.basic.model.SearchPageQuery;
import com.junmoyu.iam.model.request.*;
import com.junmoyu.iam.model.response.*;
import com.junmoyu.iam.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理接口
 */
@Tag(name = "角色管理接口")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping()
    @Operation(summary = "新增角色")
    public R<Long> create(@Valid @RequestBody RoleCreateUpdateRequest request) {
        return R.success(roleService.create(request));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除角色")
    public R<Boolean> delete(@PathVariable Long id) {
        roleService.delete(id);
        return R.success(true);
    }

    @PutMapping("{id}")
    @Operation(summary = "更新角色信息")
    public R<Boolean> update(@PathVariable Long id, @Valid @RequestBody RoleCreateUpdateRequest request) {
        roleService.update(id, request);
        return R.success(true);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取角色详情")
    public R<RoleResponse> detail(@PathVariable Long id) {
        return R.success(roleService.detail(id));
    }

    @GetMapping("page")
    @Operation(summary = "分页查询角色列表")
    public R<PageResult<RoleResponse>> page(SearchPageQuery query) {
        return R.success(roleService.page(query));
    }

    @GetMapping("list")
    @Operation(summary = "查询角色列表 - 默认只有一页")
    public R<List<RoleListResponse>> list(SearchPageQuery query) {
        return R.success(roleService.list(query));
    }

    @PutMapping("{id}/disable")
    @Operation(summary = "角色禁用")
    public R<Boolean> disable(@PathVariable Long id) {
        roleService.disable(id);
        return R.success(true);
    }

    @PutMapping("{id}/enable")
    @Operation(summary = "角色启用")
    public R<Boolean> enable(@PathVariable Long id) {
        roleService.enable(id);
        return R.success(true);
    }

    @GetMapping("{id}/permissions")
    @Operation(summary = "查询角色拥有的权限ID列表")
    public R<List<Long>> listPermissions(@PathVariable Long id) {
        return R.success(roleService.listPermissions(id));
    }

    @PutMapping("{id}/permissions")
    @Operation(summary = "设置角色权限（全量替换）")
    public R<Boolean> updatePermissions(@PathVariable Long id, @RequestBody RoleUpdatePermissionRequest request) {
        roleService.updatePermissions(id, request);
        return R.success(true);
    }
}
