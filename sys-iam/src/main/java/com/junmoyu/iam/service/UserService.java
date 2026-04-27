package com.junmoyu.iam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junmoyu.basic.constant.BasicConst;
import com.junmoyu.basic.exception.BusinessException;
import com.junmoyu.basic.model.PageResult;
import com.junmoyu.basic.model.R;
import com.junmoyu.basic.model.SearchPageQuery;
import com.junmoyu.iam.mapper.UserMapper;
import com.junmoyu.iam.mapper.UserRoleMapper;
import com.junmoyu.iam.model.converter.RoleConverter;
import com.junmoyu.iam.model.converter.UserConverter;
import com.junmoyu.iam.model.entity.RoleEntity;
import com.junmoyu.iam.model.entity.UserEntity;
import com.junmoyu.iam.model.entity.UserRoleEntity;
import com.junmoyu.iam.model.request.UserCreateRequest;
import com.junmoyu.iam.model.request.UserPageQuery;
import com.junmoyu.iam.model.request.UserUpdatePasswordRequest;
import com.junmoyu.iam.model.request.UserUpdateRequest;
import com.junmoyu.iam.model.response.UserDetailResponse;
import com.junmoyu.iam.model.response.UserListResponse;
import com.junmoyu.iam.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * UserService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public Long create(UserCreateRequest request) {
        UserEntity insertUser = new UserEntity();
        checkExist(UserEntity::getUsername, request.getUsername(), null, "用户名已存在");

        if (StringUtils.isNotBlank(request.getMobile())) {
            checkExist(UserEntity::getMobile, request.getMobile(), null, "手机号已注册");
            insertUser.setMobile(insertUser.getMobile());
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            checkExist(UserEntity::getEmail, request.getEmail(), null, "邮箱已注册");
            insertUser.setEmail(insertUser.getEmail());
        }
        insertUser.setUsername(request.getUsername());
        insertUser.setPassword(passwordEncoder.encode(request.getPassword()));
        insertUser.setNickname(request.getNickname());
        insertUser.setGender(request.getGender());
        insertUser.setAvatar(request.getAvatar());
        insertUser.setDisable(request.getDisable());
        userMapper.insert(insertUser);

        // 添加用户-角色数据
        List<UserRoleEntity> userRoles = request.getRoleIds().stream().map(id -> new UserRoleEntity(insertUser.getId(), id)).toList();
        userRoleMapper.insert(userRoles);
        return insertUser.getId();
    }

    public void delete(Long id) {
        userMapper.deleteById(id);
        userRoleMapper.delete(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getUserId, id));
    }

    public void update(Long id, UserUpdateRequest request) {
        UserEntity userEntity = userMapper.selectById(id);
        if (userEntity == null) {
            throw new BusinessException("用户不存在");
        }
        UserEntity updateUser = new UserEntity();
        updateUser.setId(id);
        if (StringUtils.isNotBlank(request.getMobile()) && !request.getMobile().equals(userEntity.getMobile())) {
            checkExist(UserEntity::getMobile, request.getMobile(), id, "手机号已注册");
            updateUser.setMobile(request.getMobile());
        }
        if (StringUtils.isNotBlank(request.getEmail()) && !request.getEmail().equals(userEntity.getEmail())) {
            checkExist(UserEntity::getEmail, request.getEmail(), id, "邮箱已注册");
            updateUser.setEmail(request.getEmail());
        }
        updateUser.setNickname(request.getNickname());
        updateUser.setGender(request.getGender());
        updateUser.setAvatar(request.getAvatar());
        updateUser.setDisable(request.getDisable());
        userMapper.updateById(updateUser);

        userRoleMapper.delete(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getUserId, id));
        // 添加用户-角色数据
        List<UserRoleEntity> userRoles = request.getRoleIds().stream().map(roleId -> new UserRoleEntity(id, roleId)).toList();
        userRoleMapper.insert(userRoles);
    }

    public UserDetailResponse detail(Long id) {
        UserEntity userEntity = userMapper.selectById(id);
        if (userEntity == null) {
            throw new BusinessException("用户不存在");
        }
        List<RoleEntity> roles = userMapper.listRoles(id);

        UserDetailResponse detail = UserConverter.INSTANCE.toDetail(userEntity);
        detail.setRoles(RoleConverter.INSTANCE.toList(roles));
        return detail;
    }

    public PageResult<UserResponse> page(UserPageQuery query) {
        Page<UserEntity> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<UserEntity>()
                .like(StringUtils.isNotBlank(query.getUsername()), UserEntity::getUsername, query.getUsername())
                .like(StringUtils.isNotBlank(query.getMobile()), UserEntity::getMobile, query.getMobile())
                .like(StringUtils.isNotBlank(query.getEmail()), UserEntity::getEmail, query.getEmail())
                .orderByDesc(UserEntity::getId);
        Page<UserEntity> userPage = userMapper.selectPage(page, queryWrapper);

        if (CollectionUtils.isEmpty(userPage.getRecords())) {
            return new PageResult<>(userPage.getTotal());
        }
        List<UserResponse> result = UserConverter.INSTANCE.toResponse(userPage.getRecords());
        return new PageResult<>(userPage.getTotal(), result);
    }

    public void disable(Long id) {
        UserEntity updateUser = new UserEntity();
        updateUser.setId(id);
        updateUser.setDisable(true);
        userMapper.updateById(updateUser);
    }

    public void enable(Long id) {
        UserEntity updateUser = new UserEntity();
        updateUser.setId(id);
        updateUser.setDisable(false);
        userMapper.updateById(updateUser);
    }

    public void updatePassword(Long id, UserUpdatePasswordRequest request) {
        UserEntity userEntity = userMapper.selectById(id);
        if (userEntity == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), userEntity.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        UserEntity updateUser = new UserEntity();
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userMapper.updateById(updateUser);
    }

    /**
     * 检查数据是否已存在
     *
     * @param column    要检查的字段
     * @param value     要检查的值
     * @param excludeId 需要排除的ID
     * @param message   错误提示
     */
    private void checkExist(SFunction<UserEntity, ?> column, Object value, Long excludeId, String message) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserEntity>()
                .eq(column, value)
                .ne(excludeId != null, UserEntity::getId, excludeId)
                .last(BasicConst.SQL_LIMIT_ONE));
        if (count != null && count > 0) {
            throw new BusinessException(message);
        }
    }
}
