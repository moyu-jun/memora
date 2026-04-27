package com.junmoyu.iam.model.converter;

import com.junmoyu.iam.model.entity.RoleEntity;
import com.junmoyu.iam.model.entity.UserEntity;
import com.junmoyu.iam.model.response.RoleListResponse;
import com.junmoyu.iam.model.response.RoleResponse;
import com.junmoyu.iam.model.response.UserDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 权限资源实体转换器
 */
@Mapper
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    RoleListResponse toList(RoleEntity entity);

    List<RoleListResponse> toList(List<RoleEntity> entities);

    RoleResponse toResponse(RoleEntity entity);

    List<RoleResponse> toResponse(List<RoleEntity> entities);
}
