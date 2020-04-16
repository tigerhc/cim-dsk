package com.lmrj.fab.eqp.entity;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by zwj on 2019/6/9.
 */
public class FabEquipmentModelTest {
    @Test
    public void parseJson() throws Exception {
        List list = Lists.newArrayList();
        FabEquipmentModel model = new FabEquipmentModel();
        model.setId("111");
        model.setIconPath("111");
        model.setCreateDate(new Date());
        list.add(model);
        FabEquipmentModel model2 = new FabEquipmentModel();
        model2.setId("222");
        list.add(model2);
        String content = JSON.toJSONString(list);
        System.out.println(content);

        String content2 = JSON.toJSONString(model);
        System.out.println(content2);
        String content3=  JsonUtil.toJsonString(model);
        FabEquipmentModel edcParamRecord = JsonUtil.from(content2, FabEquipmentModel.class);
        FabEquipmentModel edcParamRecord2 = JsonUtil.from(content3, FabEquipmentModel.class);
        System.out.println(edcParamRecord2);


    }

}