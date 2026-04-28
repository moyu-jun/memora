package com.junmoyu.rag.chat;

import com.junmoyu.basic.util.JsonUtils;
import com.junmoyu.rag.chat.model.WebSocketCommand;
import com.junmoyu.rag.chat.processor.WebSocketProcessor;
import com.junmoyu.rag.model.constant.WebSocketConst;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 核心 WebSocket 处理器
 */
@Slf4j
@Component
public class CoreWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final Map<Class<? extends WebSocketCommand>, WebSocketProcessor<?>> processorMap;

    private final Executor completableFutureExecutor;

    public CoreWebSocketHandler(WebSocketSessionManager sessionManager,
                                List<WebSocketProcessor<?>> processors,
                                Executor completableFutureExecutor) {
        this.sessionManager = sessionManager;
        this.completableFutureExecutor = completableFutureExecutor;
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(WebSocketProcessor::getSupportedType, Function.identity()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get(WebSocketConst.USER_ID_KEY);
        sessionManager.addSession(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @Nonnull CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get(WebSocketConst.USER_ID_KEY);
        sessionManager.removeSession(userId, session);
    }

    @Override
    protected void handleTextMessage(@Nonnull WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // 简单处理心跳机制 (Java 21 switch 模式匹配思路)
        if ("ping".equalsIgnoreCase(payload)) {
            session.sendMessage(new TextMessage("pong"));
            // 可以在 session attributes 里更新最后活跃时间
            return;
        }

        // 实际业务逻辑处理
        // 使用 CompletableFuture.runAsync 配合 Spring 开启的虚拟线程
        // 确保 handleTextMessage 立即返回，不阻塞后续消息接收
        CompletableFuture.runAsync(() -> {
            try {
                // 1. 统一解析消息
                WebSocketCommand command = JsonUtils.toObject(message.getPayload(), WebSocketCommand.class);

                // 2. 使用 Java 21 模式匹配 (Pattern Matching for switch) 处理分发
                // 虽然我们用了 Map，但在复杂多变的逻辑中，模式匹配能提供更强的类型检查
                if (command != null) {
                    dispatch(session, command);
                } else {
                    log.error("WebSocket command is null.");
                }
            } catch (Exception e) {
                handleGlobalError(session, e);
            }
        }, completableFutureExecutor);
    }

    @SuppressWarnings("unchecked")
    private void dispatch(WebSocketSession session, WebSocketCommand command) {
        // 查找对应的策略处理器
        var processor = (WebSocketProcessor<WebSocketCommand>) processorMap.get(command.getClass());

        if (processor != null) {
            processor.process(session, command);
        } else {
            throw new IllegalArgumentException("Unknown command type: " + command.getClass());
        }
    }

    private void handleGlobalError(WebSocketSession session, Exception e) {
        // 生产级：统一异常处理，发送标准格式给前端
        try {
            String errorMsg = "{\"error\": \"" + e.getMessage() + "\"}";
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(errorMsg));
            }
        } catch (Exception ignore) {
        }
    }
}
