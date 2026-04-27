package com.junmoyu.iam.model.converter;

import com.junmoyu.iam.model.entity.PermissionEntity;
import com.junmoyu.iam.model.entity.UserEntity;
import com.junmoyu.iam.model.response.PermissionTreeNode;
import com.junmoyu.iam.model.response.UserDetailResponse;
import com.junmoyu.iam.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 权限资源实体转换器
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mapping(target = "roles", ignore = true)
    UserDetailResponse toDetail(UserEntity entity);

    UserResponse toResponse(UserEntity entity);

    List<UserResponse> toResponse(List<UserEntity> entities);
}
