package com.lmrj.cim.ibmmq.util;

import static org.junit.Assert.*;
import com.ibm.mq.*;

public class ReceiveMessageTest {

    private static MQQueueManager qMgr;
    private static int CCSID = 1381;
    private static String queueString = "MQ_SEND";

    public static void connect() throws MQException {
        MQEnvironment.hostname = "192.168.0.91";
        MQEnvironment.channel = "CHRCVR";
        MQEnvironment.port = 10001;
        MQEnvironment.CCSID = CCSID;
        //MQ中拥有权限的用户名
        MQEnvironment.userID = "root";
        //用户名对应的密码
        MQEnvironment.password = "Daoda@123";

        qMgr = new MQQueueManager("QM01");

    }

    public static void sendMsg(String msgStr) {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            // 建立Q1通道的连接
            queue = qMgr.accessQueue(queueString, openOptions, null, null, null);
            MQMessage msg = new MQMessage();// 要写入队列的消息
            msg.format = MQC.MQFMT_STRING;
            msg.characterSet = CCSID;
            msg.encoding = CCSID;
            // msg.writeObject(msgStr); //将消息写入消息对象中
            msg.writeString(msgStr);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            msg.expiry = -1; // 设置消息用不过期
            queue.put(msg, pmo);// 将消息放入队列
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

    public static void main(String[] args) throws MQException {
        connect();
        sendMsg("我来测试一下");
        receiveMsg();
    }
}

