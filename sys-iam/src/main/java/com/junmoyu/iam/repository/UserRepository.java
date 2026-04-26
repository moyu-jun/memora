package com.junmoyu.iam.repository;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.junmoyu.basic.constant.BasicConst;
import com.junmoyu.iam.mapper.UserMapper;
import com.junmoyu.iam.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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

    public UserMapper mapper() {
        return userMapper;
    }
}
