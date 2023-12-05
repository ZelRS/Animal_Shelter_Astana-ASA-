package pro.sky.telegramBot.handler.specificHandlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.sender.MessageSender;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class BlockedUserHandlerImpl implements BlockedUserHandler {
    private final MessageSender messageSender;
    @Override
    public void sendBlockedWelcomePhotoMessage(Long chatId) {
        messageSender.sendBlockedWelcomePhotoMessage(chatId);

    }
}
