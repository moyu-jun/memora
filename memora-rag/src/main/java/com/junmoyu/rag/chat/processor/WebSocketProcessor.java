package com.junmoyu.rag.chat.processor;

import com.junmoyu.rag.chat.model.WebSocketCommand;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketProcessor
 *
 * @author moyu
 */
public interface WebSocketProcessor<T extends WebSocketCommand> {

    /**
     * WebSocket 消息处理
     *
     * @param session WebSocket Session
     * @param command 具体消息类型
     */
    void process(WebSocketSession session, T command);

    /**
     * 获取被支持的消息类型
     *
     * @return 消息类型
     */
    Class<T> getSupportedType();
}
