package soomsheo.Telo.chat.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soomsheo.Telo.repair.domain.RepairRequest;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("REPAIR_REQUEST")
public class RequestMessage extends ChatMessage {
    @ManyToOne
    @JoinColumn(name = "repair_request_id")
    private RepairRequest repairRequest;

    public RequestMessage (String roomID, String senderID, RepairRequest repairRequest, LocalDateTime sendDate){
        super(roomID, senderID, sendDate, MessageType.REPAIR_REQUEST);
        this.repairRequest = repairRequest;
    }

}
