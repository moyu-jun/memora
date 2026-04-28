package com.junmoyu.rag.chat;

import com.junmoyu.rag.model.constant.WebSocketConst;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriTemplate;

import java.util.Map;

@Slf4j
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final UriTemplate pathTemplate = new UriTemplate(WebSocketConst.CHAT_PATH);

    @Override
    public boolean beforeHandshake(@Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response,
                                   @Nonnull WebSocketHandler wsHandler, @Nonnull Map<String, Object> attributes) throws Exception {
        // 1. 匹配并提取路径变量
        String path = request.getURI().getPath();
        Map<String, String> match = pathTemplate.match(path);
        String userIdStr = match.get("userId");

        if (StringUtils.isBlank(userIdStr)) {
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        attributes.put(WebSocketConst.USER_ID_KEY, Long.parseLong(userIdStr));
        return true;
    }

    @Override
    public void afterHandshake(@Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response,
                               @Nonnull WebSocketHandler wsHandler, Exception exception) {

    }
}
