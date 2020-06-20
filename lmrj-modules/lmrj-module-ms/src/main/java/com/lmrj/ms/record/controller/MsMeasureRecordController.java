package com.lmrj.ms.record.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.controller
 * @title: ms_measure_record控制器
 * @description: ms_measure_record控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurerecord")
@ViewPrefix("ms/msmeasurerecord")
@RequiresPathPermission("ms:msmeasurerecord")
@LogAspectj(title = "ms_measure_record")
public class MsMeasureRecordController extends BaseCRUDController<MsMeasureRecord> {

    @Autowired
    IMsMeasureRecordService msMeasureRecordService;
    @RequestMapping(value = "/clientsave", method = { RequestMethod.GET, RequestMethod.POST })
    public Response insert( HttpServletRequest request,
                                  HttpServletResponse response){
        Response res= new DateResponse();
        try {
            String record = request.getReader().readLine();
            MsMeasureRecord msMeasureRecord = JsonUtil.from(record,MsMeasureRecord.class);
            String recordId = StringUtil.randomTimeUUID("MS");
            msMeasureRecord.setRecordId(recordId);
            msMeasureRecord.setApproveResult("Y");
            if(StringUtil.isBlank(msMeasureRecord.getStatus())){
                msMeasureRecord.setStatus("1");
            }
            for (MsMeasureRecordDetail msMeasureRecordDetail : msMeasureRecord.getDetail()) {
                if(msMeasureRecordDetail.getItemResult().contains("N")){
                    msMeasureRecord.setApproveResult("N");
                    break;
                }
            }
            boolean flag = commonService.insert(msMeasureRecord);
            if(flag){
                res = DateResponse.ok(recordId);
                //res.put("record", msMeasureRecord);
                return res;
                //String content = JSON.toJSONString(msMeasureRecord);
                //ServletUtils.printJson(response, content);
            }else{
                return res.error("NG");
                //ServletUtils.printJson(response, "NG");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.error("无法解析");

    }

    @RequestMapping(value = "/rptmsrecordbytime/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public Response rptMsRecordByTime(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                      HttpServletRequest request, HttpServletResponse response){
        List<Map> maps =  msMeasureRecordService.findDetailBytime(beginTime,endTime,eqpId);
        return DateResponse.ok(maps);
    }

    @RequestMapping(value = "/rptmsrecordbytime2/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void rptMsRecordByTime2(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                  HttpServletRequest request, HttpServletResponse response){
        List<Map> maps =  msMeasureRecordService.findDetailBytime(beginTime,endTime,eqpId);
        String content = JSON.toJSONStringWithDateFormat(maps, JSON.DEFFAULT_DATE_FORMAT);
        ServletUtils.printJson(response, content);
    }
}
