package com.lmrj.rms.recipe.entity;

import com.lmrj.rms.recipe.utils.FixedLength;

public class TRXO {

    private String trxId;
    private String typeId;
    private String result;
    private String msg;

    public TRXO() {
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId, Integer length) {
        this.trxId = FixedLength.toFixedLengthString(trxId, length);
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId, Integer length) {
        this.typeId = FixedLength.toFixedLengthString(typeId, length);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result, Integer length) {
        this.result = FixedLength.toFixedLengthString(result, length);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg, Integer length) {
        this.msg = FixedLength.toFixedLengthString(msg, length);
    }
}
