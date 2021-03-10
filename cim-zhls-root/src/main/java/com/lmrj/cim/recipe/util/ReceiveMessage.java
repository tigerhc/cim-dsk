package com.lmrj.cim.recipe.util;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.lmrj.cim.recipe.service.impl.RecipeServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.rms.log.service.IRmsRecipeLogService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.entity.TRXO;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.rms.recipe.service.impl.RmsRecipeServiceImpl;
import com.lmrj.util.lang.StringUtil;
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
import java.util.ArrayList;
import java.util.List;

/**
 * ibm mq 消息接受者
 */
@Component
@Slf4j
public class ReceiveMessage  extends MessageListenerAdapter {
    @Autowired
    private JmsOperations jmsOperations;
    private static String qManager;
    private MQQueueManager qMgr;
    private MQQueue qQueue2;
    private String HOST_NAME;
    private int PORT = 0;
    private String CHANNEL;
    private int CCSID;
    private static int qOptioin;

    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IRmsRecipeService rmsRecipeService;
    @Autowired
    private IRmsRecipeLogService rmsRecipeLogService;


    @Autowired
    private IRmsRecipeBodyService rmsRecipeBodyService;

    @SneakyThrows
    @Override
//    @JmsListener(destination = "LQWM2R02I") //队列
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
        sendMsg("TX105O000000000000", message.getJMSMessageID().substring(3),"" ,message.getJMSCorrelationIDAsBytes());

//        mqst.finalizer();
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

        if (StringUtil.isEmpty(trxo.getMsg())) {
            trxo.setMsg("", 100);
        }

