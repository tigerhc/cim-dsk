package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Slf4j
@Component
public class OperationYieldTask {

    @Autowired
    private IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

    /**
     * 计算产量,写入操操作日志
     */
    //@Scheduled(cron = "0 0/10 * * * ?")
    public void updateOperationYield() {
        log.info("OperationYieldTask定时任务开始执行");
        //2天前
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        cal.add(Calendar.HOUR_OF_DAY, -2);
        List<EdcDskLogOperation> operationList = edcDskLogOperationService.selectList(new EntityWrapper<EdcDskLogOperation>().eq("day_yield", "0").ge("create_date", cal.getTime()).like("eqp_id", "SIM-DM"));
        operationList.forEach(edcDskLogOperation -> {
            if(edcDskLogOperation.getLotYield()==0 || edcDskLogOperation.getDayYield()==0){
                EdcDskLogProduction edcDskLogProduction = edcDskLogProductionService.findLastYield(edcDskLogOperation.getEqpId(),edcDskLogOperation.getLotNo() ,edcDskLogOperation.getStartTime());
                if (edcDskLogProduction != null) {
                    int lotYield = edcDskLogProduction.getLotYield();
                    int dayYield = edcDskLogProduction.getDayYield();
                    edcDskLogOperation.setLotYield(lotYield);
                    edcDskLogOperation.setDayYield(dayYield);
                    edcDskLogOperationService.updateById(edcDskLogOperation);
                }
            }
        });
        log.info("OperationYieldTask定时任务开始执行结束");
    }

}
