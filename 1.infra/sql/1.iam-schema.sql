-- ----------------------------
-- 用户基础信息表 (User)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '系统用户名/登录名（要求唯一）',
  `password` varchar(255) NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) NULL DEFAULT NULL COMMENT '真实姓名/昵称',
  `gender` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别：0-保密，1-男，2-女',
  `avatar` varchar(255) NULL DEFAULT NULL COMMENT '用户头像URL',
  `mobile` varchar(20) NULL DEFAULT NULL COMMENT '手机号码（要求唯一）',
  `email` varchar(100) NULL DEFAULT NULL COMMENT '邮箱地址（要求唯一）',
  `disable` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用状态：0-未禁用，1-已禁用',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_mobile`(`mobile` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 角色表 (Role)
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '角色名称（如：系统管理员）',
  `code` varchar(50) NOT NULL COMMENT '角色编码（如：ADMIN）',
  `sort` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序编号',
  `disable` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用状态：0-未禁用，1-已禁用',
  `remark` varchar(255) NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 权限/资源表 (Permission)
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键ID',
  `parent_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级权限ID，顶级为0',
  `name` varchar(50) NOT NULL COMMENT '权限/菜单名称',
  `code` varchar(100) NULL DEFAULT NULL COMMENT '权限标识（如：user:add, menu:sys）',
  `type` tinyint UNSIGNED NOT NULL COMMENT '权限类型：1-目录，2-菜单，3-按钮',
  `path` varchar(255) NULL DEFAULT NULL COMMENT '路由地址或API路径',
  `icon` varchar(100) NULL DEFAULT NULL COMMENT '前端图标',
  `sort` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序编号',
  `disable` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用状态：0-未禁用，1-已禁用',
  `visible` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '可见状态：0-不可见，1-可见',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 用户-角色 关联表 (User-Role)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` bigint UNSIGNED NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 角色-权限 关联表 (Role-Permission)
-- ---------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键ID',
  `role_id` bigint UNSIGNED NOT NULL COMMENT '角色ID',
  `permission_id` bigint UNSIGNED NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_permission`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色-权限关联表' ROW_FORMAT = Dynamic;
