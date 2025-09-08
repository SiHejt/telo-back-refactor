package soomsheo.Telo.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.chat.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomID(String roomID);
}
