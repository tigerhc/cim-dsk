package com.lmrj.cim.ibmmq.util;

import com.lmrj.cim.recipe.service.impl.RecipeServiceImpl;
import com.lmrj.cim.recipe.util.ReceiveMessage;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.core.sys.entity.User;
import com.lmrj.rms.permit.utils.ShiroExt;
import com.lmrj.rms.recipe.utils.FixedLength;
import org.apache.shiro.SecurityUtils;
import static org.junit.Assert.*;
import com.ibm.mq.*;
import com.lmrj.util.string.HexUtil;

import java.math.BigInteger;

public class ReceiveMessageTest {

    private static MQQueueManager qMgr;
    private static int CCSID = 1381;
    private static String queueString = "LQWM2RMSI";// "LQ1WM2RMSI";

    public static void connect() throws MQException {
        MQEnvironment.hostname = "10.210.64.102";
        MQEnvironment.channel = "CVRWVCHN01";
        MQEnvironment.port = 1414;
        MQEnvironment.CCSID = CCSID;
        //MQ中拥有权限的用户名
        MQEnvironment.userID = "";
        //用户名对应的密码
        MQEnvironment.password = "";

        qMgr = new MQQueueManager("QMWM1");

    }

    //public static void sendMsg(String msgStr) {
    //    int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
    //    MQQueue queue = null;
    //    try {
    //        // 建立Q1通道的连接
    //        queue = qMgr.accessQueue(queueString, openOptions, null, null, null);
    //        MQMessage msg = new MQMessage();// 要写入队列的消息
    //        msg.format = MQC.MQFMT_STRING;
    //        msg.characterSet = CCSID;
    //        msg.encoding = CCSID;
    //        // msg.writeObject(msgStr); //将消息写入消息对象中
    //        byte[] messageId = new byte[24];
    //        //需要把设备号转为byte后放入messageid
    //        byte[] messages = "23PWEK07TCSX".getBytes();
    //        for (int i = 0; i < messageId.length; i++) {
    //            if( i < messages.length){
    //                messageId[i] = messages[i];
    //            }else{
    //                messageId[i] = 0X20;
    //            }
    //        }
    //
    //        msg.messageId = messageId;
    //
    //        msg.writeString(msgStr);
    //        MQPutMessageOptions pmo = new MQPutMessageOptions();
    //        msg.expiry = -1; // 设置消息用不过期
    //        queue.put(msg, pmo);// 将消息放入队列
    //    } catch (Exception e) {
    //        // TODO Auto-generated catch block
    //        e.printStackTrace();
    //    } finally {
    //        if (queue != null) {
    //            try {
    //                queue.close();
    //            } catch (MQException e) {
    //                // TODO Auto-generated catch block
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //}

    public static void receiveMsg() {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            queue = qMgr.accessQueue(queueString, openOptions, null, null, null);
            System.out.println("该队列当前的深度为:" + queue.getCurrentDepth());
            System.out.println("===========================");
            int depth = queue.getCurrentDepth();
            // 将队列的里的消息读出来
            while (depth-- > 0) {
                MQMessage msg = new MQMessage();// 要读的队列的消息
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                queue.get(msg, gmo);
                System.out.println("消息的大小为：" + msg.getDataLength());
                System.out.println("消息的内容：\n" + msg.readStringOfByteLength(msg.getDataLength()));
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String trxId = "TXR04";
        String typeId = "I";
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String userId = "2";
        trxId = FixedLength.toFixedLengthString(trxId, 5);
        typeId = FixedLength.toFixedLengthString(typeId, 1);
        String eqpId = FixedLength.toFixedLengthString("23PWEK07", 10);
        String recipeCode = FixedLength.toFixedLengthString("xxxx", 100);
        userId = FixedLength.toFixedLengthString(userId, 20);
        String msg = trxId + typeId + eqpId + userId;
        ReceiveMessage.sendMsg(msg,"23PWEK07TCSX", "LQWM2RMSI", null);
    }
}
