package com.junmoyu.rag.chat.config;

import com.junmoyu.rag.chat.AuthHandshakeInterceptor;
import com.junmoyu.rag.chat.CoreWebSocketHandler;
import com.junmoyu.rag.model.constant.WebSocketConst;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final CoreWebSocketHandler webSocketHandler;
    private final AuthHandshakeInterceptor authInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, WebSocketConst.CHAT_PATH)
                .addInterceptors(authInterceptor)
                // 生产环境请配置具体域名
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 8KB
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        // 空闲 5 分钟断开
        container.setMaxSessionIdleTimeout(300000L);
        return container;
    }
}
