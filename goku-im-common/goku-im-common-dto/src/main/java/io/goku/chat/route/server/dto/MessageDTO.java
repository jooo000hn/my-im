package io.goku.chat.route.server.dto;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.route.server.entity.Message;

/**
 * Created by Tony on 2/20/15.
 */
public class MessageDTO implements IMSerializer {

    private Message message;
    
    public MessageDTO() {
    	
    }

    public MessageDTO(Message message) {
        this.message = message;
    }

    public Long getTo() {
        return message.getTo();
    }

    public Long getFrom() {
        return message.getFrom();
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public DataBuffer encode() {
        DataBuffer buffer = new DataBuffer();
        buffer.writeString(message.getId());
        buffer.writeLong(message.getTo());
        buffer.writeLong(message.getFrom());
        buffer.writeByte(message.getType());
        buffer.writeString(message.getMessage());
        buffer.writeLong(message.getCreateAt());
        return buffer;
    }
    
    @Override
    public void decode(DataBuffer buffer ) {
        if (message == null) {
            message = new Message();
        }
        message.setId(buffer.readString());
        message.setTo(buffer.readLong());
        message.setFrom(buffer.readLong());
        message.setType(buffer.readByte());
        message.setMessage(buffer.readString());
        message.setCreateAt(buffer.readLong());
    }
}
