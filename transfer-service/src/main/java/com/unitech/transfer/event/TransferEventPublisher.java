package com.unitech.transfer.event;

import com.unitech.transfer.model.Transfer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishTransferEvent(Transfer transfer){
        rabbitTemplate.convertAndSend("transfer.exchange", "transfer.success", transfer);
    }

}