        String replyMsg = trxo.getTrxId() + trxo.getTypeId() + trxo.getResult() + trxo.getMsg();
//        sendMsg(replyMsg, msg.getJMSMessageID().substring(3), "LQ1WM1R01O", msg.getJMSCorrelationIDAsBytes());
        this.reply("LQ1WM1R01O" , replyMsg.getBytes("UTF-8"), hexStringToByteArray(msg.getJMSMessageID().substring(3)), msg.getJMSCorrelationIDAsBytes());
        log.info("发送至 LQ1WM1R01O({});", replyMsg);
    }

    @JmsListener(destination = "LQWM2RMSO")
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
                //获取recipe列表
                recipeList(str);
                break;
            case "TXR04" :
                //recipe上传参数格式
                uploadRecipe(str);
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
            //QMWM1/CVRWVCHN01/10.210.66.5(41416)
            //10.210.64.102(1414)
            //Hostname或IP
            HOST_NAME = "10.210.66.5";
            //要有一个侦听器，处于活动状态，且监听1414端口
            PORT = 41416;
            qManager = "QMWM1";
            //Q1是一个本地队列
            //Q_NAME2 = "LQ1WM1105O";
            //QM_APPLE上要建一个名为DC.SVRCONN的服务器连接通道
            CHANNEL = "CVRWVCHN01";
            // 表示是简体中文，CCSID的值在AIX上一般设为1383，如果要支持GBK则设为1386，在WIN上设为1381。
            CCSID = 1208;
            //MQ中拥有权限的用户名
            MQEnvironment.userID = "";
            //用户名对应的密码
            MQEnvironment.password = "";
            // 安裝MQ所在的ip address
            MQEnvironment.hostname = HOST_NAME;
            // TCP/IP port
            MQEnvironment.port = PORT;
            MQEnvironment.channel = CHANNEL;
            MQEnvironment.CCSID = CCSID;
            qMgr = new MQQueueManager(qManager);
            qOptioin = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_INQUIRE
                    | MQC.MQOO_OUTPUT;
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

    public static void sendMsg(String msgStr, String messageIdStr, String qname, byte[]  correlationId) throws Exception{
//        MQEnvironment.hostname = "10.210.64.102";
//        MQEnvironment.channel = "CVRWVCHN01";
//        MQEnvironment.port = 1414;
//        MQEnvironment.CCSID = 1381;
//        //MQ中拥有权限的用户名
//        MQEnvironment.userID = "";
//        //用户名对应的密码
//        MQEnvironment.password = "";

        MQQueue queue = null;
        try {
            // 建立Q1通道的连接
            queue = new MQQueueManager(qManager).accessQueue(qname, qOptioin, null, null, null);
            // 要写入队列的消息
            MQMessage msg = new MQMessage();
            msg.correlationId = correlationId;
            msg.format = MQC.MQFMT_STRING;
            msg.characterSet = 1381;
            msg.encoding = 1381;
            byte[] messageId = new byte[24];
            //需要把设备号转为byte后放入messageid
            byte[] messages = messageIdStr.getBytes();
            for (int i = 0; i < messageId.length; i++) {
                if( i < messages.length){
                    messageId[i] = messages[i];
                }else{
                    messageId[i] = 0X20;
                }
            }

            msg.messageId = messageId;
            // msg.writeObject(msgStr); //将消息写入消息对象中
            msg.writeString(msgStr);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            // 设置消息用不过期
            msg.expiry = -1;
            // 将消息放入队列
            queue.put(msg, pmo);
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

     //this.reply(qname , "TX105O000000000000".getBytes("UTF-8"), hexStringToByteArray( message.getJMSMessageID().substring(3)), message.getJMSCorrelationIDAsBytes());
    public void reply(String qname, byte[] qByte, byte[] messageId, byte[]  correlationId) {
        try {
            MQMessage qMsg = new MQMessage();
            qMsg.correlationId = correlationId;
            qMsg.messageId = messageId;
            qMsg.characterSet = 1208;
            qMsg.encoding=546;
            qMsg.write(qByte);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            new MQQueueManager(qManager).accessQueue(qname, qOptioin, null, null, null).put(qMsg, pmo);
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
        try {
            String trxId = message.substring(0, 5);
            String typeId = message.substring(5, 6);
            String eqpId = message.substring(6, 16);
            String result = message.substring(16, 17);
            String msg = message.substring(17, 117);
            String recipeCodeSize = message.substring(117, 122);
            int start = 122;
            int size = 0;
            if(StringUtil.isEmpty(recipeCodeSize.trim())){
                log.error("NO RecipeSize");
                return;
            }
            size = Integer.parseInt(recipeCodeSize.trim());
            for (int i = 0; i < size; i++) {
                String recipeCode;
                if (i < size - 1) {
                    recipeCode = message.substring(start + i * 100, start + (i + 1) * 100).trim();
                } else {
                    recipeCode = message.substring(start + i * 100).trim();
                }
                RecipeServiceImpl.recipeList.add(recipeCode);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void uploadRecipe(String message) {
        try {
            String trxId = message.substring(0, 5);
            String typeId = message.substring(5, 6);
            String eqpId = message.substring(6, 16).trim();
            String recipeCode = message.substring(16, 116).trim();
            String flag = message.substring(116, 117);
            String msg = message.substring(117, 217);
            String recipeBodySize = message.substring(217, 222);
            int start = 222;
            int size = 0;
            if (StringUtil.isEmpty(recipeBodySize.trim())) {
                log.error("NO RecipeSize");
                return;
            }
            size = Integer.parseInt(recipeBodySize.trim());
            RmsRecipe rmsRecipe = new RmsRecipe();
            List<RmsRecipeBody> recipeBodyList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                RmsRecipeBody recipeBody = new RmsRecipeBody();
                String str;
                if (i < size - 1) {
                    str = message.substring(start + i * 100, start + (i + 1) * 100).trim();
                } else {
                    str = message.substring(start + i * 100).trim();
                }
                String[] strs = str.split("=");
                if (strs.length == 2) {
                    recipeBody.setParaName(strs[0]);
                    recipeBody.setParaCode(strs[0]);
                    recipeBody.setSetValue(strs[1]);
                    recipeBodyList.add(recipeBody);
                }
            }
            rmsRecipe.setRecipeName(recipeCode);
            recipeCode = recipeCode.replaceAll("\\\\", "-");
            rmsRecipe.setRecipeCode(recipeCode);
            rmsRecipe.setEqpId(eqpId);
            rmsRecipe.setRmsRecipeBodyDtlList(recipeBodyList);
            rmsRecipe.setStatus("0");
            rmsRecipe.setVersionType("DRAFT");
            rmsRecipe.setApproveStep("1");
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            rmsRecipe.setEqpModelId(fabEquipment.getModelId());
            rmsRecipe.setEqpModelName(fabEquipment.getModelName());
            rmsRecipeService.insert(rmsRecipe);
            rmsRecipeLogService.addLog(rmsRecipe, "upload", eqpId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}


