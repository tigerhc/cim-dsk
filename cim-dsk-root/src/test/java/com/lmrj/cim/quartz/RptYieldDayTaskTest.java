package com.lmrj.cim.quartz;

import com.google.common.collect.Lists;
import com.lmrj.cim.CimBootApplication;
import com.lmrj.core.email.entity.EmailSendLog;
import com.lmrj.core.email.service.IEmailSendLogService;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionDefective;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionDefectiveService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.edc.state.service.impl.EdcEqpStateServiceImpl;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import com.lmrj.mes.lot.service.IMesLotWipService;
import com.lmrj.mes.track.entity.MesLotMaterial;
import com.lmrj.mes.track.entity.MesLotMaterialInfo;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotMaterialInfoService;
import com.lmrj.mes.track.service.IMesLotMaterialService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.oven.batchlot.task.OvenDayTask;
import com.lmrj.util.FileUtils;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest(classes = CimBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)

public class RptYieldDayTaskTest {
    @Autowired
    RptYieldDayTask rptYieldDayTask;
    @Autowired
    RptYieldTask rptYieldTask;
    @Autowired
    EqpStateTask eqpStateTask;
    @Autowired
    OperationYieldTask operationYieldTask;
    @Autowired
    EdcAmsRecordYieldTask edcAmsRecordYieldTask;
    @Autowired
    ApsPlanImportTask apsPlanImportTask;
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    IEdcDskLogProductionDefectiveService iEdcDskLogProductionDefectiveService;
    @Autowired
    ImageMoveTask imageMoveTask;
    @Autowired
    TrmCsvTask trmCsvTask;
    @Autowired
    IMsMeasureKongdongService iMsMeasureKongdongService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    private IEmailSendService emailSendService;
    @Autowired
    IEmailSendLogService iEmailSendLogService;
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    @Autowired
    private IEdcEqpStateService edcEqpStateService;
    @Autowired
    OvenDayTask ovenDayTask;
    @Autowired
    OvnBatchLotDayTask ovnBatchLotDayTask;
    @Autowired
    IMesLotMaterialService mesLotMaterialService;
    @Autowired
    IMesLotMaterialInfoService mesLotMaterialInfoService;
    @Autowired
    private IOvnBatchLotService ovnBatchLotService;
    @Autowired
    private IOvnBatchLotDayService ovnBatchLotDayService;
    private Boolean isRun = Boolean.FALSE;
    @Autowired
    private IMesLotWipService iMesLotWipService;
    @Autowired
    EdcEqpStateServiceImpl edcEqpStateServiceImpl;
    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;


    
    @Test
    public void eapStateFix(){
        for (int i = 1; i <30 ; i++) {
            Date startTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH, -i);
            startTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date endTime = cal.getTime();
            dataSupplement(startTime,endTime);
        }

    }

    public void dataSupplement(Date startTime ,Date endTime) {
        
        List<String> eqpIdList = new ArrayList<>();
        eqpIdList = iFabEquipmentService.findEqpIdList();
        List<RptEqpStateDay> rptEqpStateDayList = Lists.newArrayList();
        for (String eqpId : eqpIdList) {
            List<EdcEqpState> eqpStateList = edcEqpStateService.getAllByTime(startTime, endTime, eqpId);
            if (eqpStateList.size() == 0) {
                RptEqpStateDay rptEqpStateDay = new RptEqpStateDay();
                rptEqpStateDay.setEqpId(eqpId);
                rptEqpStateDay.setPeriodDate(DateUtil.formatDate(startTime, "yyyyMMdd"));
                Boolean flag = false;
                List<MesLotTrack> lotList = iMesLotTrackService.findCorrectData(startTime, endTime);
                if (lotList.size() > 0)
                    for (MesLotTrack mesLotTrack : lotList) {
                        if (mesLotTrack.getEqpId().equals(eqpId)) {
                            flag = true;
                        }
                    }
                if (flag) {
                    Double run = 24 * 60 * 60 * 1000 * 0.001;
                    rptEqpStateDay.setRunTime(run);
                    rptEqpStateDay.setIdleTime(0.0);
                } else {
                    Double idle = 24 * 60 * 60 * 1000 * 0.001;
                    rptEqpStateDay.setRunTime(0.0);
                    rptEqpStateDay.setIdleTime(idle);
                }
                rptEqpStateDay.setPmTime(0.0);
                rptEqpStateDay.setAlarmTime(0.0);
                rptEqpStateDayList.add(rptEqpStateDay);
            }
        }
        if (rptEqpStateDayList.size() > 0) {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList, 50);
        }

    }


    @Test
    public void test111222() {
        try {
            if (!isRun) {
                isRun = Boolean.TRUE;
                try {
                    //获取当前的时间的前一天并转为YYYY-MM-DD

                    Calendar ca = Calendar.getInstance();
                    ca.setTime(new Date());
                    ca.add(Calendar.DATE, -1);
                    Date lastDay = ca.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String periodDate = formatter.format(lastDay);
                    Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodDate + " 00:00:00");
                    Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodDate + " 23:59:59");
                    //获取当前设备列表并逐一写入
                    List<String> eqpList = ovnBatchLotService.lastDayEqpList(startTime, endTime);
                    for (String eqpId : eqpList) {
                        ovnBatchLotDayService.fParamToDay(eqpId, periodDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isRun = Boolean.FALSE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wlTest() {
        String materialInfo = "纳米银奖,2222A_纳米银奖,2222B";
        String eqpId1 = "TEST";
        String lotNo = "1111A";
        String opId = "1234";
        List<MesLotMaterialInfo> materialInfosList = new ArrayList<>();
        try {
            //将物料信息存入数据库
            String materialInfos[] = materialInfo.split("_");
            MesLotMaterial mesLotMaterial = mesLotMaterialService.selectMaterialData(eqpId1, lotNo);
            if (mesLotMaterial == null || "".equals(mesLotMaterial.getEqpId()) || mesLotMaterial.getEqpId() == null) {
                mesLotMaterial = new MesLotMaterial();
                mesLotMaterial.setEqpId(eqpId1);
                mesLotMaterial.setLotNo(lotNo);
                mesLotMaterial.setCreateBy(opId);
                mesLotMaterial.setCreateDate(new Date());
                mesLotMaterialService.insert(mesLotMaterial);
            }
            materialInfosList = new ArrayList<>();
            for (String info : materialInfos) {
                String materialName = info.split(",")[0];
                String materiallotNo = info.split(",")[1];
                MesLotMaterialInfo mesLotMaterialInfo = new MesLotMaterialInfo();
                mesLotMaterialInfo.setMaterialId(mesLotMaterial.getId());
                mesLotMaterialInfo.setCreateBy(opId);
                mesLotMaterialInfo.setMaterialName(materialName);
                mesLotMaterialInfo.setLotNo(materiallotNo);
                mesLotMaterialInfo.setCreateDate(new Date());
                materialInfosList.add(mesLotMaterialInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!mesLotMaterialInfoService.insertBatch(materialInfosList, 10)) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test() throws Exception {
        Date startTime = new Date();
        Date endTime = new Date();
        String hour = DateUtil.getDate("HH");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (Integer.parseInt(hour) < 8) {
            endTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            startTime = cal.getTime();
        } else {
            startTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endTime = cal.getTime();
        }
        System.out.println(DateUtil.formatTime(startTime));
        System.out.println(DateUtil.formatTime(endTime));
        int lotYieldAll = 0;
        lotYieldAll = iMesLotWipService.findDayLotYield("SIM-HTRT1", startTime, endTime);
        lotYieldAll = lotYieldAll + iMesLotWipService.findDayLotYield("SIM-HTRT2", startTime, endTime);
        System.out.println(lotYieldAll);
    }


    @Test
    public void dmStateData() {
        Date endTime = new Date();
        Date startTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        endTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -32);
        startTime = cal.getTime();
        List<EdcDskLogOperation> list = edcDskLogOperationService.findDataByTimeAndEqpId("SIM-DM2", startTime, endTime);
        for (EdcDskLogOperation edcDskLogOperation : list) {
            EdcEqpState edcEqpState = new EdcEqpState();
            edcEqpState.setEqpId(edcDskLogOperation.getEqpId());
            edcEqpState.setStartTime(edcDskLogOperation.getStartTime());
            String eventId = edcDskLogOperation.getEventId();
            String status = "";
            if ("0".equals(eventId) || "7".equals(eventId)) {
                status = "DOWN";
            } else if ("1".equals(eventId) || "6".equals(eventId)) {
                status = "RUN";
            } else if ("3".equals(eventId)) {
                status = "IDLE";
            } else if ("2".equals(eventId)) {
                status = "ALARM";
            }
            edcEqpState.setState(status);
            //发送状态数据到设备状态队列
            if (StringUtil.isNotBlank(status)) {
                String stateJson = JsonUtil.toJsonString(edcEqpState);
                rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
            }
        }
    }

    @Test
    public void eqpStateFix() {
        Date endTime = new Date();
        for (int i = 8; i >= 0; i--) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH, -i);
            endTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date startTime = cal.getTime();
            eqpStateTask.fixeqpState(startTime, endTime);
        }
        for (int i = 1; i <10 ; i++) {
            Date startTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH, -i);
            startTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date endTime1 = cal.getTime();
            dataSupplement(startTime,endTime1);
        }

    }


    @Test
    public void lotYieldfix() {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        for (int i = 20; i >= 0; i--) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            endTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date startTime = cal.getTime();
            eqpStateTask.fixeqpState(startTime, endTime);
        }
    }


    @Test
    public void mysqlTest() {
        String msg = "{\"eqpId\":\"SIM-YGAZO1\",\"paramValue\":\"Y,9,0,+14609.000,11000,167000,0,+18479.000,13500,23000,0,+17339.000,13500,23000,0,+17999.000,15850,21800,0,+16960.000,14050,21200,0,+17393.000,14550,20700,0,+16709.000,15850,20700,0,+22691.000,18500,26850,0,+3851.000,2700,4800,0,+3824.000,2700,4750,0,+3825.000,2800,4750,0,+0.374,0.300,0.500,0,+0.381,0.300,0.500,0,+0.382,0.300,0.500,0,+0.386,0.300,0.500,0,+0.367,0.300,0.500,0,+0.339,0.300,0.500,0,+0.386,0.300,0.500,0,+0.367,0.300,0.500,0,+0.391,0.300,0.500,0,+0.381,0.300,0.500,0,+0.468,0.400,0.600,0,+0.381,0.300,0.500,0,+0.394,0.300,0.500,0,+0.394,0.300,0.500,0,+0.392,0.300,0.500,0,+0.403,0.300,0.500,0,+0.381,0.300,0.500,0,+0.383,0.300,0.500\",\"recipeCode\":\"SX68123\",\"dayYield\":14172,\"lotYield\":0,\"materialLotNo\":\" \",\"materialModel\":\" \",\"materialNo\":\" \",\"materialNo2\":\" \",\"lotNo\":\"1112D\",\"productionNo\":\"5002915\",\"orderNo\":\"\",\"judgeResult\":\"Y\",\"startTime\":\"2021-01-12 12:46:09 180\",\"endTime\":\"2021-01-12 12:45:28 000\",\"duration\":10.0}";
        EdcDskLogProduction edcDskLogProduction = JsonUtil.from(msg, EdcDskLogProduction.class);
        Date startTime = edcDskLogProduction.getStartTime();
        Date endTime = edcDskLogProduction.getEndTime();
        edcDskLogProduction.setStartTime(null);
        edcDskLogProduction.setEndTime(null);
        //edcDskLogProductionService.insert(edcDskLogProduction);
        JSONObject json = JSONObject.fromObject(edcDskLogProduction);
        System.out.println(json.toString());
        EdcDskLogProductionDefective defectivePro = JsonUtil.from(json.toString(), EdcDskLogProductionDefective.class);
        defectivePro.setEndTime(endTime);
        defectivePro.setId(null);
        iEdcDskLogProductionDefectiveService.insert(defectivePro);
    }

    @Test
    public void test111() {
        List<String> pathArr = new ArrayList<>();
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6821MX(P)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6822MX(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6822MX(S)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6823MX(N)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6860MZ(B)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6860MZ(P)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA\\SMA6863MH(N)B4D");
        for (int i = 0; i < pathArr.size(); i++) {
            List<Map<String, Object>> fileNames = FileUtils.getFileInfos(pathArr.get(i), "", "");
            if (fileNames.size() > 0) {
                List<MsMeasureKongdong> list = new ArrayList<>();
                for (Map<String, Object> item : fileNames) {
                    _getDataByFile(list, MapUtils.getString(item, "fileName"), MapUtils.getString(item, "createTime"), pathArr.get(i));
                }
                if (list.size() > 0) {
                    iMsMeasureKongdongService.insertBatch(list, 1000);
                }
            }
        }
    }

    private void _getDataByFile(List<MsMeasureKongdong> list, String fileName, String createDate, String filePath) {
        if (_chkFileNameLine(filePath + "\\" + fileName)) {
            String productionNo = filePath.substring(filePath.lastIndexOf("\\") + 1);
            //filePath = filePath.substring(0,filePath.lastIndexOf("\\2020年"));
            String lineNo = "SMA";//filePath.substring(filePath.lastIndexOf("\\")+1);0D03A 2.7%-MOS-3-4.bmp
            String lotNo = fileName.substring(0, fileName.indexOf(" "));//0927A2.3%-DI-1.jpg,filePathD:\DSK1\IT化データ（一課）\X線データ\データ処理\ボイド率\SIM\2020年\10月\SIM6812M(E)D-URA F2971
            String voidRatio = fileName.substring(fileName.indexOf(" ") + 1, fileName.indexOf("%"));
            String type = fileName.substring(fileName.indexOf("%") + 2, fileName.indexOf(".bmp"));
            MsMeasureKongdong data = new MsMeasureKongdong();
            data.setLotNo(lotNo);
            data.setVoidRatio(Double.parseDouble(voidRatio));
            data.setLineNo(lineNo);
            data.setProductionName("J." + productionNo);
            data.setType(type);
            data.setCreateDate(DateUtil.parseDate(createDate));
            list.add(data);
        }
    }


    private boolean _chkFileNameLine(String fileName) {
        if (!fileName.contains("5GI") && !fileName.contains("6GI")) {
            if (fileName.indexOf(" ") <= 0) {
                return false;
            }
            if (fileName.indexOf("%") <= 0) {
                return false;
            }
            if (fileName.indexOf("-") <= 0) {
                return false;
            }
            return true;
        } else {
            if (fileName.indexOf("%") <= 0) {
                return false;
            } else {
                return true;
            }
        }
    }


    @Test
    public void kongdongdatafix() {
        List<String> list1 = new ArrayList<>();
        // lot_no,production_name,type,create_date,value
        String line = "SMA";
        list1.add("1118L,J.SX68123M (AU)D-RP,MOS-5,2021-01-19 13:11:57.422000,5.3,SX");
        list1.add("1118L,J.SX68123M (AU)D-RP,MOS-5,2021-01-19 13:11:57.422000,3.1,SX");
        List<MsMeasureKongdong> measureKongdongList = new ArrayList<>();
        for (String s : list1) {
            String lotNo = s.split(",")[0];
            String productionName = s.split(",")[1];
            String type = s.split(",")[2];
            String date = s.split(",")[3];
            Date creatDate = null;
            try {
                creatDate = DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:ss");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String value = s.split(",")[4];
            Double values = Double.parseDouble(value);
            MsMeasureKongdong measureKongdong = new MsMeasureKongdong();
            measureKongdong.setLineNo(line);
            measureKongdong.setProductionNo(productionName);
            measureKongdong.setLotNo(lotNo);
            measureKongdong.setCreateDate(creatDate);
            measureKongdong.setVoidRatio(values);
            measureKongdong.setType(type);
            measureKongdongList.add(measureKongdong);
        }
        iMsMeasureKongdongService.insertBatch(measureKongdongList, 20);
    }


    @Test
    public void trmTest() {
        trmCsvTask.trmProductionCsv();
    }


    @Test
    public void fixdata() {
        //lotno,productionNo,productionName,eqpId,createDate,itemValue,createdate2,rowName
        List<String> stringList = new ArrayList<>();
        /*stringList.add("1110C,5002435,J.6GI-2870(G)D-AP,SIM-WT2,2021-01-10 07:21:31.681000,46.2,2021-01-10 07:21:31.681000");
        stringList.add("1110C,5002435,J.6GI-2870(G)D-AP,SIM-WT2,2021-01-10 15:24:38.493000,11.2,2021-01-10 15:24:38.493000");
        stringList.add("1110C,5002435,J.6GI-2870(G)D-AP,SIM-WT2,2021-01-11 17:26:00.600000,44.7,2021-01-11 17:26:00.600000");*/
        stringList.add("1108A,5509157,J.5GI-2860B(G)D-AP,SIM-WT2,2021-01-08 15:19:00.000000,44.2,2021-01-08 15:19:00.000000,2");
        stringList.add("1125D,5509157,J.5GI-2860B(G)D-AP,SIM-WT2,2021-01-25 17:54:00.000000,44.2,2021-01-25 17:54:00.000000,2");
        MsMeasureRecord msMeasureRecord = new MsMeasureRecord();
    }

    private List<File> getAllFile() {
        List<File> list = new ArrayList<>();
        File baseFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹");
        //校验路径是否存在
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                list.add(file);
            }
        }
        return list;
    }

    @Test
    public void fileFind() {
        List<String> fileNames = new ArrayList<>();
        List<String> errorFileNameList = new ArrayList<>();
        List<File> fileList = getAllFile();
        System.out.println("             " + fileList.size());
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o2.lastModified() < o1.lastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        for (File allFile : fileList) {
            List<String> lines = new ArrayList<>();
            try {
                lines = FileUtil.readLines(allFile, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (lines.size() == 14) {
                if (lines.get(13).contains("5001846")) {
                    System.out.println(lines.get(13) + "  FielName: " + allFile.getName());
                }
            } else {
                errorFileNameList.add(allFile.getName());
            }
        }
        System.out.println("errorFileNameList:");
        for (String fileName : errorFileNameList) {
            System.out.println(fileName);
        }
    }


    @Test
    public void kongdongfix() {
        List<String> lines = new ArrayList<>();
        //productionName,val,createDate
        lines.add(",,,,,5.3");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
    }


    @Test
    public void emailSendTest() {
        String[] emails = {"1518798637@qq.com", "472109366@qq.com"};
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("EQP_ID", "TEST");
        msgMap.put("ALARM_CODE", "RTP_ALARM");
        //emailSendService.blockSend(emails,"RTP_ALARM",msgMap);
    }

    @Test
    public void emailSendLog() {
        EmailSendLog emailSendLog = iEmailSendLogService.selectEmailLog("RTP_ALARM");
        System.out.println(emailSendLog);
    }


    @Test
    public void edcAmsRecoedFix() {
        List<File> operationFileList = new ArrayList<>();
        operationFileList = (List<File>) FileUtil.listFiles(new File("C:\\Users\\86187_dar6n7g\\Desktop\\test\\"), new String[]{"csv"}, false);
        if (operationFileList.size() > 0) {
            Collections.sort(operationFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o2.lastModified() < o1.lastModified()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            for (File eqpFile : operationFileList) {
                String eqpId = eqpFile.getName().split("_")[1];
                List<EdcDskLogOperation> operationList = operParse(eqpId, eqpFile, 0, true);
                String logJson = JsonUtil.toJsonString(operationList);
                rabbitTemplate.convertAndSend("C2S.Q.OPERATIONLOG.DATA", logJson);
            }
        }

    }


    public List<EdcDskLogOperation> operParse(String eqpId, File file, int startLineno, Boolean lastFlag) {
        List<EdcDskLogOperation> operationLogList = Lists.newArrayList();
        String fileContent = "";
        String encoding = "";
        try {
            if (eqpId.contains("CLEAN") || eqpId.contains("SORT") || eqpId.contains("2DAOI") || eqpId.contains("TRM") || eqpId.contains("SAT") || eqpId.contains("SMT") || eqpId.contains("HTRT") || eqpId.contains("AT") || eqpId.contains("LF") || eqpId.contains("VI") || eqpId.contains("PRINTER")) {
                encoding = "GBK";
            } else if (eqpId.contains("DM")) {
                encoding = "Shift_JIS";
            } else {
                encoding = "UTF-8";
            }
            fileContent = FileUtil.readFileToString(file, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //为3DAOI设备日志添加标题

        String[] lines = fileContent.split("\\r\\n");
        if (eqpId.contains("DISPENSING")) {
            lines = fileContent.split("\\n");
        }
        //||
        lines[0] = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        for (int lineno = startLineno; lineno < lines.length; lineno++) {
            String line = lines[lineno];
            //}
            //for (String line : lines) {
            if (StringUtil.isBlank(line)) {
                continue;
            }
            EdcDskLogOperation operationLog = new EdcDskLogOperation();
            operationLog.setEqpId(eqpId);
            if (eqpId.contains("VI")) {
                operationLog.setEqpId("APJ-VI1");
            }
            //operationLog.setEqpModelId(fabEquipment.getModelId());
            //operationLog.setEqpModelName(fabEquipment.getModelName());

            String[] columns = line.split(",", -1);
            int columnNo = 3;
            operationLog.setRecipeCode(columns[3]);
            operationLog.setOrderNo(columns[4]);
            operationLog.setLotNo(columns[5]);
            operationLog.setProductionNo(columns[6]);
            if (StringUtil.isNotBlank(columns[7])) {
                operationLog.setDayInput(Integer.parseInt(columns[7].replace(" ", "")));
            }
            if (StringUtil.isNotBlank(columns[8])) {
                operationLog.setLotInput(Integer.parseInt(columns[8].replace(" ", "")));
            }
            if (StringUtil.isNotBlank(columns[9])) {
                operationLog.setDayYield(Integer.parseInt(columns[9].replace(" ", "")));
            }
            if (StringUtil.isNotBlank(columns[10])) {
                operationLog.setLotYield(Integer.parseInt(columns[10].replace(" ", "")));
            }

            try {
                String startDateStr = columns[11];
                if (eqpId.contains("2DAOI")) {
                    startDateStr = startDateStr.substring(0, 21);
                }
                if (eqpId.contains("DM")) {
                    startDateStr = startDateStr.replace("/", "-");
                }
                if (startDateStr.contains("/")) {
                    startDateStr = startDateStr.replace("/", "-");
                }
                if (startDateStr.length() == 19) {
                    startDateStr = startDateStr + ".0";
                }
                String parsePatterns = "yyyy-MM-dd HH:mm:ss.S";
                if (eqpId.contains("DISPENSING")) {
                    parsePatterns = "yyyy-MM-dd HH:mm:ss:SSS";
                }
                Date startDate = DateUtil.parseDate(startDateStr, parsePatterns);
                //只向服务器传输新数据
                /*if(startDate.before(cal.getTime())){
                    continue;
                }*/
                operationLog.setStartTime(startDate);
            } catch (ParseException e) {
                System.out.println("Operation时间处理错误" + e);
                e.printStackTrace();
            }
            operationLog.setEventId(columns[12]);
            operationLog.setAlarmCode(columns[13]);
            operationLog.setEventName(columns[14]);
            operationLog.setEventDetail(columns[15]);
            if (columns.length >= 17) {
                operationLog.setCreateBy(columns[16]);
            }
            operationLogList.add(operationLog);
        }
        if (operationLogList.size() == 0) {
            return null;
        }
        //对APJ-DM事件日志进行特殊处理，把旧数据去除
        return operationLogList;
    }


    @Test
    public void deleteTest() {
        File a = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)");
        File[] fileList = a.listFiles();
        for (File eqpFile : fileList) {
            System.out.println(eqpFile.getName());
        }
    }

    public static void main(String[] args) {
        File a = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)");
        File[] fileList = a.listFiles();
        for (File eqpFile : fileList) {
            System.out.println(eqpFile.getName());
            if (eqpFile.isDirectory()) {
                System.out.println(eqpFile.getName());
                FileUtil.delDir(eqpFile.getPath(), true);
            }
        }
    }

    @Test
    public void stateDataFix() {
        List<String> epqIdList = new ArrayList<>();
        epqIdList = iFabEquipmentService.findEqpIdList();
        Date startTime = new Date();
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        startTime = cal.getTime();
        for (String eqpId : epqIdList) {
            List<EdcEqpState> eqpStateList = edcEqpStateService.getAllByTime(startTime, endTime, eqpId);
            List<EdcEqpState> newEqpStateList = new ArrayList<>();
            for (int i = 0; i < eqpStateList.size() - 1; i++) {
                EdcEqpState edcEqpState = eqpStateList.get(i);
                EdcEqpState nextEdcEqpState = eqpStateList.get(i + 1);
                edcEqpState.setEndTime(nextEdcEqpState.getStartTime());
                Double state = (double) (nextEdcEqpState.getStartTime().getTime() - edcEqpState.getStartTime().getTime());
                edcEqpState.setStateTimes(state);
                newEqpStateList.add(edcEqpState);
            }
            if (newEqpStateList.size() > 0) {
                try {
                    edcEqpStateService.updateBatchById(newEqpStateList, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
