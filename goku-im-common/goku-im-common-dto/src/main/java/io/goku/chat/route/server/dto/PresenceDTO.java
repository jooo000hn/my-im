package io.goku.chat.route.server.dto;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.route.server.bean.Presence;

/**
 * 
 * 描述
 * 
 *    对象：存在
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
public class PresenceDTO implements IMSerializer {
	
    private Presence presence;

    public PresenceDTO() {
    }

    public PresenceDTO(Presence presence) {
        this.presence = presence;
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    @Override
    public DataBuffer encode() {
        DataBuffer buffer = new DataBuffer();
        buffer.writeLong(presence.getUin());
        buffer.writeByte(presence.getMode());
        buffer.writeString(presence.getStatus());
        return buffer;
    }

    @Override
    public void decode(DataBuffer buffer  ) {
        presence = new Presence();
        presence.setUin(buffer.readLong());
        presence.setMode(buffer.readByte());
        presence.setStatus(buffer.readString());
    }
}
