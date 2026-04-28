package com.junmoyu.rag.chat.processor;

import com.junmoyu.rag.chat.model.ChatCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * ChatProcessor
 *
 * @author moyu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatProcessor implements WebSocketProcessor<ChatCommand> {

    @Override
    public void process(WebSocketSession session, ChatCommand command) {
        // 1. 快速校验逻辑
        log.info("Processing chat to: {}", command.toUserId());

        log.info("线程信息 ChatProcessor：{}", Thread.currentThread().getName());
    }

    @Override
    public Class<ChatCommand> getSupportedType() {
        return ChatCommand.class;
    }
}
