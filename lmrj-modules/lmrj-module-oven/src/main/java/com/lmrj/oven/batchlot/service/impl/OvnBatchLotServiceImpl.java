package com.lmrj.oven.batchlot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FtpUtil;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.mapper.OvnBatchLotMapper;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service.impl
* @title: ovn_batch_lot服务实现
* @description: ovn_batch_lot服务实现
* @author: zhangweijiang
* @date: 2019-06-09 08:49:15
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
@Slf4j
@Transactional
@Service("ovnbatchlotService")
public class OvnBatchLotServiceImpl  extends CommonServiceImpl<OvnBatchLotMapper,OvnBatchLot> implements IOvnBatchLotService {
    @Autowired
    IOvnBatchLotParamService ovnBatchLotParamService;
    String[] FTP94 = {"10.11.100.40", "21", "cim", "Pp123!@#"};

    @Override
    public OvnBatchLot selectById(Serializable id) {
        OvnBatchLot ovnBatchLot = super.selectById(id);
        EntityWrapper<OvnBatchLotParam> entityWrapper = new EntityWrapper<OvnBatchLotParam>();
        entityWrapper.orderBy("createDate",true);
        ovnBatchLot.setOvnBatchLotParamList(ovnBatchLotParamService.selectList(entityWrapper.eq("BATCH_ID", ovnBatchLot.getId())));
        return ovnBatchLot;
    }

    //@RabbitHandler
    //@RabbitListener(queues= {"S2C.T.CURE.COMMAND"})
    //public void getMsg(String msg){
    //    System.out.println("接收到的消息"+msg);
    //}

    @Override
    public List<FabEquipmentOvenStatus> selectFabStatus(String s) {
        return baseMapper.selectFabStatus(s);
    }

    @Override
    public List<Map> selectFabStatusParam( List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList) {
        return baseMapper.selectFabStatusParam( fabEquipmentOvenStatusList);
    }

    @Override
    public List<Map> selectChart(String s) {
        return baseMapper.selectChartByCase(s);
    }

    @Override
    public boolean insert(OvnBatchLot ovnBatchLot) {
        // 保存主表
        super.insert(ovnBatchLot);
        // 保存细表
        List<OvnBatchLotParam> ovnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
        for (OvnBatchLotParam ovnBatchLotParam : ovnBatchLotParamList) {
            ovnBatchLotParam.setBatchId(ovnBatchLot.getId());
        }
        ovnBatchLotParamService.insertBatch(ovnBatchLotParamList);
        return true;
    }

    @Override
    public boolean resolveTemperatureFile(String eqptId, String fileName) throws Exception {
        //解析文件log.info("第一步{}开始解析{}", eqptId, fileName);
        acquireFile(eqptId, fileName);

        //移动温度曲线文件至备份文件夹
        log.info("第二步{}备份文件{}", eqptId, fileName);
        boolean copyFlag = FtpUtil.copyFile(FTP94, "/oven/"+eqptId,fileName,"/oven/goodback/"+ DateUtil.getDate("yyyyMM")+"/"+eqptId,fileName);
        //删除文件
        boolean delFlag = false;
        if(copyFlag){
            log.info("第三步{}删除文件{}", eqptId, fileName);
            delFlag = FtpUtil.deleteFile(FTP94, "/oven/"+eqptId+"/"+fileName);
        }
        return delFlag;
    }

