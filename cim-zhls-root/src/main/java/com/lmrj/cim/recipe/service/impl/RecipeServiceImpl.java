package com.lmrj.cim.recipe.service.impl;

import com.lmrj.cim.recipe.service.IRecipeService;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.core.sys.entity.User;
import com.lmrj.rms.permit.utils.ShiroExt;
import com.lmrj.rms.recipe.utils.FixedLength;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
        eqpId = FixedLength.toFixedLengthString(eqpId, 10);
        userId = FixedLength.toFixedLengthString(userId, 20);
        String msg = trxId + typeId + eqpId + userId;
        // TODO 发送MQ消息
        log.info("发送至 LQ1WM1RMSI({});", msg);
        while (recipeList.size() == 0) {
            try {
                log.info("未收到回复，休眠等待");
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        List<String> recipes = new ArrayList<>(recipeList);
        recipeList = new ArrayList<>();
        return recipes;
    }
}
