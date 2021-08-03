package com.lmrj.edc.state.controller;

import com.google.common.collect.Lists;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.util.lang.ArrayUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.controller
 * @title: rpt_eqp_state_day控制器
 * @description: rpt_eqp_state_day控制器
 * @author: 张伟江
 * @date: 2020-02-20 01:26:27
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/rpteqpstateday")
@ViewPrefix("edc/rpteqpstateday")
@RequiresPathPermission("edc:rpteqpstateday")
@LogAspectj(title = "rpt_eqp_state_day")
public class RptEqpStateDayController extends BaseCRUDController<RptEqpStateDay> {
    @Value("${dsk.lineNo}")
    private String lineNo;
    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;

    @GetMapping("eqp")
    public Response day(String beginTime,String endTime,String eqpId){
        Response response=new Response();
        beginTime=beginTime.replaceAll("-","");
        endTime=endTime.replaceAll("-","");
        List eqpList = Lists.newArrayList();
        if(eqpId.contains(",")){
            eqpList = ArrayUtil.split(eqpId, ",");
        }else{
            eqpList = ArrayUtil.split(eqpId, "\n");
        }
        List<Map> list = rptEqpStateDayService.findEqpOee(beginTime, endTime, eqpList);
        if(!CollectionUtils.isEmpty(list)) {
            list.forEach(map -> {
                map.put("periodDate", getTimes((String) map.get("periodDate")));
                String runTime =String.valueOf(map.get("runTime"));
                BigDecimal runTimeR =new BigDecimal(runTime);
                /*String idleTime =String.valueOf(map.get("idleTime"));
                BigDecimal idleTimeR =new BigDecimal(idleTime);*/
                String downTime =String.valueOf(map.get("downTime"));
                BigDecimal downTimeR =new BigDecimal(downTime);
                //BigDecimal add =runTimeR.add(idleTimeR);
                BigDecimal result= runTimeR.add(downTimeR);

                String alarmTime =String.valueOf(map.get("alarmTime"));
                BigDecimal alarmTimeR =new BigDecimal(alarmTime);
                BigDecimal result1 = result.add(alarmTimeR);

                BigDecimal temp =new BigDecimal(24);
                BigDecimal idle =temp.subtract(result1);
                map.put("idleTime",Double.valueOf(idle.toString()));
                map.remove("otherTime");
            });
        }

        List<Map> list2 = rptEqpStateDayService.findEqpsOee(beginTime, endTime, eqpList);
        if(!CollectionUtils.isEmpty(list2)) {
            list2.forEach(map -> {
                String runTime =String.valueOf(map.get("runTime"));
                BigDecimal runTimeR =new BigDecimal(runTime);
                /*String idleTime =String.valueOf(map.get("idleTime"));
                BigDecimal idleTimeR =new BigDecimal(idleTime);*/
                String downTime =String.valueOf(map.get("downTime"));
                BigDecimal downTimeR =new BigDecimal(downTime);
                //BigDecimal add =runTimeR.add(idleTimeR);
                BigDecimal result= runTimeR.add(downTimeR);

                String alarmTime =String.valueOf(map.get("alarmTime"));
                BigDecimal alarmTimeR =new BigDecimal(alarmTime);
                BigDecimal result1 = result.add(alarmTimeR);

                long countDay =(long)map.get("countDay");
                BigDecimal temp =new BigDecimal(24 * countDay);
                BigDecimal idle =temp.subtract(result1);
                if(Double.valueOf(idle.toString())<0){
                    idle =new BigDecimal(0);
                }
                map.put("idleTime",Double.valueOf(idle.toString()));
                map.remove("otherTime");
            });
        }
        response.put("count",list.size());
        response.put("eqpOee",list);
        response.put("eqpsOee",list2);
        return response;
    }
    public static Double getHour(Double second){
        return (double) Math.round(second/36)/100;
    }