    @Override
    public OvnBatchLot acquireFile(String eqptId, String fileName) throws Exception {
        OvnBatchLot ovnBatchLot = new OvnBatchLot();
        ovnBatchLot.setOfficeId("21100019");
        // TODO: 2019/6/24 判断文件是否存在
        FTPClient ftpClient = FtpUtil.connectFtp(FTP94);
        Boolean fileExistFlag = FtpUtil.checkFileExist(ftpClient,"/oven/"+eqptId,fileName);
        if(!fileExistFlag){
            try {
                // TODO: 2019/6/24 打印日志
                //this.sendMail(eqptId,lotNo,"文件不存在",fileName+"文件不存在");
                //imMesCallService.save(eventId, "1", eqptId, "CureFtpFileAnalysis", "处理结束,文件不存在", lotNo);
                return null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // TODO: 2019/6/24 解析文件
        log.info("----acquireFile----eqpId:"+eqptId+";fileName:"+fileName+";连接ftp服务器,获取输入流;");
        // TODO: 2019/10/16 判断是否为linux服务器
        FtpUtil.downloadFile(ftpClient,"/oven/"+eqptId,fileName,"C:/tmp/file/cure/");
        InputStream in = new FileInputStream("C://tmp/file/cure/" + fileName);
        log.info("----acquireFile----eqpId:"+eqptId+";fileName:"+fileName+";将流整体读取;");
        BufferedReader br = new BufferedReader(new InputStreamReader(in)); //将流整体读取。


        String str;
        List<OvnBatchLotParam> ovnBatchLotParamList = new ArrayList<OvnBatchLotParam>();
        while ((str = br.readLine()) != null) {
            log.info("csv:"+str);
            if (str.length() == 0) {
                continue;
            }
            if(str.indexOf("vice,") >= 0){
                ovnBatchLot.setEqpId(str.split(",")[1]);
                continue;
            }
            if (str.indexOf("Lot,") >= 0) {
                ovnBatchLot.setLotId(str.replace("Lot,",""));
                continue;
            }
            if(str.indexOf("Recipe,") >= 0){
                ovnBatchLot.setRecipeCode(str.replace("Recipe,",""));
                continue;
            }

            String tempArray[] = str.split(",");
            if (tempArray.length < 5) {
                log.info("数据缺失异常");
                throw new RuntimeException("数据缺失异常");
            }
            //if (Double.valueOf(tempArray[2]) == 0) { //第四行温度为0，不处理
            //    continue;
            //}
            OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
            ovnBatchLotParam.setCreateDate(DateUtil.parseDate(tempArray[0], "yyyy-MM-dd HH:mm:ss"));
            //为edcBatchTemDtl设置参数
            //ovnBatchLotParam.preInsert();
            ovnBatchLotParam.setTempPv(tempArray[1]);
            ovnBatchLotParam.setStep(Short.parseShort(tempArray[2]));
            ovnBatchLotParam.setTempMax(tempArray[3]);
            ovnBatchLotParam.setTempMin(tempArray[4]);
            ovnBatchLotParam.setTempSp(tempArray[5]);
            ovnBatchLotParamList.add(ovnBatchLotParam);
        }
        if(ovnBatchLotParamList.size()<=5){
            log.info("数据记录不足");
            throw new RuntimeException("数据记录不足，在五条以下");
        }
        ovnBatchLot.setOvnBatchLotParamList(ovnBatchLotParamList);
        log.info("----acquireFile----eqpId:"+eqptId+";fileName:"+fileName+";读取完成;");

        //判断温度曲线是否正常
        ovnBatchLot.setCheckFlag("Y");
        for(OvnBatchLotParam temp :ovnBatchLotParamList){
            Double pv = Double.parseDouble(temp.getTempPv());
            Double max = Double.parseDouble(temp.getTempMax());
            Double min = Double.parseDouble(temp.getTempMin());
            if( pv>max|| pv<min){
                log.info("----acquireFile----eqpId:"+eqptId+";fileName:"+fileName+";温度曲线异常;");
                ovnBatchLot.setCheckFlag("N");
                ovnBatchLot.setCheckResult("温度超标：温度("+pv+"),上限温度("+max+"),下限温度("+min+")");
                break;
            }
        }

        // TODO: 2019/6/24 插入数据库中
        List<OvnBatchLotParam> lll = ovnBatchLot.getOvnBatchLotParamList();
        ovnBatchLot.setStartTime(lll.get(0).getCreateDate());
        ovnBatchLot.setEndTime(lll.get(lll.size()-1).getCreateDate());
        ovnBatchLot.setCreateBy("1");
        ovnBatchLot.setCreateDate(new Date());
        for(OvnBatchLotParam temp: ovnBatchLot.getOvnBatchLotParamList()){
            temp.setCreateBy("1");
        }
        this.insert(ovnBatchLot);
        return ovnBatchLot;
    }

    public void resolveAllTempFile(String eqpId){
        FTPFile[] ftpFiles = FtpUtil.listFile(FTP94,"/oven/"+eqpId+"/");
        for(FTPFile ftpFile: ftpFiles){
            if(ftpFile.getName().length()<14){
                continue;
            }
            try {
                log.info("/oven/"+eqpId+"/"+ ftpFile.getName());
                resolveTemperatureFile(eqpId, ftpFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ftpFile.getName();
        }
    }

    @Override
    public List<Map> findDetailBytime(String beginTime, String endTime,String eqpId) {
        int count = baseMapper.findCountBytime(eqpId,  beginTime,  endTime);
        if(count>100000){
            return null;
        }else{
            List<Map> detail =  baseMapper.findDetailBytime(eqpId,  beginTime,  endTime);
            long timeFlag = 0;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date begin = format.parse(beginTime);
                Date end = format.parse(endTime);
                 timeFlag = end.getTime()-begin.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (timeFlag > 24*3600*1000*7){
            List<Map> result =new ArrayList<>();
            Map<String,Object> temp = new HashMap<>();
            BigDecimal pv = new BigDecimal(0);
            String resultArr = (String) detail.get(0).get("other_temps_value");
            String[] strResultArr = resultArr.split(",");
            Double[] Other = new Double[strResultArr.length];
            for (int j = 0; j < strResultArr.length ; j++) {
                Other[j]=(Double.valueOf(strResultArr[j]));
            }
            for (int i = 0; i <detail.size() ; i++) {
                if((i!=0&&i%60==0)||(i==(detail.size()-1))){
                    for (int j = 0; j <Other.length ; j++) {
                        if (j%4 == 0){
                            BigDecimal first = BigDecimal.valueOf(Other[j]);
                            BigDecimal multiply = new BigDecimal(60);
                            Other[j] =Double.valueOf(first.divide(multiply,2, RoundingMode.HALF_UP).toString());
                        }
                    }
                    Map<String,Object> element = new HashMap<>();
                    String[] stringArr = new String[Other.length];
                    for (int j = 0; j <Other.length ; j++) {
                        stringArr[j] = Other[j].toString();
                    }
                    BigDecimal multiply = new BigDecimal(60);
                    element.put("temp_pv",Double.valueOf(pv.divide(multiply,2,RoundingMode.HALF_UP).toString()));
                    element.put("temp_min",detail.get(i).get("temp_min"));
                    element.put("temp_sp",detail.get(i).get("temp_sp"));
                    element.put("temp_max",detail.get(i).get("temp_max"));
                    element.put("create_date",detail.get(i).get("create_date"));
                    element.put("other_temps_value", StringUtil.join(stringArr,","));
                    result.add(element);
                    pv = new BigDecimal(0);
                    String str = (String) detail.get(i).get("other_temps_value");
                    String[] strArr = str.split(",");
                    Double[] doubleArr = new Double[strArr.length];
                    for (int j = 0; j <strArr.length ; j++) {
                        doubleArr[j]=(Double.valueOf(strArr[j]));
                    }
                    Other = doubleArr;
                    continue;
                }
                String convert = String.valueOf(detail.get(i).get("temp_pv"));
                BigDecimal pv_temp = new BigDecimal(convert);
                pv = pv_temp.add(pv);
                String str = (String) detail.get(i).get("other_temps_value");
                String[] strArr = str.split(",");
                Double[] doubleArr = new Double[strArr.length];
                for (int j = 0; j <strArr.length ; j++) {
                    doubleArr[j]=(Double.valueOf(strArr[j]));
                }
                for (int j = 0; j <doubleArr.length ; j++) {
                    if (j%4 == 0){
                        BigDecimal first = BigDecimal.valueOf(Other[j]);
                        BigDecimal second = BigDecimal.valueOf(doubleArr[j]);
                        Other[j] = Double.valueOf(first.add(second).toString());
                    }
                }
            }
                return result;
            }
            return detail;
        }
    }

    @Override
    public List<Map<String, Object>> findTodayEqpIds() {
        String timeStr = DateUtil.getDate();
        return baseMapper.findToEqpId(timeStr);
    }

    @Override
    public boolean saveTempData(List<Map<String, Object>> eqpList, List<Map<String, Object>> temp) {
        if(eqpList!=null && eqpList.size()>0){
            baseMapper.saveEqp(eqpList);
        }
        if(temp!=null && temp.size()>0){
            baseMapper.saveTempParam(temp);
        }
        return true;
    }
}
