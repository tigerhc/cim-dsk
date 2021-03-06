package com.lmrj.core.entity;

/**
 * Created by zwj on 2019/6/16.
 */
public class MesResult {
    private static  final String OK_CODE = "Y";
    private static  final String ERROR_CODE = "N";

    private String flag;
    private String msg;
    private Object content;

    public MesResult() {
        flag = "Y";
        msg = "";
    }

    public static MesResult ok(){
        return new MesResult();
    }

    public static MesResult ok(String msg){
        return init(OK_CODE, msg, null);
    }

    public static MesResult ok(Object content){
        return init(OK_CODE, "", content);
    }

    public static MesResult ok(String msg, Object content){
        return init(OK_CODE, msg, content);
    }
    public static MesResult error(String msg){
        return init(ERROR_CODE, msg, null);
    }

    public static MesResult init(String flag, String msg, Object content){
        MesResult mesResult = new MesResult();
        mesResult.flag = flag;
        mesResult.msg = msg;
        mesResult.content = content;
        return mesResult;
    }

    public String getFlag() {
        return flag;
    }
    public boolean isOk() {
        return OK_CODE.equals(this.flag);
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
