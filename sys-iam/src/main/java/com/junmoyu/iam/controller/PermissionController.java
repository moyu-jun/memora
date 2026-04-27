package com.junmoyu.iam.controller;

import com.junmoyu.basic.model.R;
import com.junmoyu.iam.model.request.PermissionCreateUpdateRequest;
import com.junmoyu.iam.model.response.PermissionResponse;
import com.junmoyu.iam.model.response.PermissionTreeNode;
import com.junmoyu.iam.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限资源管理接口
 */
@Tag(name = "权限资源管理接口")
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping()
    @Operation(summary = "新增权限资源（目录、菜单、按钮/API）")
    public R<Long> create(@Valid @RequestBody PermissionCreateUpdateRequest request) {
        return R.success(permissionService.create(request));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除权限资源")
    public R<Boolean> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return R.success(true);
    }

    @PutMapping("{id}")
    @Operation(summary = "更新权限资源")
    public R<Boolean> update(@PathVariable Long id, @Valid @RequestBody PermissionCreateUpdateRequest request) {
        permissionService.update(id, request);
        return R.success(true);
    }

    @GetMapping("{id}")
    @Operation(summary = "查询权限详情")
    public R<PermissionResponse> detail(@PathVariable Long id) {
        return R.success(permissionService.detail(id));
    }

    @GetMapping("tree")
    @Operation(summary = "获取全量权限/资源树")
    public R<List<PermissionTreeNode>> tree() {
        return R.success(permissionService.tree());
    }

    @PutMapping("{id}/disable")
    @Operation(summary = "权限禁用")
    public R<Boolean> disable(@PathVariable Long id) {
        permissionService.disable(id);
        return R.success(true);
    }

    @PutMapping("{id}/enable")
    @Operation(summary = "权限启用")
    public R<Boolean> enable(@PathVariable Long id) {
        permissionService.enable(id);
        return R.success(true);
    }
}
