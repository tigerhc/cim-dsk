package com.lmrj.ms.webSocket.entity;

import java.io.Serializable;

public class FabEqptInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String eqpId;
    private Integer sortCode;
    private String officeId;
    private String officeName;
    private String officeIds;
    private String stepId;
    private String stepCode;
    private String activeFlag;
    private String bcCode;
    private String ip;
    private String port;
    private String deviceId;
    private String modelId;
    private String modelName;
    private String protocolName;
    private String fab;
    private String lineNo;
    private String location;
    private String status;
    private String wip;
    private String PPID;
    private String lotNo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEqpId() {
        return eqpId;
    }

    public void setEqpId(String eqpId) {
        this.eqpId = eqpId;
    }

    public Integer getSortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getBcCode() {
        return bcCode;
    }

    public void setBcCode(String bcCode) {
        this.bcCode = bcCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getFab() {
        return fab;
    }

    public void setFab(String fab) {
        this.fab = fab;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWip() {
        return wip;
    }

    public void setWip(String wip) {
        this.wip = wip;
    }

    public String getPPID() {
        return PPID;
    }

    public void setPPID(String PPID) {
        this.PPID = PPID;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    @Override
    public String toString() {
        return "FabEqptInfo{" +
                "id='" + id + '\'' +
                ", eqpId='" + eqpId + '\'' +
                ", sortCode=" + sortCode +
                ", officeId='" + officeId + '\'' +
                ", officeName='" + officeName + '\'' +
                ", officeIds='" + officeIds + '\'' +
                ", stepId='" + stepId + '\'' +
                ", stepCode='" + stepCode + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                ", bcCode='" + bcCode + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", modelId='" + modelId + '\'' +
                ", modelName='" + modelName + '\'' +
                ", protocolName='" + protocolName + '\'' +
                ", fab='" + fab + '\'' +
                ", lineNo='" + lineNo + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", PPID='" + PPID + '\'' +
                ", lotNo='" + lotNo + '\'' +
                '}';
    }
}
