package com.lmrj.edc.param.entity;

import com.google.common.collect.Lists;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by zwj on 2019/6/15.
 */
public class EdcParamRecordTest {

    @Test
    public void tojson() throws Exception {
        EdcParamRecord edcParamRecord = new EdcParamRecord();
        EdcParamRecord edcParamRecord2 = new EdcParamRecord();
        edcParamRecord.setId("111");
        edcParamRecord.setBizDate(new Date());
        edcParamRecord2.setId("111111");
        EdcParamRecordDtl edcParamRecordDtl = new EdcParamRecordDtl();
        edcParamRecordDtl.setId("222");
        edcParamRecordDtl.setRecordId(edcParamRecord2.getId());
        List list = Lists.newArrayList();
        list.add(edcParamRecordDtl);
        edcParamRecord.setEdcParamRecordDtlList(list);
        String content = JsonUtil.toJsonString(edcParamRecord);
        System.out.println(content);


    }

    @Test
    public void from() throws Exception {
        //EdcParamRecord edcParamRecord = new EdcParamRecord();
        //EdcParamRecord edcParamRecord2 = new EdcParamRecord();
        //edcParamRecord.setId("111");
        //edcParamRecord2.setId("111111");
        //EdcParamRecordDtl edcParamRecordDtl = new EdcParamRecordDtl();
        //edcParamRecordDtl.setId("222");
        //edcParamRecordDtl.setRecordId(edcParamRecord);
        //List list = Lists.newArrayList();
        //list.add(edcParamRecordDtl);
        //edcParamRecord.setEdcParamRecordDtlList(list);
        String content ="{\"edcParamRecordDtlList\":[{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"lot\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":null,\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null},{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"recipe\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":null,\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null},{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"vendor\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":\"AAA\",\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null},{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"ptno\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":\"45\",\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null},{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"seggo\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":\"27\",\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null},{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"rtime\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":\"31\",\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null},{\"recordId\":null,\"paramRowId\":null,\"paramId\":null,\"paramCode\":\"Temp\",\"paramName\":null,\"paramShotName\":null,\"paramValue\":\"84\",\"paramUnit\":null,\"protocol\":null,\"protocolSub\":null,\"valueType\":null,\"setValue\":null,\"minValue\":null,\"maxValue\":null}],\"eqpId\":\"D3500-0001\",\"bizDate\":\"2019-06-15 15:21:15\",\"bizType\":null,\"bizSubType\":null}";
        EdcParamRecord edcParamRecord = JsonUtil.from(content,EdcParamRecord.class);
        System.out.println(edcParamRecord);


    }

}