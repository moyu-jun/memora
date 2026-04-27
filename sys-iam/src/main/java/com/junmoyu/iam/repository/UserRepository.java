package com.junmoyu.iam.repository;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.junmoyu.basic.constant.BasicConst;
import com.junmoyu.iam.mapper.UserMapper;
import com.junmoyu.iam.model.entity.PermissionEntity;
import com.junmoyu.iam.model.entity.RoleEntity;
import com.junmoyu.iam.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * UserRepository
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public UserEntity getUserByAccount(String account) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getDisable, Boolean.FALSE);
        if (Validator.isMobile(account)) {
            queryWrapper.eq(UserEntity::getMobile, account);
        } else if (Validator.isEmail(account)) {
            queryWrapper.eq(UserEntity::getEmail, account);
        } else {
            queryWrapper.eq(UserEntity::getUsername, account);
        }
        queryWrapper.last(BasicConst.SQL_LIMIT_ONE);
        return userMapper.selectOne(queryWrapper);
    }

    public List<String> listRoleCodes(Long userId) {
        List<RoleEntity> roles = userMapper.listRoles(userId);
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return roles.stream().map(RoleEntity::getCode).toList();
    }

    public List<String> listPermissionCodes(Long userId) {
        List<PermissionEntity> permissions = userMapper.listPermissions(userId);
        if (CollectionUtils.isEmpty(permissions)) {
            return Collections.emptyList();
        }
        return permissions.stream().map(PermissionEntity::getCode).filter(StringUtils::isNotBlank).toList();
    }

    public UserMapper mapper() {
        return userMapper;
    }
}
