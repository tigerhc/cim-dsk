package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class YieldCheckTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    IEdcDskLogOperationService iEdcDskLogOperationService;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;
    /**
     * 生成TRM前一天的表格
     * 每隔10分钟一次
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void yieldCheck() {
        if(!jobenabled){
            return;
        }
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY,-1);
        endTime=cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        Date startTime=cal.getTime();
        log.info("各个批次产量异常判定定时任务开始执行");
        File erroFiletxt = new File("E:\\GXJTest\\yieldCheck.txt");
        List<MesLotTrack> list = mesLotTrackService.findCorrectData(startTime,endTime);
        List<String> yieldCheckList = new ArrayList<>();
        for (MesLotTrack lotTrack : list) {
            if(lotTrack.getEndTime()!=null){
                if(lotTrack.getUpdateDate()==null){
                    yieldCheckList.add(lotTrack.getEqpId()+","+lotTrack.getLotNo()+","+lotTrack.getStartTime()+","+lotTrack.getLotYieldEqp());
                }else{
                    cal.add(Calendar.MINUTE,+30);
                    if(lotTrack.getUpdateDate().getTime()<cal.getTimeInMillis()){
                        yieldCheckList.add(lotTrack.getEqpId()+","+lotTrack.getLotNo()+","+lotTrack.getStartTime()+","+lotTrack.getLotYieldEqp());
                    }
                }
            }
        }
        try {
            FileUtil.writeLines(erroFiletxt,yieldCheckList,true);
        } catch (IOException e) {
            log.error("yieldCheck日志记录新增出错");
            e.printStackTrace();
        }
    }
}
