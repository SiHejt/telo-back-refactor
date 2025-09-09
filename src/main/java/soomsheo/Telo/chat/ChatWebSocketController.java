package soomsheo.Telo.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import soomsheo.Telo.chat.domain.ChatMessage;
import soomsheo.Telo.chat.domain.PhotoMessage;
import soomsheo.Telo.chat.domain.TextMessage;
import soomsheo.Telo.notification.FcmService;

import java.io.IOException;

@Controller
public class ChatWebSocketController {
    private final ChatService chatService;
    private final FcmService fcmService;

    public ChatWebSocketController(ChatService chatService, FcmService fcmService) {
        this.chatService = chatService;
        this.fcmService = fcmService;
    }

    @MessageMapping("/chat/{roomID}/text")
    @SendTo("/queue/{roomID}")
    public ChatMessage handleTextMessage(@DestinationVariable String roomID, @Payload TextMessage message) throws IOException {
        ChatMessage chatMessage = chatService.saveTextMessage(roomID, message.getSenderID(), message.getMessage());
        fcmService.sendPushNotification(roomID, message);
        return chatMessage;
    }

    @MessageMapping("/chat/{roomID}/photo")
    @SendTo("/queue/{roomID}")
    public ChatMessage handlePhotoMessage(@DestinationVariable String roomID, @Payload PhotoMessage message) throws IOException {
        ChatMessage chatMessage = chatService.savePhotoMessage(roomID, message.getSenderID(), message.getImageURL());
        fcmService.sendPushNotification(roomID, message);
        return chatMessage;
    }
}
