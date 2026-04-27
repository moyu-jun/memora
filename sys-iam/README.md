# IAM - 统一用户管理与权限控制系统

## 1. 用户管理 (User)

| 方法     | 路径                       | 描述        |
|--------|--------------------------|-----------|
| POST   | /api/users               | 新增用户      |
| DELETE | /api/users/{id}          | 删除用户      |
| PUT    | /api/users/{id}          | 更新用户信息    |
| GET    | /api/users/{id}          | 查询用户详情    |
| GET    | /api/users               | 分页查询用户列表  |
| PUT    | /api/users/{id}/disable  | 用户禁用      |
| PUT    | /api/users/{id}/enable   | 用户启用      |
| PUT    | /api/users/{id}/password | 重置密码      |

---

## 2. 角色管理 (Role)

| 方法     | 路径                          | 描述           |
|--------|-----------------------------|--------------|
| POST   | /api/roles                  | 新增角色         |
| DELETE | /api/roles/{id}             | 删除角色         |
| PUT    | /api/roles/{id}             | 更新角色         |
| GET    | /api/roles/{id}             | 查询角色详情       |
| GET    | /api/roles/page             | 分页查询角色列表     |
| GET    | /api/roles/list             | 查询角色列表（不分页）  |
| PUT    | /api/roles/{id}/disable     | 禁用角色         |
| PUT    | /api/roles/{id}/enable      | 启用角色         |
| GET    | /api/roles/{id}/permissions | 查询角色拥有的权限    |
| PUT    | /api/roles/{id}/permissions | 设置角色权限（全量替换） |

---

## 3. 权限管理 (Permission)

| 方法     | 路径                            | 描述             |
|--------|-------------------------------|----------------|
| POST   | /api/permissions              | 新增权限           |
| DELETE | /api/permissions/{id}         | 删除权限（级联删除子权限）  |
| PUT    | /api/permissions/{id}         | 更新权限           |
| GET    | /api/permissions/{id}         | 查询权限详情         |
| GET    | /api/permissions              | 查询权限列表（可按父级过滤） |
| GET    | /api/permissions/tree         | 获取权限树结构        |
| PUT    | /api/permissions/{id}/disable | 禁用权限           |
| PUT    | /api/permissions/{id}/enable  | 启用权限           |

---

## 4. 认证接口

| 方法   | 路径                 | 描述                |
|------|--------------------|-------------------|
| POST | /api/auth/register | 用户注册              |
| POST | /api/auth/login    | 用户登录（返回访问令牌与刷新令牌） |
| POST | /api/auth/logout   | 用户登出（废弃当前令牌）      |
| POST | /api/auth/refresh  | 刷新访问令牌            |
| GET  | /api/auth/me       | 获取当前登录用户信息        |
| GET  | /api/auth/menus    | 获取当前用户可访问的菜单树     |
