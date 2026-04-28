package com.junmoyu.iam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junmoyu.basic.constant.BasicConst;
import com.junmoyu.basic.exception.BusinessException;
import com.junmoyu.basic.model.PageResult;
import com.junmoyu.basic.model.SearchPageQuery;
import com.junmoyu.iam.mapper.RoleMapper;
import com.junmoyu.iam.mapper.RolePermissionMapper;
import com.junmoyu.iam.mapper.UserRoleMapper;
import com.junmoyu.iam.model.converter.RoleConverter;
import com.junmoyu.iam.model.entity.RoleEntity;
import com.junmoyu.iam.model.entity.RolePermissionEntity;
import com.junmoyu.iam.model.entity.UserRoleEntity;
import com.junmoyu.iam.model.request.RoleCreateUpdateRequest;
import com.junmoyu.iam.model.request.RoleUpdatePermissionRequest;
import com.junmoyu.iam.model.response.RoleListResponse;
import com.junmoyu.iam.model.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * RoleService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long create(RoleCreateUpdateRequest request) {
        checkCodeExist(request.getCode(), null);

        RoleEntity insertRole = new RoleEntity();
        insertRole.setName(request.getName());
        insertRole.setCode(request.getCode());
        insertRole.setSort(request.getSort());
        insertRole.setDisable(request.getDisable());
        insertRole.setRemark(request.getRemark());
        roleMapper.insert(insertRole);
        return insertRole.getId();
    }

    public void delete(Long id) {
        List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getRoleId, id));
        if (CollectionUtils.isNotEmpty(userRoleEntities)) {
            throw new BusinessException("该角色下存在用户，无法删除");
        }
        roleMapper.deleteById(id);
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermissionEntity>()
                .eq(RolePermissionEntity::getRoleId, id));
    }

    public void update(Long id, RoleCreateUpdateRequest request) {
        RoleEntity role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        RoleEntity updateRole = new RoleEntity();
        updateRole.setId(id);
        if (!request.getCode().equals(role.getCode())) {
            checkCodeExist(request.getCode(), id);
            updateRole.setCode(request.getCode());
        }
        updateRole.setName(request.getName());
        updateRole.setCode(request.getCode());
        updateRole.setSort(request.getSort());
        updateRole.setDisable(request.getDisable());
        updateRole.setRemark(request.getRemark());

        roleMapper.updateById(updateRole);
    }

    public RoleResponse detail(Long id) {
        RoleEntity role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return RoleConverter.INSTANCE.toResponse(role);
    }

    public PageResult<RoleResponse> page(SearchPageQuery query) {
        Page<RoleEntity> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<RoleEntity>()
                .like(StringUtils.isNotBlank(query.getKeywords()), RoleEntity::getName, query.getKeywords())
                .orderByAsc(RoleEntity::getSort).orderByDesc(RoleEntity::getId);
        Page<RoleEntity> rolePage = roleMapper.selectPage(page, queryWrapper);

        if (CollectionUtils.isEmpty(rolePage.getRecords())) {
            return new PageResult<>(rolePage.getTotal());
        }
        List<RoleResponse> result = RoleConverter.INSTANCE.toResponse(rolePage.getRecords());
        return new PageResult<>(rolePage.getTotal(), result);
    }

    public List<RoleListResponse> list(SearchPageQuery query) {
        Page<RoleEntity> page = new Page<>(1, query.getSize() == null ? 20 : query.getSize());
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<RoleEntity>()
                .eq(RoleEntity::getDisable, false)
                .like(StringUtils.isNotBlank(query.getKeywords()), RoleEntity::getName, query.getKeywords())
                .orderByAsc(RoleEntity::getSort).orderByDesc(RoleEntity::getId);
        Page<RoleEntity> rolePage = roleMapper.selectPage(page, queryWrapper);

        if (CollectionUtils.isEmpty(rolePage.getRecords())) {
            return Collections.emptyList();
        }
        return RoleConverter.INSTANCE.toList(rolePage.getRecords());
    }

    public void disable(Long id) {
        RoleEntity updateRole = new RoleEntity();
        updateRole.setId(id);
        updateRole.setDisable(true);
        roleMapper.updateById(updateRole);
    }

    public void enable(Long id) {
        RoleEntity updateRole = new RoleEntity();
        updateRole.setId(id);
        updateRole.setDisable(false);
        roleMapper.updateById(updateRole);
    }

    public List<Long> listPermissions(Long id) {
        List<RolePermissionEntity> rolePermissions = rolePermissionMapper
                .selectList(new LambdaQueryWrapper<RolePermissionEntity>().eq(RolePermissionEntity::getRoleId, id));
        return rolePermissions.stream().map(RolePermissionEntity::getPermissionId).toList();
    }

    public void updatePermissions(Long id, RoleUpdatePermissionRequest request) {
        RoleEntity role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermissionEntity>().eq(RolePermissionEntity::getRoleId, id));
        // 添加角色 - 权限数据
        List<RolePermissionEntity> rolePermissions = request.getPermissionIds().stream().map(permissionId -> new RolePermissionEntity(id, permissionId)).toList();
        rolePermissionMapper.insert(rolePermissions);
    }


    /**
     * 检查数据是否已存在
     *
     * @param value     要检查的值
     * @param excludeId 需要排除的ID
     */
    private void checkCodeExist(Object value, Long excludeId) {
        Long count = roleMapper.selectCount(new LambdaQueryWrapper<RoleEntity>()
                .eq(RoleEntity::getCode, value)
                .ne(excludeId != null, RoleEntity::getId, excludeId)
                .last(BasicConst.SQL_LIMIT_ONE));
        if (count != null && count > 0) {
            throw new BusinessException("角色编码已存在");
        }
    }
}
