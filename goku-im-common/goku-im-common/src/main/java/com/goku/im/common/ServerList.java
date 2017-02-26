package com.goku.im.common;

import java.io.Serializable;

public class ServerList implements Serializable {
    private Short id;

    private String name;

    private String serverName;

    private Integer serverId;

    private String classname;

    private String listener;

    private String role;

    private String portType;

    private String serviceType;

    private String serverType;

    private String ip;

    private Short port;

    private Integer idletime;

    private Short maxsessions;

    private Short recontime;

    private String parameters;

    private Integer cacheServerId;

    private Short shutPort;

    private Short parentId;

    private Byte isDynamic;

    private static final long serialVersionUID = 1L;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Short getPort() {
        return port;
    }
    
    public void setPort(Short port) {
        this.port = port;
    }

    public Integer getIdletime() {
        return idletime;
    }

    public void setIdletime(Integer idletime) {
        this.idletime = idletime;
    }

    public Short getMaxsessions() {
        return maxsessions;
    }

    public void setMaxsessions(Short maxsessions) {
        this.maxsessions = maxsessions;
    }

    public Short getRecontime() {
        return recontime;
    }

    public void setRecontime(Short recontime) {
        this.recontime = recontime;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Integer getCacheServerId() {
        return cacheServerId;
    }

    public void setCacheServerId(Integer cacheServerId) {
        this.cacheServerId = cacheServerId;
    }

    public Short getShutPort() {
        return shutPort;
    }

    public void setShutPort(Short shutPort) {
        this.shutPort = shutPort;
    }

    public Short getParentId() {
        return parentId;
    }
    
    public void setParentId(Short parentId) {
        this.parentId = parentId;
    }

    public Byte getIsDynamic() {
        return isDynamic;
    }

    public void setIsDynamic(Byte isDynamic) {
        this.isDynamic = isDynamic;
    }
}