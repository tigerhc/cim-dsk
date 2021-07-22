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
            String str = "[{\"eqpId\":\"SIM-PRINTER1\",\"eqpName\":\"SIM-印刷机1\"},\n" +
                    "{\"eqpId\":\"SIM-YGAZO1\",\"eqpName\":\"SIM-印刷后画像1#\"},\n" +
                    "{\"eqpId\":\"SIM-DM1\",\"eqpName\":\"SIM-DM1#\"},\n" +
                    "{\"eqpId\":\"SIM-DM2\",\"eqpName\":\"SIM-DM2#\"},\n" +
                    "{\"eqpId\":\"SIM-DM3\",\"eqpName\":\"SIM-DM3#\"},\n" +
                    "{\"eqpId\":\"SIM-DM4\",\"eqpName\":\"SIM-DM4#\"},\n" +
                    "{\"eqpId\":\"SIM-DM5\",\"eqpName\":\"SIM-DM5#\"},\n" +
                    "{\"eqpId\":\"SIM-DM6\",\"eqpName\":\"SIM-DM6#\"},\n" +
                    "{\"eqpId\":\"SIM-DM7\",\"eqpName\":\"SIM-DM7#\"},\n" +
                    "{\"eqpId\":\"SIM-REFLOW1\",\"eqpName\":\"SIM-回流焊1#\"},\n" +
                    "{\"eqpId\":\"SIM-HGAZO1\",\"eqpName\":\"SIM-回流焊后画像1#\"},\n" +
                    "{\"eqpId\":\"SIM-WB-1A\",\"eqpName\":\"SIM-WB焊线机1A\"},\n" +
                    "{\"eqpId\":\"SIM-WB-1B\",\"eqpName\":\"SIM-WB焊线机1B\"},\n" +
                    "{\"eqpId\":\"SIM-WB-2A\",\"eqpName\":\"SIM-WB焊线机2A\"},\n" +
                    "{\"eqpId\":\"SIM-WB-2B\",\"eqpName\":\"SIM-WB焊线机2B\"},\n" +
                    "{\"eqpId\":\"SIM-WB-3A\",\"eqpName\":\"SIM-WB焊线机3A\"},\n" +
                    "{\"eqpId\":\"SIM-WB-3B\",\"eqpName\":\"SIM-WB焊线机3B\"},\n" +
                    "{\"eqpId\":\"SIM-WB-4A\",\"eqpName\":\"SIM-WB焊线机4A\"},\n" +
                    "{\"eqpId\":\"SIM-WB-4B\",\"eqpName\":\"SIM-WB焊线机4B\"},\n" +
                    "{\"eqpId\":\"SIM-WB-5A\",\"eqpName\":\"SIM-WB焊线机5A\"},\n" +
                    "{\"eqpId\":\"SIM-WB-5B\",\"eqpName\":\"SIM-WB焊线机5B\"},\n" +
                    "{\"eqpId\":\"SIM-WB-6A\",\"eqpName\":\"SIM-WB焊线机6A\"},\n" +
                    "{\"eqpId\":\"SIM-WB-6B\",\"eqpName\":\"SIM-WB焊线机6B\"},\n" +
                    "{\"eqpId\":\"SIM-TRM1\",\"eqpName\":\"SIM-TRM塑封机1\"},\n" +
                    "{\"eqpId\":\"SIM-TRM2\",\"eqpName\":\"SIM-TRM塑封机2\"},\n" +
                    "{\"eqpId\":\"SIM-LF1\",\"eqpName\":\"SIM-分离1\"},\n" +
                    "{\"eqpId\":\"SIM-HTRT1\",\"eqpName\":\"SIM-检查1\"},\n" +
                    "{\"eqpId\":\"SIM-LF2\",\"eqpName\":\"SIM-分离2\"},\n" +
                    "{\"eqpId\":\"SIM-HTRT2\",\"eqpName\":\"SIM-检查2\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("eqpId", list);
        } else {
            String str = "[{\"eqpId\":\"APJ-IGBT-SMT1\",\"eqpName\":\"APJ-IGBT印刷贴片机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-PRINTER1\",\"eqpName\":\"APJ-IGBT印刷机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-2DAOI1\",\"eqpName\":\"APJ-IGBT印刷 2D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-REFLOW1\",\"eqpName\":\"APJ-IGBT印刷立式固化炉\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-3DAOI1\",\"eqpName\":\"APJ-IGBT印刷-3D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-SORT1\",\"eqpName\":\"APJ-IGBT印刷下料机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-SORT2\",\"eqpName\":\"APJ-IGBT预结合上料机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-DM1\",\"eqpName\":\"APJ-IGBT预结合晶圆贴片机\"},\n" +
                    "{\"eqpId\":\"APJ-IGBT-SORT3\",\"eqpName\":\"APJ-IGBT预结合下料机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-SMT1\",\"eqpName\":\"APJ-IGBT印刷贴片机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-PRINTER1\",\"eqpName\":\"APJ-FRD印刷机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-2DAOI1\",\"eqpName\":\"APJ-FRD印刷 2D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-REFLOW1\",\"eqpName\":\"APJ-FRD印刷立式固化炉\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-3DAOI1\",\"eqpName\":\"APJ-FRD印刷-3D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-SORT1\",\"eqpName\":\"APJ-FRD印刷下料机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-SORT2\",\"eqpName\":\"APJ-FRD预结合上料机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-DM1\",\"eqpName\":\"APJ-FRD预结合晶圆贴片机\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-SORT3\",\"eqpName\":\"APJ-FRD预结合下料机\"},\n" +
                    "{\"eqpId\":\"APJ-HB1-SORT1\",\"eqpName\":\"APJ-一次热压上料机\"},\n" +
                    "{\"eqpId\":\"APJ-HB1-SINTERING1\",\"eqpName\":\"APJ-一次热压机\"},\n" +
                    "{\"eqpId\":\"APJ-HB1-3DAOI1\",\"eqpName\":\"APJ-一次热压3D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-HB1-SORT2\",\"eqpName\":\"APJ-一次热压下料机\"},\n" +
                    "{\"eqpId\":\"APJ-VI1\",\"eqpName\":\"APJ-中间耐压检查一体机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-SORT1\",\"eqpName\":\"APJ-上基板印刷上料机   \"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-PRINTER1\",\"eqpName\":\"APJ-上基板印刷机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-2DAOI1\",\"eqpName\":\"APJ-上基板印刷2D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-REFLOW1\",\"eqpName\":\"APJ-上基板印刷立式固化炉\"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-3DAOI1\",\"eqpName\":\"APJ-上基板3D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-SORT2\",\"eqpName\":\"APJ-上基板印刷下料机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-SORT1\",\"eqpName\":\"APJ-下基板印刷上料机   \"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-PRINTER1\",\"eqpName\":\"APJ-下基板印刷机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-2DAOI1\",\"eqpName\":\"APJ-下基板印刷2D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-REFLOW1\",\"eqpName\":\"APJ-下基板印刷立式固化炉\"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-3DAOI1\",\"eqpName\":\"APJ-下基板3D画像检查机\"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-SORT2\",\"eqpName\":\"APJ-下基板印刷下料机\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-SORT1\",\"eqpName\":\"APJ-二次热压上料机\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-DISPENSING1\",\"eqpName\":\"APJ-二次热压点胶机1\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-DISPENSING2\",\"eqpName\":\"APJ-二次热压点胶机2\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-DISPENSING3\",\"eqpName\":\"APJ-二次热压点胶机3\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-2DAOI1\",\"eqpName\":\"APJ-二次热压2D画像检查机1\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-SMT1\",\"eqpName\":\"APJ-二次热压贴片机1\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-SMT2\",\"eqpName\":\"APJ-二次热压贴片机2\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-2DAOI2\",\"eqpName\":\"APJ-二次热压2D画像检查机2\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-ASSEMBLY1\",\"eqpName\":\"APJ-二次热压组立机\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-SINTERING1\",\"eqpName\":\"APJ-二次热压机\"},\n" +
                    "{\"eqpId\":\"APJ-HB2-SORT2\",\"eqpName\":\"APJ-二次热压下料机\"},\n" +
                    "{\"eqpId\":\"APJ-XRAY1\",\"eqpName\":\"APJ-X射线检查机\"},\n" +
                    "{\"eqpId\":\"APJ-CLEAN-US1\",\"eqpName\":\"APJ-US1洗净机\"},\n" +
                    "{\"eqpId\":\"APJ-CLEAN-JET1\",\"eqpName\":\"APJ-JET洗净机\"},\n" +
                    "{\"eqpId\":\"APJ-TRM1\",\"eqpName\":\"APJ-TRM1\"},\n" +
                    "{\"eqpId\":\"APJ-SAT1\",\"eqpName\":\"APJ-无损检测机\"},\n" +
                    "{\"eqpId\":\"APJ-AT1\",\"eqpName\":\"APJ-耐老化试验搬送机\"},\n" +
                    "{\"eqpId\":\"APJ-LF1\",\"eqpName\":\"APJ-分离检查机\"},\n" +
                    "{\"eqpId\":\"APJ-HTRT1\",\"eqpName\":\"APJ-高温室温检查机\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("eqpId", list);
        }
        return rs;
    }



    private String getTimes(String str){

        String time=str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6);

        return time;
    }
}
