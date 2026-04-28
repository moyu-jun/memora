package com.junmoyu.rag.chat.model;

/**
 * ChatCommand
 */
public record ChatCommand(String toUserId, String content) implements WebSocketCommand {
}
