package com.zum.escape.api.util;

import com.zum.escape.api.users.dto.Message;
import com.zum.escape.api.users.dto.UserDto;
import sun.net.www.content.audio.x_aiff;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMaker {
    public static String dtoToMessage(List<? extends Message> dto, String defaultMessage) {
        if(dto == null || dto.isEmpty())
            return defaultMessage;

        return dto.stream()
                .map(Message::toMessage)
                .collect(Collectors.joining("\n"));
    }
}
