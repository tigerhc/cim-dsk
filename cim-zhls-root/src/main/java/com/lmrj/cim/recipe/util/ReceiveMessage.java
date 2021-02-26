package com.lmrj.cim.recipe.util;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.lmrj.cim.recipe.service.impl.RecipeServiceImpl;
import com.lmrj.rms.recipe.entity.TRXO;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.service.impl.RmsRecipeServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.UnsupportedEncodingException;

/**
 * ibm mq 消息接受者
 */
@Component
@Slf4j
public class ReceiveMessage  extends MessageListenerAdapter {
    @Autowired
    JmsOperations jmsOperations;
    private String qManager;// QueueManager名
    private MQQueueManager qMgr;
    private MQQueue qQueue2;
    String HOST_NAME;
    int PORT = 0;
    String Q_NAME2;
    String CHANNEL;
    int CCSID;


    @Autowired
    private IRmsRecipeBodyService rmsRecipeBodyService;

    @SneakyThrows
    @Override
    @JmsListener(destination = "LQ1WM1R02I") //队列
    public void onMessage(Message message) {
        String str = null;
        // 1、读取报文
        try {
            if (message instanceof BytesMessage) {
                BytesMessage bm = (BytesMessage) message;
                //bm.getJMSCorrelationIDAsBytes()
                byte[] bys = null;
                bys = new byte[(int) bm.getBodyLength()];
                bm.readBytes(bys);
                str = new String(bys, "UTF-8");
            } else {
                str = ((TextMessage) message).getText();
                str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            }
            log.info("MQ传来的值为:{}" , str);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ReceiveMessage mqst = new ReceiveMessage();
        this.sendMsg("TX105O000000000000".getBytes("UTF-8"), hexStringToByteArray( message.getJMSMessageID().substring(3)), message.getJMSCorrelationIDAsBytes());

        mqst.finalizer();
    }

    @JmsListener(destination = "LQ1WM1R01I")
    public void getMsg2(Message msg) throws Exception {
        String str = null;
        System.out.println(msg);

        if (msg instanceof BytesMessage) {
            BytesMessage bm = (BytesMessage) msg;
            byte[] bys = null;
            bys = new byte[(int) bm.getBodyLength()];
            bm.readBytes(bys);
            str = new String(bys, "UTF-8");
        } else {
            str = ((TextMessage) msg).getText();
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }

        String eqpId = str.substring(106,116).trim();
        String recipeCode = str.substring(6,106).trim();

        String recipeBodySize = str.substring(116,121);
        String recipeBody = str.substring(121);

        TRXO trxo = rmsRecipeBodyService.checkRecipeBody(eqpId, recipeCode, recipeBody, recipeBodySize);

        String replyMsg = trxo.getTrxId() + trxo.getTypeId() + trxo.getResult() + trxo.getMsg();
        this.sendMsg(replyMsg.getBytes("UTF-8"), hexStringToByteArray( msg.getJMSMessageID().substring(3)), msg.getJMSCorrelationIDAsBytes());
    }

    @JmsListener(destination = "LQ1WM1RMSI")
    public void getReply(Message msg) throws Exception {
        String str = null;
        System.out.println(msg);

        if (msg instanceof BytesMessage) {
            BytesMessage bm = (BytesMessage) msg;
            byte[] bys = null;
            bys = new byte[(int) bm.getBodyLength()];
            bm.readBytes(bys);
            str = new String(bys, "UTF-8");
        } else {
            str = ((TextMessage) msg).getText();
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        System.out.println(str);
        String trxId = str.substring(0, 5);
        switch (trxId) {
            case "TXR03" :
                recipeList(str);
                break;
            case "TXR04" :
                System.out.println("recipe上传参数格式");
                break;
            case "TXR05" :
                System.out.println("recipe上传单文本");
                break;
            case "TXR06" :
                System.out.println("下载recipe");
                break;
            default:
                log.error("不识别的TRX");
                break;
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        if (s.indexOf(" ") > 0) {
            s = s.replaceAll(" ", "");
        }

        int len = s.length();
        byte[] ba = new byte[len / 2];

        for(int i = 0; i < ba.length; ++i) {
            int j = i * 2;
            int t = Integer.parseInt(s.substring(j, j + 2), 16);
            byte b = (byte)(t & 255);
            ba[i] = b;
        }

        return ba;
    }

    @PostConstruct
    public void init() {
        try {
            HOST_NAME = "10.210.64.102";//Hostname或IP
            PORT = 1414;//要有一个侦听器，处于活动状态，且监听1414端口
            qManager = "QMWM1";
            Q_NAME2 = "LQ1WM1105O";//Q1是一个本地队列
            CHANNEL = "CVRWVCHN01";//QM_APPLE上要建一个名为DC.SVRCONN的服务器连接通道
            CCSID = 1208; // 表示是简体中文，
            // CCSID的值在AIX上一般设为1383，如果要支持GBK则设为1386，在WIN上设为1381。
            MQEnvironment.hostname = HOST_NAME; // 安裝MQ所在的ip address
            MQEnvironment.port = PORT; // TCP/IP port
            MQEnvironment.channel = CHANNEL;
            MQEnvironment.CCSID = CCSID;
            qMgr = new MQQueueManager(qManager);
            int qOptioin = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_INQUIRE
                    | MQC.MQOO_OUTPUT;
            qQueue2 = qMgr.accessQueue(Q_NAME2, 9264);
        } catch (MQException e) {
            System.out
                    .println("A WebSphere MQ error occurred : Completion code "
                            + e.completionCode + " Reason Code is "
                            + e.reasonCode);
        }
    }

    @PreDestroy
    void finalizer() {
        try {
            qQueue2.close();
            qMgr.disconnect();
        } catch (MQException e) {
            System.out
                    .println("A WebSphere MQ error occurred : Completion code "
                            + e.completionCode + " Reason Code is "
                            + e.reasonCode);
        }
    }

    public void sendMsg(byte[] qByte, byte[] messageId, byte[]  correlationId) {
        try {
            MQMessage qMsg = new MQMessage();
            qMsg.correlationId = correlationId;
            qMsg.messageId = messageId;
            qMsg.characterSet = 1208;
            qMsg.encoding=546;
            qMsg.write(qByte);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            qQueue2.put(qMsg, pmo);
            System.out.println("----"+qMsg);
            System.out.println("\tThe message is " + new String(qByte, "GBK"));
        } catch (MQException e) {
            System.out
                    .println("A WebSphere MQ error occurred : Completion code "
                            + e.completionCode + " Reason Code is "
                            + e.reasonCode);
        } catch (java.io.IOException e) {
            System.out
                    .println("An error occurred whilst to the message buffer "
                            + e);
        }
    }

    private void recipeList(String message) {
        String trxId = message.substring(0, 5);
        String typeId = message.substring(5, 6);
        String eqpId = message.substring(6, 16);
        String result = message.substring(16, 17);
        String msg = message.substring(17, 117);
        String recipeCodeSize = message.substring(117, 122);
        int start = 122;
        int size = Integer.parseInt(recipeCodeSize);
        for (int i = 0; i < size; i++) {
            String recipeCode;
            if (i < size - 1) {
                recipeCode = message.substring(start + i * 100, start + (i + 1) * 100).trim().split("=")[1];
            } else {
                recipeCode = message.substring(start + i * 100).trim().split("=")[1];
            }
            RecipeServiceImpl.recipeList.add(recipeCode);
        }
    }

}


