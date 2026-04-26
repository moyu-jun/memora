package com.junmoyu.iam.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新访问令牌请求参数
 */
@Data
public class RefreshRequest {

    @Schema(description = "刷新令牌 - refreshToken")
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;

    @Schema(description = "客户端IP，对前端隐藏", hidden = true)
    private String ip;

    @Schema(description = "客户端IP，对前端隐藏", hidden = true)
    private String userAgent;
}
