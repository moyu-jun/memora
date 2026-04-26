package com.junmoyu.iam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junmoyu.iam.model.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper
 */
public interface UserMapper extends BaseMapper<UserEntity> {


    /**
     * 根据用户ID获取用户全部角色标识
     *
     * @param userId 用户ID
     * @return 全部角色标识
     */
    List<String> listRoles(@Param("userId") Long userId);

    /**
     * 根据用户ID获取用户全部权限标识
     *
     * @param userId 用户ID
     * @return 全部权限标识
     */
    List<String> listPermissions(@Param("userId") Long userId);

}