package com.lmrj.fab.eqp.service.impl;

import com.google.common.collect.Lists;
import com.lmrj.fab.FabBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by zwj on 2019/10/17.
 */
@SpringBootTest(classes = FabBootApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class FabEquipmentStatusServiceImplTest {

    @Autowired
    FabEquipmentStatusServiceImpl fabEquipmentStatusServiceImpl;
    @Test
    public void initStatus() throws Exception {
        List<String> ids = Lists.newArrayList();
        ids.add("f610141cbd104c32909dac10e7be2a67");
        fabEquipmentStatusServiceImpl.initStatus(ids);
    }

}