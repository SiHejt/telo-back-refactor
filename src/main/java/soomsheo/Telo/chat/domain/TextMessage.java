package soomsheo.Telo.chat.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("TEXT")
public class TextMessage extends ChatMessage {
    private String message;
    public TextMessage(String roomID, String senderID, String message, LocalDateTime sendDate) {
        super(roomID, senderID, sendDate, MessageType.TEXT);
        this.message = message;
    }

}
