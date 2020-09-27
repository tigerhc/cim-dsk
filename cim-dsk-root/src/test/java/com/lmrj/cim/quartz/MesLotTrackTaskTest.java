package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class MesLotTrackTaskTest {
    @Autowired
    MesLotTrackTask mesLotTrackTask;
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Test
    public void fixLotTrackData() {
        mesLotTrackTask.fixLotTrackData();
    }
    @Test
    public void ss(){
        String eqpId="SIM-DM1";
        String date = "2020-09-26";
        Date time = DateUtil.parseDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        Date startTime=cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR,1);
        Date endTime=cal.getTime();
        //查询当天Production日志记录生成文件，首先查询批次
        List<MesLotTrack> lotList=iMesLotTrackService.selectList(new EntityWrapper<MesLotTrack>().eq("eqp_id", eqpId).between("start_time",startTime,endTime).orderBy("start_time"));
        System.out.println(lotList);
    }
}
