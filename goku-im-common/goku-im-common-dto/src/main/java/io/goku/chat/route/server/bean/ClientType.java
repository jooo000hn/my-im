package io.goku.chat.route.server.bean;

/**
 * 客户端类型
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午4:16:56<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public enum ClientType {
	
    WINDOWS(0), MAC(1), LINUX(2), IOS(3), ANDROID(4), WINPHONE(5), UNKNOUWN(-1);
	
    private byte mValue = 0;

    public byte value() {
        return mValue;
    }

    ClientType(int value) {
        mValue = (byte) value;
    }

    public static ClientType valueOfRaw(byte value) {
        for (ClientType clientType : ClientType.values()) {
            if (clientType.value() == value) {
                return clientType;
            }
        }
        return UNKNOUWN;
    }
}
