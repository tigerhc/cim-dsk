package com.lmrj.cim.recipe.service.impl;

import com.ibm.mq.*;
import com.lmrj.cim.recipe.service.IRecipeService;
import com.lmrj.cim.recipe.util.ReceiveMessage;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.core.sys.entity.User;
import com.lmrj.rms.permit.utils.ShiroExt;
import com.lmrj.rms.recipe.utils.FixedLength;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("recipeService")
@Slf4j
public class RecipeServiceImpl implements IRecipeService {

    public static List<String> recipeList = new ArrayList<>();

    private ReceiveMessage receiveMessage = new ReceiveMessage();

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public List<String> selectRecipeList(String eqpId) throws Exception{
        String trxId = "TXR03";
        String typeId = "I";
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = ShiroExt.getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(id);
        String userId = user.getId();
        trxId = FixedLength.toFixedLengthString(trxId, 5);
        typeId = FixedLength.toFixedLengthString(typeId, 1);
        eqpId = FixedLength.toFixedLengthString("23PWEK07", 10);
        userId = FixedLength.toFixedLengthString(userId, 20);
        String msg = trxId + typeId + eqpId + userId;
        // TODO 发送MQ消息
//        jmsTemplate.convertAndSend("LQWM2RMSI", msg);
        sendMsg(msg, "23PWEK07TCSX");
        log.info("发送至 LQWM2RMSI({});", msg);
        int count = 0;
        while (recipeList.size() == 0) {
            try {
                log.info("未收到回复，休眠等待");
                count++;
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(count > 60){
                return new ArrayList<>();
            }

        }
        List<String> recipes = new ArrayList<>(recipeList);
        recipeList = new ArrayList<>();
        return recipes;
    }

    @Override
    public boolean uploadRecipe(String eqpId, List<String> recipeList) throws Exception {
        String trxId = "TXR04";
        String typeId = "I";
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = ShiroExt.getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(id);
        String userId = user.getId();
        userId = FixedLength.toFixedLengthString(userId, 20);
        eqpId = FixedLength.toFixedLengthString("23PWEK07", 10);
        for (String recipe : recipeList) {
            recipe = FixedLength.toFixedLengthString("recipe", 100);
            String msg = trxId + typeId + eqpId + recipe + userId;
            sendMsg(msg, "23PWEK07TCSX");
            log.info("发送至 LQWM2RMSI({});", msg);
        }
        return true;
    }

    public static void sendMsg(String msgStr, String messageIdStr) throws Exception{
        MQEnvironment.hostname = "10.210.64.102";
        MQEnvironment.channel = "CVRWVCHN01";
        MQEnvironment.port = 1414;
        MQEnvironment.CCSID = 1381;
        //MQ中拥有权限的用户名
        MQEnvironment.userID = "";
        //用户名对应的密码
        MQEnvironment.password = "";

        MQQueueManager qMgr = new MQQueueManager("QMWM1");
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            // 建立Q1通道的连接
            queue = new MQQueueManager("QMWM1").accessQueue("LQWM2RMSI", openOptions, null, null, null);
            MQMessage msg = new MQMessage();// 要写入队列的消息
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
}

