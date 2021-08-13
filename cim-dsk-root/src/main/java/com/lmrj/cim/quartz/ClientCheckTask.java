package com.lmrj.cim.quartz;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lmrj.core.entity.MesResult;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ClientCheckTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    IEdcDskLogOperationService iEdcDskLogOperationService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Value("${dsk.lineNo}")
    String lineNo;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;
    /**
     * 检查客户端连接状态，每十分钟检查一次
     * 每隔10分钟一次
     */
    //@Scheduled(cron = "0 20 * * * ?")
    public void clientCheck() {
        if(!jobenabled){
            return;
        }
        List<String> bcCodeList = new ArrayList<>();
        if ("SIM".equals(lineNo)) {
            bcCodeList.add("SIM-BC1");
            bcCodeList.add("SIM-BC2");
        } else if ("DM".equals(lineNo)) {
            bcCodeList.add("APJ-BC1");
            bcCodeList.add("APJ-BC2");
            bcCodeList.add("APJ-BC3");
            bcCodeList.add("APJ-BC4");
            bcCodeList.add("APJ-BC6");
            bcCodeList.add("APJ-BC7");
        }
        for (String bcCode : bcCodeList) {
            log.info("客户端：" + bcCode + "运行状态判断开始！");
            Boolean successFlag = false;
            Map<String, String> map = Maps.newHashMap();
            map.put("METHOD", "CLIENT_CHECK");
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bcCode, JsonUtil.toJsonString(map));
            if (replyMsg != null && !"".equals(replyMsg)) {
                MesResult result = JsonUtil.from(replyMsg, MesResult.class);
                String value = (String) result.getContent();
                if (!"".equals(value) && value != null) {
                    successFlag = true;
                }
            }
            if (successFlag) {
                log.info("客户端：" + bcCode + "运行状态判断结束！  客户端运行正常");
            } else {
                JSONObject jsonObject = new JSONObject();
                String jsonString = "";
                jsonObject.put("EQP_ID", "客户端：" + bcCode + "连接失败，请立刻检查上位机客户端eap程序运行情况及网络连接情况，若软件关闭，请立即将程序开启！");
                jsonObject.put("ALARM_CODE", "E-0020");
                jsonString = jsonObject.toJSONString();
                rabbitTemplate.convertAndSend("C2S.Q.MSG.MAIL", jsonString);
                log.error("客户端 " + bcCode + " 连接失败，已发送邮件通知！请开发人员及时处理");
            }
        }
    }
}
