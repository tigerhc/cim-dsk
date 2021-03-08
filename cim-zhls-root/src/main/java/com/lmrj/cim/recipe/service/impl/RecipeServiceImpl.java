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
        ReceiveMessage.sendMsg(msg, "23PWEK07TCSX", "LQWM2RMSI", null);
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
            ReceiveMessage.sendMsg(msg, "23PWEK07TCSX", "LQWM2RMSI", null);
            log.info("发送至 LQWM2RMSI({});", msg);
        }
        return true;
    }

}

