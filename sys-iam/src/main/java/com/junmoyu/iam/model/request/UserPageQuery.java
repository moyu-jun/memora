package com.junmoyu.iam.model.request;

import com.junmoyu.basic.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserPageQuery
 *
 * @author mjwang17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageQuery extends PageQuery {
    
    @Schema(description = "用户名搜索关键词")
    private String username;

    @Schema(description = "手机号搜索关键词")
    private String mobile;

    @Schema(description = "邮箱搜索关键词")
    private String email;
}
