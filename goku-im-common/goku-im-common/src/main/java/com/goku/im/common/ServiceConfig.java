package com.goku.im.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 描述
 * 
 * 
 * 游戏服务器的配置 <b>类描述: </b><br/>
 * 
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月4日 下午5:20:05<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class ServiceConfig extends ServerList implements Serializable {
	private static final long serialVersionUID = 2808031788810701654L;
	
	/**
	 * 默认的端口类型
	 */
	public static final String DEFAULT_PORT_TYPE = "local";

	private boolean onlyConfig;

	/**
	 * @return the onlyConfig
	 */
	public boolean isOnlyConfig() {
		return onlyConfig;
	}

	/**
	 * @param onlyConfig
	 *            the onlyConfig to set
	 */
	public void setOnlyConfig(boolean onlyConfig) {
		this.onlyConfig = onlyConfig;
	}

	/**
	 * 每个服务的作用是什么
	 * 
	 * CONNECTOR 表示是socket的客户端，需要创建连接到服务端（ACCEPTOR) ACCEPTOR
	 * 表示socket的服务端，接受CONNECTOR的连接 OTHER 反射className的实现，启动Service
	 * 
	 */
	public static enum ROLE {
		CONNECTOR, ACCEPTOR, OTHER
	};

	/**
	 * 服务的类型，例如数据库服务，网关服务，登录服务，地图服务等 CONNECTOR制定的targetServiceType就是SERVICE_TYPE
	 */
	public static enum SERVICE_TYPE {
		ADMINSERVICE((byte) 1),

		LOGINSERVICE((byte) 2),

		REGSERVICE((byte) 3),

		AGENTSERVICE((byte) 4),

		MAPSERVICE((byte) 5),

		DBSERVICE((byte) 10),

		SHARESERVICE((byte) 20),

		MAINSERVICE((byte) 30),

		GATESERVICE((byte) 40),

		CACHESERVICE((byte) 100),

		ROBOTSERVICE((byte) 101),;

		SERVICE_TYPE(byte value) {
			this.value = value;
		}

		private byte value;

		public byte getValue() {
			return this.value;
		}

		private final static Map<Byte, SERVICE_TYPE> map = new HashMap<Byte, SERVICE_TYPE>();
		static {
			for (SERVICE_TYPE type : values()) {
				map.put(type.value, type);
			}
		}

		public static SERVICE_TYPE getType(byte value) {
			return map.get(value);
		}
	};
	
	/**
	 * 端口类型 LOCAL ip表示内网ip，port表示内网端口 REMOTE ip表示外网ip，port表示外网端口
	 */
	public static enum PORT_TYPE {
		LOCAL, REMOTE
	}
	
	/**
	 * 其他参数
	 */
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * 获取参数的值
	 * 
	 * @param parameter
	 * @return
	 */
	public String getParameter(String parameter) {
		return parameters.get(parameter);
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceConfig [getId()=" + getId() + ", getName()=" + getName() + ", getServerId()=" + getServerId()
				+ ", getClassname()=" + getClassname() + ", getListener()=" + getListener() + ", getRole()=" + getRole()
				+ ", getPortType()=" + getPortType() + ", getServiceType()=" + getServiceType() + ", getServerType()="
				+ getServerType() + ", getIp()=" + getIp() + ", getPort()=" + getPort() + ", getIdletime()="
				+ getIdletime() + ", getMaxsessions()=" + getMaxsessions() + ", getRecontime()=" + getRecontime()
				+ ", getParameters()=" + getParameters() + ", getCacheServerId()=" + getCacheServerId()
				+ ", getShutPort()=" + getShutPort() + ", getParentId()=" + getParentId() + ", getIsDynamic()="
				+ getIsDynamic() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + ", onlyConfig=" + onlyConfig + ", parameters=" + parameters + "]";
	}

	public String getPortType() {
		if (super.getPortType() == null) {
			setPortType(null);
		}
		return super.getPortType();
	}
	
	public void setPortType(String portType) {
		if (portType == null || (portType != null && portType.trim().isEmpty())) {
			super.setPortType(DEFAULT_PORT_TYPE);
		} else {
			super.setPortType(portType);
		}
	}
	
	public static void main(String[] args) {
		
	}
}
