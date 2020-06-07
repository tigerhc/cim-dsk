package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Scheduled(cron = "0 09 * * * ?")
    public void dskaps() {
        log.error("OperationYieldTask定时任务开始执行");
        //2天前
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        List<EdcDskLogOperation> operationList = edcDskLogOperationService.selectList(new EntityWrapper<EdcDskLogOperation>().eq("day_yield", "0").ge("create_date", cal.getTime()).like("eqp_id", "SIM-DM"));
        operationList.forEach(edcDskLogOperation -> {
            EdcDskLogProduction edcDskLogProduction = edcDskLogProductionService.findNextYield(edcDskLogOperation.getEqpId(), edcDskLogOperation.getStartTime());
            if (edcDskLogProduction != null) {
                int lotYield = edcDskLogProduction.getLotYield();
                int dayYield = edcDskLogProduction.getDayYield();
                edcDskLogOperation.setLotYield(lotYield);
                edcDskLogOperation.setDayYield(dayYield);
                edcDskLogOperationService.updateById(edcDskLogOperation);
            }
        });
        log.error("OperationYieldTask定时任务开始执行结束");
    }

}
