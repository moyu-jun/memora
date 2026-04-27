package com.junmoyu.iam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junmoyu.basic.constant.BasicConst;
import com.junmoyu.basic.exception.BusinessException;
import com.junmoyu.basic.model.PageResult;
import com.junmoyu.basic.model.SearchPageQuery;
import com.junmoyu.iam.mapper.PermissionMapper;
import com.junmoyu.iam.mapper.RoleMapper;
import com.junmoyu.iam.mapper.RolePermissionMapper;
import com.junmoyu.iam.mapper.UserRoleMapper;
import com.junmoyu.iam.model.converter.PermissionConverter;
import com.junmoyu.iam.model.converter.RoleConverter;
import com.junmoyu.iam.model.entity.PermissionEntity;
import com.junmoyu.iam.model.entity.RoleEntity;
import com.junmoyu.iam.model.entity.RolePermissionEntity;
import com.junmoyu.iam.model.entity.UserRoleEntity;
import com.junmoyu.iam.model.request.PermissionCreateUpdateRequest;
import com.junmoyu.iam.model.request.RoleCreateUpdateRequest;
import com.junmoyu.iam.model.request.RoleUpdatePermissionRequest;
import com.junmoyu.iam.model.response.PermissionResponse;
import com.junmoyu.iam.model.response.PermissionTreeNode;
import com.junmoyu.iam.model.response.RoleResponse;
import com.junmoyu.iam.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * PermissionService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    private final PermissionRepository permissionRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long create(PermissionCreateUpdateRequest request) {
        checkCodeExist(request.getCode(), null);

        PermissionEntity insertPermission = new PermissionEntity();
        insertPermission.setParentId(request.getParentId());
        insertPermission.setName(request.getName());
        insertPermission.setCode(request.getCode());
        insertPermission.setType(request.getType());
        insertPermission.setPath(request.getPath());
        insertPermission.setIcon(request.getIcon());
        insertPermission.setSort(request.getSort());
        insertPermission.setDisable(request.getDisable());
        insertPermission.setVisible(request.getVisible());
        permissionMapper.insert(insertPermission);
        return insertPermission.getId();
    }

    public void delete(Long id) {
        List<RolePermissionEntity> rolePermissions = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermissionEntity>()
                .eq(RolePermissionEntity::getPermissionId, id)
                .last(BasicConst.SQL_LIMIT_ONE));
        if (CollectionUtils.isNotEmpty(rolePermissions)) {
            throw new BusinessException("有角色已关联该权限，无法删除");
        }
        permissionMapper.deleteById(id);
    }

    public void update(Long id, PermissionCreateUpdateRequest request) {
        PermissionEntity permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限数据不存在");
        }
        PermissionEntity updatePermission = new PermissionEntity();
        updatePermission.setId(id);

        if (!request.getCode().equals(permission.getCode())) {
            checkCodeExist(request.getCode(), id);
            updatePermission.setCode(request.getCode());
        }

        updatePermission.setParentId(request.getParentId());
        updatePermission.setName(request.getName());
        updatePermission.setType(request.getType());
        updatePermission.setPath(request.getPath());
        updatePermission.setIcon(request.getIcon());
        updatePermission.setSort(request.getSort());
        updatePermission.setDisable(request.getDisable());
        updatePermission.setVisible(request.getVisible());
        permissionMapper.updateById(updatePermission);
    }

    public PermissionResponse detail(Long id) {
        PermissionEntity permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限数据不存在");
        }
        String parentName = "根目录";
        if (permission.getParentId() != 0) {
            PermissionEntity parentPermission = permissionMapper.selectById(permission.getParentId());
            parentName = parentPermission.getName();
        }

        PermissionResponse response = PermissionConverter.INSTANCE.toResponse(permission);
        response.setParentName(parentName);
        return response;
    }

    public List<PermissionTreeNode> tree() {
        return permissionRepository.tree(3);
    }

    public void disable(Long id) {
        PermissionEntity updatePermission = new PermissionEntity();
        updatePermission.setId(id);
        updatePermission.setDisable(true);
        permissionMapper.updateById(updatePermission);
    }

    public void enable(Long id) {
        PermissionEntity updatePermission = new PermissionEntity();
        updatePermission.setId(id);
        updatePermission.setDisable(false);
        permissionMapper.updateById(updatePermission);
    }

    /**
     * 检查数据是否已存在
     *
     * @param value     要检查的值
     * @param excludeId 需要排除的ID
     */
    private void checkCodeExist(Object value, Long excludeId) {
        Long count = permissionMapper.selectCount(new LambdaQueryWrapper<PermissionEntity>()
                .eq(PermissionEntity::getCode, value)
                .ne(excludeId != null, PermissionEntity::getId, excludeId)
                .last(BasicConst.SQL_LIMIT_ONE));
        if (count != null && count > 0) {
            throw new BusinessException("权限编码已存在");
        }
    }
}
