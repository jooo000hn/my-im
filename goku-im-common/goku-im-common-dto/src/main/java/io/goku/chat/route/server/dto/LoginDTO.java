package io.goku.chat.route.server.dto;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.route.server.entity.Login;

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
public class LoginDTO implements IMSerializer {
	
    private Login login;
    
    public LoginDTO() {
    	
    }

    public LoginDTO(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }
    
    public void setLogin(Login login) {
        this.login = login;
    }
    
    @Override
    public DataBuffer encode() {
        DataBuffer buffer = new DataBuffer();
        buffer.writeLong(login.getUin());
        buffer.writeString(login.getAuthToken());
        buffer.writeLong(login.getActiveTime());
        return buffer;
    }

    @Override
    public void decode(DataBuffer buffer) {
        if(login == null) {
            login = new Login();
        }
        login.setUin(buffer.readLong());
        login.setAuthToken(buffer.readString());
        login.setActiveTime(buffer.readLong());
    }

    

}