    @RequestMapping("selectEqpList")
    public Response tempEqpList(){
        Response rs = Response.ok();
        if(lineNo.equals("SIM")){//应苏科长要求,REFLOW1 显示REFLOW1,个别设备冰箱等显示描述
            String str = "[{\"id\":\"SIM-PRINTER1\",\"name\":\"SIM-印刷机1\"},\n" +
                    "{\"id\":\"SIM-YGAZO1\",\"name\":\"SIM-印刷后画像1#\"},\n" +
                    "{\"id\":\"SIM-DM1\",\"name\":\"SIM-DM1#\"},\n" +
                    "{\"id\":\"SIM-DM2\",\"name\":\"SIM-DM2#\"},\n" +
                    "{\"id\":\"SIM-DM3\",\"name\":\"SIM-DM3#\"},\n" +
                    "{\"id\":\"SIM-DM4\",\"name\":\"SIM-DM4#\"},\n" +
                    "{\"id\":\"SIM-DM5\",\"name\":\"SIM-DM5#\"},\n" +
                    "{\"id\":\"SIM-DM6\",\"name\":\"SIM-DM6#\"},\n" +
                    "{\"id\":\"SIM-DM7\",\"name\":\"SIM-DM7#\"},\n" +
                    "{\"id\":\"SIM-REFLOW1\",\"name\":\"SIM-回流焊1#\"},\n" +
                    "{\"id\":\"SIM-HGAZO1\",\"name\":\"SIM-回流焊后画像1#\"},\n" +
                    "{\"id\":\"SIM-WB-1A\",\"name\":\"SIM-WB焊线机1A\"},\n" +
                    "{\"id\":\"SIM-WB-1B\",\"name\":\"SIM-WB焊线机1B\"},\n" +
                    "{\"id\":\"SIM-WB-2A\",\"name\":\"SIM-WB焊线机2A\"},\n" +
                    "{\"id\":\"SIM-WB-2B\",\"name\":\"SIM-WB焊线机2B\"},\n" +
                    "{\"id\":\"SIM-WB-3A\",\"name\":\"SIM-WB焊线机3A\"},\n" +
                    "{\"id\":\"SIM-WB-3B\",\"name\":\"SIM-WB焊线机3B\"},\n" +
                    "{\"id\":\"SIM-WB-4A\",\"name\":\"SIM-WB焊线机4A\"},\n" +
                    "{\"id\":\"SIM-WB-4B\",\"name\":\"SIM-WB焊线机4B\"},\n" +
                    "{\"id\":\"SIM-WB-5A\",\"name\":\"SIM-WB焊线机5A\"},\n" +
                    "{\"id\":\"SIM-WB-5B\",\"name\":\"SIM-WB焊线机5B\"},\n" +
                    "{\"id\":\"SIM-WB-6A\",\"name\":\"SIM-WB焊线机6A\"},\n" +
                    "{\"id\":\"SIM-WB-6B\",\"name\":\"SIM-WB焊线机6B\"},\n" +
                    "{\"id\":\"SIM-TRM1\",\"name\":\"SIM-TRM塑封机1\"},\n" +
                    "{\"id\":\"SIM-TRM2\",\"name\":\"SIM-TRM塑封机2\"},\n" +
                    "{\"id\":\"SIM-LF1\",\"name\":\"SIM-分离1\"},\n" +
                    "{\"id\":\"SIM-HTRT1\",\"name\":\"SIM-检查1\"},\n" +
                    "{\"id\":\"SIM-LF2\",\"name\":\"SIM-分离2\"},\n" +
                    "{\"id\":\"SIM-HTRT2\",\"name\":\"SIM-检查2\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("id", list);
        } else {
            String str = "[{\"id\":\"DM-IGBT-SMT1\",\"name\":\"DM-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"DM-IGBT-PRINTER1\",\"name\":\"DM-IGBT印刷机\"},\n" +
                    "{\"id\":\"DM-IGBT-2DAOI1\",\"name\":\"DM-IGBT印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"DM-IGBT-REFLOW1\",\"name\":\"DM-IGBT印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-IGBT-3DAOI1\",\"name\":\"DM-IGBT印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"DM-IGBT-SORT1\",\"name\":\"DM-IGBT印刷下料机\"},\n" +
                    "{\"id\":\"DM-IGBT-SORT2\",\"name\":\"DM-IGBT预结合上料机\"},\n" +
                    "{\"id\":\"DM-IGBT-DM1\",\"name\":\"DM-IGBT预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"DM-IGBT-SORT3\",\"name\":\"DM-IGBT预结合下料机\"},\n" +
                    "{\"id\":\"DM-FRD-SMT1\",\"name\":\"DM-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"DM-FRD-PRINTER1\",\"name\":\"DM-FRD印刷机\"},\n" +
                    "{\"id\":\"DM-FRD-2DAOI1\",\"name\":\"DM-FRD印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"DM-FRD-REFLOW1\",\"name\":\"DM-FRD印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-FRD-3DAOI1\",\"name\":\"DM-FRD印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"DM-FRD-SORT1\",\"name\":\"DM-FRD印刷下料机\"},\n" +
                    "{\"id\":\"DM-FRD-SORT2\",\"name\":\"DM-FRD预结合上料机\"},\n" +
                    "{\"id\":\"DM-FRD-DM1\",\"name\":\"DM-FRD预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"DM-FRD-SORT3\",\"name\":\"DM-FRD预结合下料机\"},\n" +
                    "{\"id\":\"DM-HB1-SORT1\",\"name\":\"DM-一次热压上料机\"},\n" +
                    "{\"id\":\"DM-HB1-SINTERING1\",\"name\":\"DM-一次热压机\"},\n" +
                    "{\"id\":\"DM-HB1-3DAOI1\",\"name\":\"DM-一次热压3D画像检查机\"},\n" +
                    "{\"id\":\"DM-HB1-SORT2\",\"name\":\"DM-一次热压下料机\"},\n" +
                    "{\"id\":\"DM-VI1\",\"name\":\"DM-中间耐压检查一体机\"},\n" +
                    "{\"id\":\"DM-DBCT-SORT1\",\"name\":\"DM-上基板印刷上料机   \"},\n" +
                    "{\"id\":\"DM-DBCT-PRINTER1\",\"name\":\"DM-上基板印刷机\"},\n" +
                    "{\"id\":\"DM-DBCT-2DAOI1\",\"name\":\"DM-上基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCT-REFLOW1\",\"name\":\"DM-上基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-DBCT-3DAOI1\",\"name\":\"DM-上基板3D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCT-SORT2\",\"name\":\"DM-上基板印刷下料机\"},\n" +
                    "{\"id\":\"DM-DBCB-SORT1\",\"name\":\"DM-下基板印刷上料机   \"},\n" +
                    "{\"id\":\"DM-DBCB-PRINTER1\",\"name\":\"DM-下基板印刷机\"},\n" +
                    "{\"id\":\"DM-DBCB-2DAOI1\",\"name\":\"DM-下基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCB-REFLOW1\",\"name\":\"DM-下基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-DBCB-3DAOI1\",\"name\":\"DM-下基板3D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCB-SORT2\",\"name\":\"DM-下基板印刷下料机\"},\n" +
                    "{\"id\":\"DM-HB2-SORT1\",\"name\":\"DM-二次热压上料机\"},\n" +
                    "{\"id\":\"DM-HB2-DISPENSING1\",\"name\":\"DM-二次热压点胶机1\"},\n" +
                    "{\"id\":\"DM-HB2-DISPENSING2\",\"name\":\"DM-二次热压点胶机2\"},\n" +
                    "{\"id\":\"DM-HB2-DISPENSING3\",\"name\":\"DM-二次热压点胶机3\"},\n" +
                    "{\"id\":\"DM-HB2-2DAOI1\",\"name\":\"DM-二次热压2D画像检查机1\"},\n" +
                    "{\"id\":\"DM-HB2-SMT1\",\"name\":\"DM-二次热压贴片机1\"},\n" +
                    "{\"id\":\"DM-HB2-SMT2\",\"name\":\"DM-二次热压贴片机2\"},\n" +
                    "{\"id\":\"DM-HB2-2DAOI2\",\"name\":\"DM-二次热压2D画像检查机2\"},\n" +
                    "{\"id\":\"DM-HB2-ASSEMBLY1\",\"name\":\"DM-二次热压组立机\"},\n" +
                    "{\"id\":\"DM-HB2-SINTERING1\",\"name\":\"DM-二次热压机\"},\n" +
                    "{\"id\":\"DM-HB2-SORT2\",\"name\":\"DM-二次热压下料机\"},\n" +
                    "{\"id\":\"DM-XRAY1\",\"name\":\"DM-X射线检查机\"},\n" +
                    "{\"id\":\"DM-CLEAN-US1\",\"name\":\"DM-US1洗净机\"},\n" +
                    "{\"id\":\"DM-CLEAN-JET1\",\"name\":\"DM-JET洗净机\"},\n" +
                    "{\"id\":\"DM-TRM1\",\"name\":\"DM-TRM1\"},\n" +
                    "{\"id\":\"DM-AT1\",\"name\":\"DM-耐老化试验搬送机\"},\n" +
                    "{\"id\":\"DM-LF1\",\"name\":\"DM-分离检查机\"},\n" +
                    "{\"id\":\"DM-HTRT1\",\"name\":\"DM-高温室温检查机\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("id", list);
        }
        return rs;
    }



    private String getTimes(String str){

        String time=str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6);

        return time;
    }
}
