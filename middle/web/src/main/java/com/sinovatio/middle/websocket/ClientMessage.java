package com.sinovatio.middle.websocket;


import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class ClientMessage {

    private String name;

    public ClientMessage() {
    }

}
