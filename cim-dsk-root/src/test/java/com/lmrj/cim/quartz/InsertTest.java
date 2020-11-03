package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class InsertTest {
    @Autowired
    RptYieldDayTask rptYieldDayTask;
    @Autowired
    RptYieldTask rptYieldTask;
    @Autowired
    EqpStateTask eqpStateTask;
    @Autowired
    OperationYieldTask operationYieldTask;
    @Autowired
    EdcAmsRecordYieldTask edcAmsRecordYieldTask;
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    IEdcDskLogOperationService iEdcDskLogOperationService;
    @Autowired
    IEdcDskLogRecipeService iEdcDskLogRecipeService;
    @Test
    public void test() throws Exception{
        log.info("000000");
        List<EdcDskLogOperation> list1= new ArrayList<>();
        for (int i = 1000; i > 0 ; i--) {
            EdcDskLogOperation edcDskLogOperation = new EdcDskLogOperation();
            edcDskLogOperation.setId(StringUtil.randomTimeUUID());
            edcDskLogOperation.setEventId("0001");
            list1.add(edcDskLogOperation);
        }
        iEdcDskLogOperationService.insertList(list1);
        log.info("111111");
    }
    @Test
    public void test1() throws Exception{
        /*log.error("000000");
        List<EdcDskLogOperation> list1= new ArrayList<>();
        for (int i = 10; i > 0 ; i--) {
            EdcDskLogOperation edcDskLogOperation = new EdcDskLogOperation();
            edcDskLogOperation.setId(StringUtil.randomTimeUUID());
            edcDskLogOperation.setEventId("0001");
            list1.add(edcDskLogOperation);
        }
        iEdcDskLogOperationService.insertBatch(list1,1000000);
        //iEdcDskLogOperationService.updateBatchById(list1,10000);
        log.error("111111");*/
        EdcDskLogRecipe edcDskLogRecipe=new EdcDskLogRecipe();
        edcDskLogRecipe.setCreateBy("00");
        iEdcDskLogRecipeService.insertOrUpdate(edcDskLogRecipe);
    }
}
