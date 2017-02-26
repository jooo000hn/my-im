package io.goku.chat.route.server.dto;


import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;

/**
 * 
 * 描述
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午4:06:46<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class AckDTO implements IMSerializer {
		
    private long to;
    private String ackId;
    
    public AckDTO() {
    }
    
    public AckDTO(long to, String ackId) {
        this.to = to;
        this.ackId = ackId;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public String getAckId() {
        return ackId;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }
    
    @Override
    public DataBuffer encode() {
        DataBuffer buffer = new DataBuffer();
        buffer.writeLong(to);
        buffer.writeString(ackId);
        return buffer;
    }
    	
    @Override
    public void decode(DataBuffer buffer ) {
        to = buffer.readLong();
        ackId = buffer.readString();
    }
    
    @Override
    public String toString() {
        return "AckDTO{" +
                "to=" + to +
                ", ackId='" + ackId + '\'' +
                '}';
    }
    
}
