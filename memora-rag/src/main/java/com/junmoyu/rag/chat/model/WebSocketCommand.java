package com.junmoyu.rag.chat.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * WebSocketCommand
 *
 * @author moyu
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatCommand.class, name = "CHAT"),
        @JsonSubTypes.Type(value = DataCommand.class, name = "DATA")
})
public sealed interface WebSocketCommand permits ChatCommand, DataCommand {
}
