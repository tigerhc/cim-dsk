package com.lmrj.edc.particle.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.particle.entity.ParticleDataBean;
import com.lmrj.edc.particle.service.IEdcParticleService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("edc/edcparticle")
@ViewPrefix("modules/EdcParticle")
@RequiresPathPermission("EdcParticle")
@LogAspectj(title = "edc_particle")
public class EdcParticleController extends BaseCRUDController<ParticleDataBean> {
    @Autowired
    private IEdcParticleService particleService;

    @RequestMapping("echartData")
    public Response echartData(@RequestParam String eqpId, @RequestParam String beginTime, @RequestParam String endTime){
        if(StringUtils.isEmpty(eqpId)||StringUtils.isEmpty(beginTime)||StringUtils.isEmpty(endTime)){
            return Response.error("参数不正确"+eqpId+beginTime+endTime);
        }
        Map<String, Object> param = new HashMap();
        param.put("eqpId", eqpId);
        param.put("beginTime", beginTime);
        param.put("endTime", endTime);
        Response rs = Response.ok();
        rs.put("data", particleService.getEchartData(param));
        return rs;
    }

    @RequestMapping("getParticleEqps")
    public Response getParticleEqps(){
        Response rs = Response.ok();
        rs.putList("eqps", particleService.getParticleEqps());
        return rs;
    }

    @RequestMapping("/export23")
    public Response export23(@RequestParam String eqpId, @RequestParam String beginTime, @RequestParam String endTime){
        if(StringUtils.isEmpty(eqpId)||StringUtils.isEmpty(beginTime)||StringUtils.isEmpty(endTime)){
            return Response.error("参数不正确"+eqpId+beginTime+endTime);
        }
        try {
            String title = "尘埃粒子计数器";
            Response res = Response.ok("导出成功");
            Map<String, Object> param = new HashMap();
            param.put("eqpId", eqpId);
            param.put("beginTime", beginTime);
            param.put("endTime", endTime);
            Response rs = Response.ok();
            Map<String, Object> all  =particleService.getEchartData(param);
            List<Map<String, String>> dataList = new LinkedList<>();
            Map<String, String> data = new HashMap<>() ;
            List date = (List) all.get("date");
            List point3μm = (List) all.get("0.3μm");
            List point5μm = (List) all.get("0.5μm");
            List oneμm = (List) all.get("1μm");
            List threeμm = (List) all.get("3μm");
            List fiveμm = (List) all.get("5μm");
            List tenμm = (List) all.get("10μm");
            List temp = (List) all.get("温度");
            List wet = (List) all.get("湿度");
            List flow = (List) all.get("流量");
            List speed = (List) all.get("风速");
            List preasure = (List) all.get("压差");

            for (int i = 0; i <date.size() ; i++) {
                data = new HashMap<String,String>();
                data.put("1",(String) date.get(i));
                data.put("2",String.valueOf(point3μm.get(i)));
                data.put("3",String.valueOf(point5μm.get(i)));
                data.put("4",String.valueOf(oneμm.get(i)));
                data.put("5",String.valueOf(threeμm.get(i)));
                data.put("6",String.valueOf(fiveμm.get(i)));
                data.put("7",String.valueOf(tenμm.get(i)));
                data.put("8",String.valueOf(temp.get(i)));
                data.put("9",String.valueOf(wet.get(i)));
                data.put("10",String.valueOf(flow.get(i)));
                data.put("11",String.valueOf(speed.get(i)));
                data.put("12",String.valueOf(preasure.get(i)));
                dataList.add(data);
            }

            List<ExcelExportEntity> keyList= new LinkedList<>();
            ExcelExportEntity key = new ExcelExportEntity("日期","1");
            keyList.add(key);
            ExcelExportEntity key2 = new ExcelExportEntity("0.3μm","2");
            keyList.add(key2);
            ExcelExportEntity key3 = new ExcelExportEntity("0.5μm","3");
            keyList.add(key3);
            ExcelExportEntity key4 = new ExcelExportEntity("1μm","4");
            keyList.add(key4);
            ExcelExportEntity key5 = new ExcelExportEntity("3μm","5");
            keyList.add(key5);
            ExcelExportEntity key6 = new ExcelExportEntity("5μm","6");
            keyList.add(key6);
            ExcelExportEntity key7 = new ExcelExportEntity("10μm","7");
            keyList.add(key7);
            ExcelExportEntity key8 = new ExcelExportEntity("温度","8");
            keyList.add(key8);
            ExcelExportEntity key9 = new ExcelExportEntity("湿度","9");
            keyList.add(key9);
            ExcelExportEntity key10 = new ExcelExportEntity("流量","10");
            keyList.add(key10);
            ExcelExportEntity key11 = new ExcelExportEntity("风速","11");
            keyList.add(key11);
            ExcelExportEntity key12 = new ExcelExportEntity("压差","12");
            keyList.add(key12);

            //获得eqpId 对应的中文
            List<Map<String, Object>> particleEqp = particleService.getParticleEqps();
            String eqpName = eqpId;
            for(Map<String, Object> item : particleEqp){
                if(eqpId.equals(MapUtil.getString(item, "eqpId"))){
                    eqpName = MapUtil.getString(item, "eqpName");
                }
            }
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams("尘埃粒子计数器" + eqpName,"量测详细信息"),keyList,dataList);
            FileOutputStream fos = new FileOutputStream("D:/ExcelExportForMap.xls");
            book.write(fos);
            fos.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            title = title + "-" + DateUtil.getDateTime();
            res.put("bytes", bytesRes);
            res.put("title", title);
            return res;
        } catch (Exception var16) {
            var16.printStackTrace();
            return Response.error(999998, "导出失败");
        }
    }
}
