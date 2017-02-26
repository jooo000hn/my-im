package io.goku.chat.route.server.dto;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.route.server.entity.User;

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
public class UserDTO implements IMSerializer {
	
    private User user;
    
    public UserDTO() {
    	
    }
    
    public UserDTO(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public DataBuffer encode() {
        DataBuffer data = new DataBuffer();
        data.writeString(user.getId());
        data.writeLong(user.getUin());
        data.writeString(user.getAccount());
        data.writeString(user.getPassword());
        data.writeString(user.getNickName());
        data.writeString(user.getAvatarUrl());
        data.writeByte(user.getGender());
        return data;
    }
    
    @Override
    public void decode(DataBuffer buffer ) {
        if (user == null) {
            user = new User();
        }
        user.setId(buffer.readString());
        user.setUin(buffer.readLong());
        user.setAccount(buffer.readString());
        user.setPassword(buffer.readString());
        user.setNickName(buffer.readString());
        user.setAvatarUrl(buffer.readString());
        user.setGender(buffer.readByte());
    }
}
