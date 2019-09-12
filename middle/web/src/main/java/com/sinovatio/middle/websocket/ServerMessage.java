package com.sinovatio.middle.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ServerMessage {

    private String content;

    public ServerMessage() {
    }

    @Override
    public String toString() {
        return content;
    }

}
