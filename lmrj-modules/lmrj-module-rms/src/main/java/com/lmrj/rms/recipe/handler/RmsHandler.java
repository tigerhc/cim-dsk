package com.lmrj.rms.recipe.handler;

import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.service.impl.RmsRecipeServiceImpl;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RmsHandler {

    @Autowired
    private RmsRecipeServiceImpl rmsRecipeServiceImpl;

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.RMS.UPLOAD"})
    public void repeatAlarm(String msg) {
        Map<String, String> msgMap = JsonUtil.from(msg, Map.class);
        RmsRecipe rmsRecipe = JsonUtil.from(msgMap.get("recipe"), RmsRecipe.class);
        try {
            rmsRecipeServiceImpl.repeatUpload(rmsRecipe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
