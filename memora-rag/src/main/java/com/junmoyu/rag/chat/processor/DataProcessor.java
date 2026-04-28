package com.junmoyu.rag.chat.processor;

import com.junmoyu.rag.chat.model.DataCommand;
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
public class DataProcessor implements WebSocketProcessor<DataCommand> {

    @Override
    public void process(WebSocketSession session, DataCommand command) {
        // 1. 快速校验逻辑
        log.info("Processing data id: {}", command.dataId());

    }

    @Override
    public Class<DataCommand> getSupportedType() {
        return DataCommand.class;
    }
}
