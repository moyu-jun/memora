package com.junmoyu.rag.chat.model;

/**
 * DataCommand
 *
 * @author moyu
 */
public record DataCommand(String dataId, String dataType, String value) implements WebSocketCommand {
}
