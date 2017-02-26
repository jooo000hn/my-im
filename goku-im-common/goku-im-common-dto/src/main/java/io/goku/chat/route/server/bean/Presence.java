package io.goku.chat.route.server.bean;

/**
 * 
 * 描述
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午4:13:54<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class Presence {
    private long uin;
    private byte mode = Mode.AVAILABLE.value();
    private long activeTime = System.currentTimeMillis();
    private String status;

    public long getUin() {
        return uin;
    }

    public void setUin(long uin) {
        this.uin = uin;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    /**
     * An enum to represent the presence mode.
     */
    public enum Mode {
        /**
         * Unavailable(the default)
         */
        UNAVAILABLE(-1),

        /**
         * Available
         */
        AVAILABLE(0),


        /**
         * Work
         */
        WORK(1),

        /**
         * Do not disturb.
         */
        DND(2),

        /**
         * Away.
         */
        AWAY(3);

        private byte mValue = 0;

        public byte value() {
            return mValue;
        }

        Mode(int value) {
            mValue = (byte) value;
        }

        public static Mode valueOfRaw(byte value) {
            for (Mode v : Mode.values()) {
                if (v.value() == value) {
                    return v;
                }
            }
            return UNAVAILABLE;
        }

    }
}
