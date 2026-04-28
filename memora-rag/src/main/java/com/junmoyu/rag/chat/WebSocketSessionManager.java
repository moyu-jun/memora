package com.junmoyu.rag.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketSessionManager {

    private final ConcurrentHashMap<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    /**
     * 添加或替换用户的会话。如果该用户已存在旧的会话且处于打开状态，会强制关闭旧会话。
     */
    public void addSession(Long userId, WebSocketSession session) {
        // 原子替换并返回旧值
        WebSocketSession oldSession = userSessions.put(userId, session);
        if (oldSession != null && oldSession.isOpen()) {
            try {
                // 关闭旧连接，状态码可自定义，此处使用“被新连接替换”的语义
                oldSession.close(CloseStatus.NORMAL);
            } catch (IOException e) {
                log.error("WebSocket 连接清理失败", e);
            }
        }
    }

    /**
     * 移除指定的会话，只有当该userId当前映射的会话与参数相同时才移除，避免误删新会话。
     */
    public void removeSession(Long userId, WebSocketSession session) {
        userSessions.remove(userId, session);
    }

    /**
     * 给指定用户发送消息（仅限本机连接）。
     */
    public void sendMessage(Long userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("sendMessage error", e);
            }
        }
    }
}
