package com.lmrj.cim.quartz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.lmrj.cim.CimBootApplication;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionDefective;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionDefectiveService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.amsrpt.utils.RepeatAlarmUtil;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.edc.param.service.impl.EdcEqpLogParamServiceImpl;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class FileParseTest {
    static Map<String,String> htdcMap = new HashMap<>();
    static Map<String,String> htacMap = new HashMap<>();
    static Map<String,String> rtdcMap = new HashMap<>();
    static Map<String,String> viMap = new HashMap<>();
    static Map<String,String> atMap = new HashMap<>();
    static{
        htdcMap.put("2","1,2,3,4,5,6,7,8,9,10,11,40");
        htdcMap.put("3","50,51,52,53,54,55,56,57,58,59,60,61,62,63,64");
        htdcMap.put("4","108,109,110,208,209,210");
        htdcMap.put("5","300,101,102,103,104,112,114,116,118,119,120,201,202,203,204,212,214,216,218,219,220,301");
        htdcMap.put("6","115,117,215,217");

        htacMap.put("2","5,6,7,8,9,10,11,12,13,14,15,16,20,21,22,23");
        htacMap.put("3","30");
        htacMap.put("4","100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116");
        htacMap.put("5","200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216");
        htacMap.put("6","116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133");
        htacMap.put("7","216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233");
        htacMap.put("8","134,135,136,137,138,139,140");
        htacMap.put("9","234,235,236,237,238,239,240");
        htacMap.put("10","141,142,143,144,145,146,147,148,149");
        htacMap.put("11","241,242,243,242,245,246,247,248,249");

        rtdcMap.put("2","1,2,3,4,5,6,7,8,9,10,11,40");
        rtdcMap.put("3","50,51,52,53,54,55,56,57,58,59,60,61,62,63,64");
        rtdcMap.put("4","105,106,107,108,109,110");
        rtdcMap.put("5","300,101,102,103,104,111,112,113,114,116,118,119,120,201,202,203,204,211,212,213,214,216,218,219,220,301");
        rtdcMap.put("6","115,117,215,217");

        viMap.put("2","1,2,100,101,102,103");
        viMap.put("3","10");

        atMap.put("1","100,101,102,103,104,105,106,107,108");
        atMap.put("3","998,999,1000,4000,7000,1001,4001,7001");
        atMap.put("4","2000,5000,2001,5001");
        atMap.put("5","3000,6000,3001,6001");
        atMap.put("6","8000,8001");
        atMap.put("7","201,202,203,204");
    }
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
    IOvnBatchLotService iOvnBatchLotService;
    @Autowired
    ApsPlanImportTask apsPlanImportTask;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    IEdcEqpStateService iEdcEqpStateService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Autowired
    private IFabLogService fabLogService;

    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;
    @Autowired
    IOvnBatchLotService ovnBatchLotService;
    @Autowired
    IOvnBatchLotParamService iOvnBatchLotParamService;
    @Autowired
    IFabEquipmentService fabEquipmentService;
    @Autowired
    IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    IEdcDskLogRecipeService edcDskLogRecipeService;
    @Autowired
    IMesLotTrackLogService mesLotTrackLogService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    private IEmailSendService emailSendService;
    @Autowired
    RepeatAlarmUtil repeatAlarmUtil;

    @Autowired
    IFabEquipmentService iFabEquipmentService;
    @Autowired
    IEdcDskLogProductionDefectiveService iEdcDskLogProductionDefectiveService;

    @Autowired
    IEdcDskLogProductionService iEdcDskLogProductionService;
    @Autowired
    EdcEqpLogParamServiceImpl edcEqpLogParamService;
    @Test
    public void test1(){
        apsPlanImportTask.rundskAps();
    }
    @Test
    public void fileParse() {
        OvnBatchLot ovnBatchLot = new OvnBatchLot();
        ovnBatchLot.setEqpId("SIM-HGAZO1");
        ovnBatchLot.setStartTime(new Date());
        ovnBatchLot.setOtherTempsTitle("第2温区温度当前值,第2温区温度SET,第2温区温度MIN,第2温区温度MAX,第3温区温度当前值,第3温区温度SET,第3温区温度MIN,第3温区温度MAX,第4温区温度当前值,第4温区温度SET,第4温区温度MIN,第4温区温度MAX,第5温区温度当前值,第5温区温度SET,第5温区温度MIN,第5温区温度MAX,第6温区温度当前值,第6温区温度SET,第6温区温度MIN,第6温区温度MAX,第7温区温度当前值,第7温区温度SET,第7温区温度MIN,第7温区温度MAX,第8温区温度当前值,第8温区温度SET,第8温区温度MIN,第8温区温度MAX,第9温区温度当前值,第9温区温度SET,第9温区温度MIN,第9温区温度MAX,第10温区温度当前值,第10温区温度SET,第10温区温度MIN,第10温区温度MAX,第11温区温度当前值,第11温区温度SET,第11温区温度MIN,第11温区温度MAX,第12温区温度当前值,第12温区温度SET,第12温区温度MIN,第12温区温度MAX,第13温区温度当前值,第13温区温度SET,第13温区温度MIN,第13温区温度MAX,第14温区温度当前值,第14温区温度SET,第14温区温度MIN,第14温区温度MAX,第15温区温度当前值,第15温区温度SET,第15温区温度MIN,第15温区温度MAX,第16温区温度当前值,第16温区温度SET,第16温区温度MIN,第16温区温度MAX,冷却区第1温区温度当前值,冷却区第1温区温度SET,冷却区第2温区温度当前值,冷却区第2温区温度SET,冷却区第3温区温度当前值,冷却区第3温区温度SET");
        OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
        ovnBatchLotParam.setOtherTempsValue("87.637,0,+0000060.000,0,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000065.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000070.000,00000000,+0000000.000,0,+0000050.000,00000000,+0000000.000,0,+0000080.000,00000000,+0000000.000,0,+0000050.000");
        ovnBatchLotParam.setTempSp("0");
        ovnBatchLotParam.setTempMin("0");
        ovnBatchLotParam.setTempMax("0");
        List<OvnBatchLotParam> list = new ArrayList<>();
        list.add(ovnBatchLotParam);
        ovnBatchLot.setOvnBatchLotParamList(list);
        iOvnBatchLotService.insert(ovnBatchLot);
    }

    @Test
    public void wbFilesize(){
        String remoteUrl = "C:\\Users\\86187_dar6n7g\\Desktop\\SIM-WB-6A\\";
        File logFile = new File(remoteUrl);
        List<File> allFiles = FileUtil.fetchFiles(logFile);
        int size = 0;
        for (File allFile : allFiles) {
            List<String> lines = new ArrayList<>();
            try {
                lines = FileUtil.readLines(allFile, "GBK");
            } catch (IOException e) {
                e.printStackTrace();
            }
            size = size + lines.size();
        }
        System.out.println(size);
    }


    @Test
    public void test11(){
        String remoteUrl = "C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (3)\\";
        File logFile = new File(remoteUrl);
        List<File> allFiles = FileUtil.fetchFiles(logFile);
        Collections.sort(allFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(o2.lastModified() < o1.lastModified()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        for (File allFile : allFiles) {
            System.out.println(allFile.getName());
        }
    }



    @Test
    public void testaaa(){
        String csvPath = "C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (3)\\";
        String csvFileName = "DSK_APJ-TRM1_202101201727181_Operationlog.csv";
        File operationFile = new File(csvPath + csvFileName);
        File logFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (3)\\aa");
        List<File> allFiles = FileUtil.fetchFiles(logFile);
        Collections.sort(allFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(o2.lastModified() < o1.lastModified()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        for (File File : allFiles) {
            read(File,operationFile);
        }
    }

    //使用字节流读取文件
    public static void read(File file,File newFile){
        int rownum = 1;
        String row;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift_JIS"));
            BufferedWriter out = new BufferedWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile,true),"GBK")));
            while ((row = in.readLine()) != null) {
                rownum++;
                out.append(row + "\r\n");
            }
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1111111(){
        List<String> ipList = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress.isLoopbackAddress()) {//回路地址，如127.0.0.1
//                    System.out.println("loop addr:" + inetAddress);
                } else if (inetAddress.isLinkLocalAddress()) {//169.254.x.x
//                    System.out.println("link addr:" + inetAddress);
                } else {
                    //非链接和回路真实ip
                    String localname = inetAddress.getHostName();
                    String localip = inetAddress.getHostAddress();
                }
            }
        }
    }

    @Test
    public void fileMove(){

        File test = new File("C:\\Users\\86187_dar6n7g\\Desktop\\APJ设备日志\\SMT");
        List<File> allFiles = FileUtil.fetchFiles(test);
        for (File allFile : allFiles) {
            System.out.println(allFile.getName());
        }
    }



    @Test
    public void LGfileParse(){
        File lg2file1 = new File("C:\\Users\\86187_dar6n7g\\Desktop\\LG二次热压\\DSK_2ND SINTER_1_202102071700280_Productionlog.csv");
        File logFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\LG二次热压\\SinteringProductionLog");
        List<File> allFiles = FileUtil.fetchFiles(logFile);
        Collections.sort(allFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long time2 = Long.parseLong(o2.getName().split("_")[3]);
                long time1 = Long.parseLong(o1.getName().split("_")[3]);

                if(time2 < time1){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        for (File lgFile : allFiles) {
            List<String> lines = new ArrayList<>();
            try {
                lines = FileUtil.readLines(lgFile, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(lines.get(0));
            try {
                FileUtil.writeLines(lg2file1,"GBK",lines,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void statusFileparse(){
        File statusFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹\\Seqlog00.log");
        List<String> lines = new ArrayList<>();
        try {
            lines = FileUtil.readLines(statusFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newDate = "";
        if(lines.get(lines.size()-1).contains("Send")){
            newDate=lines.get(lines.size()-1).split("Send")[0].trim();
        }
        System.out.println(newDate);
        newDate = DateUtil.getYear().substring(0,2)+newDate.replace("/","-");
        System.out.println(newDate);
        try {
            Date time = DateUtil.parseDate(newDate,"yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void dmStatusParse(){
        //for (int  j = 1;  j <=7 ;  j++) {
            //String eqpId = "SIM-DM"+j;
            File statusFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\产量\\Seqlog00.log");
            List<String> lines = new ArrayList<>();
            try {
                lines = FileUtil.readLines(statusFile, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //判断文件是否更新
            String lastLine = lines.get(lines.size() - 1);
            if(StringUtil.isBlank(lastLine)){
                lastLine = lines.get(lines.size() - 2);
            }
            String newDate = lastLine.substring(0,21);
            String oldDate = "21/03/02 09:35:52.198";
            if(oldDate==null){
                oldDate = "21/03/02 09:35:52.198";
            }
            if ((newDate).equals(oldDate)) {
                return ;
            } else {
                oldDate = "21/03/02 09:35:52.198";
            }
            //获取状态灯变化数据
            List<String> lineList = new ArrayList<>();
            for (int i = lines.size() - 1; i >= 0; i--) {
                if(lines.get(i).length()<22){
                    continue;
                }
                String lineDate = lines.get(i).substring(0,21);
                if (lines.get(i).contains("Lamp") && !lineDate.equals(oldDate)) {
                    lineList.add(lines.get(i));
                }if(lineDate.equals(oldDate)){
                    break;
                }
            }
            for (String s : lineList) {
                System.out.println(s);
            }
            /*for (int i = lineList.size()-1; i >=0; i--) {
                String  statusChangeline = lineList.get(i);
                if("".equals(statusChangeline)){
                    continue;
                }else{
                    System.out.println(statusChangeline);
                }
            }
            List<EdcEqpState> statelist = new ArrayList<>();

            for (int i = lineList.size()-1; i >=0; i--) {
                String  statusChangeline = lineList.get(i);
                EdcEqpState edcEqpState = new EdcEqpState();
                edcEqpState.setEqpId(eqpId);
                String startTime = DateUtil.getYear().substring(0, 2) + statusChangeline.split("Send")[0].trim().replace("/", "-");
                Date time = null;
                try {
                    time = DateUtil.parseDate(startTime, "yyyy-MM-dd HH:mm:ss.SSS");
                    edcEqpState.setStartTime(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String state = "RUN";
                if (statusChangeline.contains("Warning(On)")) {
                    state = "IDLE";
                } else if (statusChangeline.contains("Warning(Off)") || statusChangeline.contains("Error(Off)") || statusChangeline.contains("Auto Running(On)")) {
                    state = "RUN";
                } else if (statusChangeline.contains("Auto Running(Off)")) {
                    state = "DOWN";
                } else if (statusChangeline.contains("Error(On)")) {
                    state = "ALARM";
                }
                if(statelist.size()>0){
                    EdcEqpState lastedcEqpState = statelist.get(statelist.size()-1);
                    lastedcEqpState.setEndTime(time);
                    Double statetime = (double) (edcEqpState.getStartTime().getTime() - lastedcEqpState.getStartTime().getTime());
                    lastedcEqpState.setStateTimes(statetime);
                }
                edcEqpState.setState(state);
                statelist.add(edcEqpState);
            }
            for (EdcEqpState edcEqpState : statelist) {
                try {
                    iEdcEqpStateService.insert(edcEqpState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        //}
    }

    @Test
    public void test111(){
        List<EdcEqpState> list = new ArrayList<>();
        EdcEqpState edcEqpState1 = new EdcEqpState();
        edcEqpState1.setEqpId("1");
        list.add(edcEqpState1);
        EdcEqpState edcEqpState = list.get(0);
        edcEqpState.setEqpId("2");
        System.out.println(list.get(0));
    }

    @Test
    public void wbYeildCheck(){
        File newFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)\\新建 Microsoft Excel 工作表.csv");
        File logFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)\\新建文件夹\\");
        List<File> allFiles = FileUtil.fetchFiles(logFile);
        Collections.sort(allFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long time2 = Long.parseLong(o2.getName().split("_")[2]);
                long time1 = Long.parseLong(o1.getName().split("_")[2]);

                if(time2 < time1){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
       List<String> newa = new ArrayList<>();
        for (File allFile : allFiles) {
            List<String>  lines = new ArrayList<>();
            try {
                //lines = FileUtil.readLines(logFile,"Windows-31J");
                lines = FileUtil.readLines(allFile,"UTF-16");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String line : lines) {
                System.out.println(line);
                newa.add(line);
            }
        }
        try {
            FileUtil.writeLines(newFile, "GBK", newa);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void lgFileCheck(){
        File newFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\SMT\\SMT\\APJ-FRD-SMT1\\新建 Microsoft Excel 工作表.csv");
        File logFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\SMT\\SMT\\APJ-FRD-SMT1\\新建文件夹");
        List<File> allFiles = FileUtil.fetchFiles(logFile);
        Collections.sort(allFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(o1.lastModified() > o2.lastModified()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        List<String> newList = new ArrayList<>();
        List<String> lines1 = new ArrayList<>();
        try {
            lines1 = FileUtil.readLines(allFiles.get(0), "Shift_JIS");
        } catch (IOException e) {
            e.printStackTrace();
        }
        newList.add(FileUtil.csvBom+lines1.get(0));
        newList.add(lines1.get(1));
        for (File allFile : allFiles) {
            List<String> lines = new ArrayList<>();
            try {
                lines = FileUtil.readLines(allFile, "Shift_JIS");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line = lines.get(2);
            newList.add(line);
        }
        try {
            FileUtil.writeLines(newFile, "UTF-8", newList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void fileParseTest(){
//        File proFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)\\DSK_APJ-HTRT1_20210310104052_Productionlog.csv");
//        List<String> proLines = new ArrayList<>();
//        try {
//            proLines = FileUtil.readLines(proFile, "GBK");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        File file1 = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹\\RUN_5003768_1326A_L1_ST1_210401_1454_39438178.csv");
//        File file2 = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)\\RUN_5003741_1118B_L1_ST1_210310_1036_12345678.DLK");
//        File file3 = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (2)\\RUN_5003741_1118B_L1_ST1_210310_1047_12345678.DLK");
        List<File> HTDCFiles = new ArrayList<>();
        HTDCFiles.add(file1);
//        List<File> HTACFiles = new ArrayList<>();
//        HTACFiles.add(file2);
//        List<File> RTDCFiles = new ArrayList<>();
//        RTDCFiles.add(file3);
        //高温DC
        Map<String, String> HDCMap = parseParamLine(HTDCFiles,"1326A","APJ-AT1");
        //高温AC
//        Map<String, String> HACMap = parseParamLine(HTACFiles,"1118B","HTAC");
//        //室温DC
//        Map<String, String> RDCMap = parseParamLine(RTDCFiles,"1118B","RTDC");
//        List<String> newProLogLines = new ArrayList<>();
//        for (String proLine : proLines) {
//            if(!proLine.contains("APJ")){
//                newProLogLines.add(proLine);
//                continue;
//            }
//            String proLine1 = proLine;
//            if(proLine.split(",",-1).length<45){
//                int length = proLine.split(",",-1).length;
//                for (int i = 0; i <(45-length) ; i++) {
//                    proLine1 = proLine1 + ",";
//                }
//            }
//            String trayId = "";
//            if(proFile.getName().contains("VI")){
//                trayId =proLine.split(",")[13]+"-"+proLine.split(",")[14]+"-"+proLine.split(",")[15];
//            }else{
//                trayId = proLine.split(",")[22].split(";")[1];
//            }
//            proLine = proLine + HDCMap.get(trayId).split("ABCD")[0] +HACMap.get(trayId).split("ABCD")[0] + RDCMap.get(trayId).split("ABCD")[0] + HDCMap.get(trayId).split("ABCD")[1] + HACMap.get(trayId).split("ABCD")[1] + RDCMap.get(trayId).split("ABCD")[1];
//            newProLogLines.add(proLine);
//        }
//        try {
//            FileUtil.writeLines(proFile, "GBK", newProLogLines, false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public Map<String, String> parseParamLine(List<File> paramFiles,String lotNo,String eqpId) {
        //检查机原始日志
        Map<String, String> paramMap = new HashMap<>();
        for (File paramFile : paramFiles) {
            if(!paramFile.getName().contains(lotNo)){
                continue;
            }
            List<String> Lines = null;
            try {
                Lines = FileUtil.readLines(paramFile, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //每一组检查数据存为一行
            List<String> ParamLines = new ArrayList<>();
            String paramLine = "";
            for (int i = 3; i < Lines.size(); i++) {
                String line = Lines.get(i);
                //每一个检查数据从H开始，到下一个H结束
                if (line.equals("H")) {
                    ParamLines.add(paramLine);
                    paramLine = "";
                } else {
                    paramLine = paramLine + "ABCD" + line;
                    if(i == Lines.size()-1){
                        ParamLines.add(paramLine);
                    }
                }

            }
            for (String line : ParamLines) {
                Map<String, String> judgeMap = new HashMap<>();
                String proparamLine = "";
                String columns[] = line.split("ABCD");
                String trayId = "";
                if(line.contains("T-Serial") && line.contains("TrayPosX") && line.contains("TrayPosY")){
                    trayId = columns[9].split(",")[3]+"-"+columns[10].split(",")[3]+"-"+columns[11].split(",")[3];
                }else{
                    trayId =  columns[9].split(",")[3];
                    int length = trayId.length();
                    for (int i = 0; i < (4 - length); i++) {
                        trayId = "0" + trayId;
                    }
                }
                String title = "";
                for (int i = 10; i < columns.length; i++) {
                    String param = columns[i];
                    String paramStr[] = param.split(",");
                    if (paramStr.length != 8) {
                        continue;
                    }
                    String judgeNo = paramStr[0];
                    //max                 min                 avg
                    proparamLine = proparamLine + "," + paramStr[6] + "," + paramStr[5] + "," + paramStr[3];
                    try {
                        title = title +"  "+ paramStr[2] + "最大值,"+ paramStr[0] + "  "+ paramStr[2] + "最小值,"+ paramStr[0] + "  "+ paramStr[2] + "实测值,";
                        if(paramStr[3].contains("x")){
                            if(paramStr[6].equals(paramStr[3]) && paramStr[5].equals(paramStr[3])){
                                judgeMap.put(judgeNo,"Y");
                            }else {
                                judgeMap.put(judgeNo,"N");
                            }
                        }else{
                            double max = Double.parseDouble(paramStr[6]);
                            double min = Double.parseDouble(paramStr[5]);
                            double avg = Double.parseDouble(paramStr[3]);
                            if(avg<=max && min<=avg){
                                judgeMap.put(judgeNo,"Y");
                            }else {
                                judgeMap.put(judgeNo,"N");
                            }
                            /*Boolean max = paramStr[6].startsWith("-");
                            Boolean min = paramStr[5].startsWith("-");
                            Boolean avg = paramStr[3].startsWith("-");
                            if( max && min && avg){
                                if(paramStr[3].compareTo(paramStr[6])>=0 && paramStr[5].compareTo(paramStr[3])>=0){
                                    judgeMap.put(judgeNo,"Y");
                                }else {
                                    judgeMap.put(judgeNo,"N");
                                }
                            }else if(!max && min && avg){
                                if(paramStr[3].compareTo(paramStr[6])<=0 && paramStr[5].compareTo(paramStr[3])>=0){
                                    judgeMap.put(judgeNo,"Y");
                                }else {
                                    judgeMap.put(judgeNo,"N");
                                }
                            } else {
                                if(paramStr[3].compareTo(paramStr[6])<=0 && paramStr[5].compareTo(paramStr[3])<=0){
                                    judgeMap.put(judgeNo,"Y");
                                }else {
                                    judgeMap.put(judgeNo,"N");
                                }
                            }*/
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(title);
                String paramJudgeStr = parseJudgerStr(eqpId,judgeMap);
                paramMap.put(trayId, paramJudgeStr+"ABCD"+proparamLine);
            }
        }
        return paramMap;
    }

    public String parseJudgerStr(String eqpId, Map<String,String> judgeMap){
        String paramJudgeNos = "";
        String paramJudgeStr = "";
        Map<String,String> judgeNoMap = new HashMap<>();
        if(eqpId.equals("HTDC")){
            judgeNoMap = htdcMap;
            paramJudgeNos = "2,3,4,5,6";
        }else if(eqpId.equals("HTAC")){
            judgeNoMap = htacMap;
            paramJudgeNos = "2,3,4,5,6,7,8,9,10,11";
        }else if(eqpId.equals("RTDC")){
            judgeNoMap = rtdcMap;
            paramJudgeNos = "2,3,4,5,6";
        }else if(eqpId.equals("VI")){
            judgeNoMap = viMap;
            paramJudgeNos = "2,3";
        }else if(eqpId.equals("AT")){
            judgeNoMap = atMap;
            paramJudgeNos = "1,3,4,5,6,7";
        }
        //根据不良项目名NO   2,3,4,5,6
        String judgeNos[] = paramJudgeNos.split(",");
        for (String judgeNo : judgeNos) {
            //根据不良项目名NO 获取其对应的参数No
            String judgeno[] = judgeNoMap.get(judgeNo).split(",");
            String judgeStr = ",Y,";
            for (String s : judgeno) {
                //获取参数No对应的判定结果 如果有N 则该不良项目的判定结果为N 且列出导致不良的参数的No
                if("N".equals(judgeMap.get(s))){
                    judgeStr = ",N,"+s;
                    break;
                }
            }
            paramJudgeStr = paramJudgeStr+judgeStr;
        }
        System.out.println(paramJudgeStr);
        return paramJudgeStr;
    }






    public void dmFileParseTest(){
        File newFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\APJ设备日志\\SMT\\SMT\\APJ-FRD-SMT1\\TraceData\\PcbLogInformation\\PcbMountLog\\Y51775\\PcbMountLog+FRD_MASSPRO+Y51775+20210218190722+2+J000500024.csv");
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\APJ设备日志\\新建文本文档.csv");
        List<String> lines1 = new ArrayList<>();
        try {
            lines1 = FileUtil.readLines(newFile, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : lines1) {
            System.out.println(s);
        }
        lines1.add("测试");
        try {
            FileUtil.writeLines(file, "GBK", lines1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Map<String,String> parseParamLine(File paramFile){
        //检查机原始日志
        List<String> Lines = null;
        try {
            Lines = FileUtil.readLines(paramFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,String> paramMap = new HashMap<>();
        //每一组检查数据存为一行
        List<String> ParamLines = new ArrayList<>();
        String paramLine = "";
        for (int i = 3; i < Lines.size(); i++) {
            String line = Lines.get(i);
            //每一个检查数据从H开始，到下一个H结束
            if(line.equals("H")){
                ParamLines.add(paramLine);
                paramLine = "";
            }else{
                paramLine = paramLine+ "()" + line;
            }
        }

        return paramMap;
    }

    @Test
    public void  ASSParse(){
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (4)\\DSK_JIG ASSY_1_202102081257560_Productionlog.csv");
        List<String> fileLines = new ArrayList<>();
        try {
            fileLines = FileUtil.readLines(file, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
        for (String fileLine : fileLines) {
            line = fileLine.replace("・","/");
            System.out.println(line);
        }
        File newfile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (4)\\新建文本文档.csv");
        try {
            FileUtil.write(newfile, line+System.getProperty("line.separator"),"GBK", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void  lingouRecipeLogParse(){
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\test\\RUN_5003741_1525A_L1_ST1_210608_1523_39984318.DLK");
        String recipeData = recipeParse(file);
        System.out.println(recipeData);
    }



    public String recipeParse(File inspectionFile){
        String recipeLine = "";
        List<String> inspectionLines = new ArrayList<>();
        try {
            inspectionLines = FileUtil.readLines(inspectionFile, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> allRecipeLines = new ArrayList<>();
        for (int i = 3; i < inspectionLines.size(); i++) {
            String line = inspectionLines.get(i);
            //每一个检查数据从H开始，到下一个H结束
            if (line.equals("H")) {
                allRecipeLines.add(recipeLine);
                recipeLine = "";
            } else {
                recipeLine = recipeLine + "ABCD" + line;
                if(i == inspectionLines.size()-1){
                    allRecipeLines.add(recipeLine);
                }
            }
        }
        List<String> recipeLines = new ArrayList<>();
        for (String line : allRecipeLines) {
            String recipeline = "";
            String columns[] = line.split("ABCD");
            for (int i = 10; i < columns.length; i++) {
                String param = columns[i];
                String paramStr[] = param.split(",");
                if (paramStr.length != 8) {
                    continue;
                }
                if(paramStr[0].contains("99990") || paramStr[0].contains("99991") || paramStr[0].contains("99992") || paramStr[0].contains("99993") || paramStr[0].contains("99994")){
                    continue;
                }
                //参数名(编号)                        参数设定值 最小值~最大值
                recipeline = recipeline + "," + paramStr[2]+"("+paramStr[0]+")";
            }
            System.out.println(recipeline);
            recipeLines.add(recipeline);
        }
        recipeLine="";
        for (String line : recipeLines) {
            if(line.length()>recipeLine.length()){
                recipeLine = line;
            }
        }
        return recipeLine;
    }

    @Test
    public void fileUtest(){
        File newFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\文件格式测试\\DSK_APJ-HB1-SINTERING1_202103101155390_Productionlog.csv");
        List<String> lines1 = new ArrayList<>();
        try {
            lines1 = FileUtil.readLines(newFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtil.writeLines(newFile, "GBK", lines1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test12344(){
        File logFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (3)\\新建文本文档.txt");
        List<File> fileList = FileUtil.fetchFiles(logFile);
        for (File eqpFile : fileList) {
            System.out.println(eqpFile.getName());
        }
    }

    @Test
    public void test12366() throws Exception{
        File recipeRecord = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (3)\\新建文本文档.txt");
        String content = FileUtil.readFileToString(recipeRecord, "GBK");
        String recipeLine = "sdnksdkjslkjklsamalkasdlsadlksdalmkal";
        if(content.equals(recipeLine)){
            recipeLine = "164164616166661";
        }
        FileUtil.write(recipeRecord, recipeLine,false);
    }




    @Test
    public void dmOperationFileParse() throws Exception{
        File oldfile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹\\DSK_APJ-IGBT-DM1_20210329080000_Operationlog.csv");
        File newfile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹\\test.csv");

        String content = FileUtil.readFileToString(oldfile, "Shift_JIS");
        System.out.println(content);
        //read(oldfile,newfile);
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹\\新建文本文档.csv");
        FileUtil.write(file, "実装ﾉｽﾞﾙ吸着ｾﾝｻｰOFF NG <NOT SQX1101>,[ALARM 0002] 安全ｶﾊﾞｰ開放 <IN INX0C8,[ALARM 0456] 供給ﾉｽﾞﾙ吸着ｾﾝｻｰOFF NG <NOT SQX1100>","GBK", true);
    }





    @Test
    public void satFileParstTest(){
        File logFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (7)");
        List<File> fileList = FileUtil.fetchFiles(logFile);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long f1 = 0;
                long f2 = 0;
                f1 =  Long.parseLong(o1.getName().split("_")[2]);
                f2 =  Long.parseLong(o2.getName().split("_")[2]);
                if(f2 < f1){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        for (File eqpFile : fileList) {
            List<String> lines = null;
            try {
                lines = FileUtil.readLines(eqpFile, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (StringUtil.isBlank(line)) {
                    return ;
                }
                String[] columns = line.split(",", -1);
                String startdate = columns[4];
                String enddate = columns[5];
                Date statrTime = DateUtil.parseDate(startdate);
                Date endTime = DateUtil.parseDate(enddate);
                Long oneduration = (endTime.getTime()-statrTime.getTime())/lines.size();
                Date statrtime = DateUtil.parseDate(startdate);
                Date endtime = DateUtil.parseDate(enddate);
                statrtime = new Date(statrtime.getTime()+i*oneduration);
                endtime = new Date(endtime.getTime()-(lines.size()-i-1)*oneduration);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                String start = simpleDateFormat1.format(statrtime);
                String end = simpleDateFormat1.format(endtime);
                columns[4] = start;
                columns[5] = end;
                String productionLine = "";
                for (String column : columns) {
                    if(productionLine.equals("")) {
                        productionLine = column;
                    }else{
                        productionLine = productionLine + "," + column;
                    }
                }
                System.out.println(productionLine);
            }
        }
        
    }


    @Test
    public void redlowProductionLogParseTest(){
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹\\DSK_APJ-FRD-REFLOW1_202103290635141_Productionlog.csv");
        List<EdcDskLogProduction> abc =  parse("APJ-FRD-REFLOW1",file,0,true);
        Object logList = abc;
        String logJson = JsonUtil.toJsonString(logList);
        rabbitTemplate.convertAndSend("C2S.Q.PRODUCTIONLOG.DATA", logJson);
    }


    public List<EdcDskLogProduction> parse(String eqpId, File file, int startLineno, Boolean lastFlag) {
        List<EdcDskLogProduction> productionLogList = Lists.newArrayList();
        String fileContent = "";
        String encoding = "";
        try {
            if (eqpId.contains("CLEAN") || eqpId.contains("SORT") || eqpId.contains("2DAOI") || eqpId.contains("TRM") || eqpId.contains("SAT") || eqpId.contains("SMT")|| eqpId.contains("DM")) {
                encoding = "GBK";
            } else {
                encoding = "UTF-8";
            }
            fileContent = FileUtil.readFileToString(file, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = fileContent.split("\\r\\n");
        //针对3DAOI进行特殊处理
        if (!lines[0].contains("设备No") && eqpId.contains("DM") && !lastFlag) {
            String title = "";
            if (eqpId.contains("DM")) {
                title = "设备No,设备名,号机,程序名称,作业开始时间,作业完了时间,日的投入数  连番,批量内  连番,日的生産数,批量内  生産数,制品的序列号,制品的批量,制品的品番,投入夹具识别信息,投入夹具地址信息x,投入夹具地址信息Y,排出夹具识别信息,排出夹具地址信息x,排出夹具地址信息Y,识别引线框信息,引线框膜腔No X,引线框膜腔No Y,每1个製品信息,作业指示书序列号,作业指示书批量,作业指示书品番,综合判定,ウェハID,ウェハX,ウェハY,荷重,ツール温度,サイクルタイム";
            }
            fileContent = title + System.getProperty("line.separator") + fileContent;
            try {
                FileUtil.write(file, fileContent, "GBK", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lines[0] = "";
        }
        if (startLineno > lines.length) {
            System.out.println("{}行数少于record中已读行数,已读行数{}, 当前文件总行数{}!"+file.getName()+ startLineno+ lines.length);
            return null;
        }
        for (int lineno = startLineno; lineno < lines.length; lineno++) {
            String line = lines[lineno];
            if (StringUtil.isBlank(line)) {
                continue;
            }

            EdcDskLogProduction productionLog = new EdcDskLogProduction();
            productionLog.setEqpId(eqpId);
            //productionLog.setEqpModelId(fabEquipment.getModelId());
            //productionLog.setEqpModelName(fabEquipment.getModelName());


            String[] columns = line.split(",", -1);
            int columnNo = 3;
            productionLog.setRecipeCode(columns[3]);
            try {

                String startDateStr = columns[4];
                //
                if (eqpId.contains("2DAOI")) {
                    startDateStr = startDateStr.substring(0, 23);
                }
                if (startDateStr.length() == 21) {
                    startDateStr = startDateStr + "00";
                }
                if (startDateStr.contains("/")) {
                    startDateStr = startDateStr.replace("/", "-");
                }
                if (startDateStr.length() == 19) {
                    startDateStr = startDateStr + ".000";
                }
                Date startDate = DateUtil.parseDate(startDateStr, "yyyy-MM-dd HH:mm:ss.SSS");
                productionLog.setStartTime(startDate);
                String endDateStr = columns[5];
                if (endDateStr.contains("/")) {
                    endDateStr = endDateStr.replace("/", "-");
                }
                if (endDateStr.length() > 10) {
                    if (eqpId.contains("2DAOI")) {
                        endDateStr = endDateStr.substring(0, 23);
                    }
                    if (startDateStr.length() == 21) {
                        endDateStr = endDateStr + "00";
                    }
                    if (endDateStr.length() == 19) {
                        endDateStr = endDateStr + ".000";
                    }
                    Date endDate = DateUtil.parseDate(endDateStr, "yyyy-MM-dd HH:mm:ss.SSS");
                    productionLog.setEndTime(endDate);
                }
            } catch (ParseException e) {
                System.out.println("产量日志解析过程出错 Exception:");
                e.printStackTrace();
            }
            int dayInput = 0;
            int lotInput = 0;
            int dayYield = 0;
            int lotYield = 0;
            if (!columns[6].equals("")) {
                dayInput = Integer.parseInt(columns[6].replace(" ",""));
            }
            if (!columns[7].equals("")) {
                lotInput = Integer.parseInt(columns[7].replace(" ",""));
            }
            if (!columns[8].equals("")) {
                dayYield = Integer.parseInt(columns[8].replace(" ",""));
            }
            if (!columns[9].equals("")) {
                lotYield = Integer.parseInt(columns[9].replace(" ",""));
            }
            productionLog.setDayInput(dayInput);
            productionLog.setLotInput(lotInput);
            productionLog.setDayYield(dayYield);
            productionLog.setLotYield(lotYield);
            //持续时间
            //String duration = columns[columnNo++];
            //if( StringUtil.isNumeric(duration)){
            //    productionLog.setDuration(Double.parseDouble(duration));
            //}
            //制品的序列号	制品的批量	制品的品番	制品序列	作业指示书的订单	作业指示书的批量	作业指示书的品番
            productionLog.setMaterialNo(columns[10]); //制品的序列号（订单）
            productionLog.setMaterialLotNo(columns[11]); //制品的批量
            productionLog.setMaterialModel(columns[12]);//制品的品番
            //productionLog.setMaterialNo2(columns[columnNo++]); //制品序列
            productionLog.setOrderNo(columns[23]);  //作业指示书的订单
            productionLog.setLotNo(columns[24]);  //作业指示书的批量
            productionLog.setProductionNo(columns[25]); //作业指示书的品番
            String[] paramsValues = Arrays.copyOfRange(columns, 27, columns.length);
            productionLog.setParamValue(StringUtil.join(paramsValues, ","));
            String judge = columns[26];
            if (judge.equals("OK") || judge.equals("1") || judge.equals("Y") || judge.equals("")) {
                judge = "Y";
            } else {
                judge = "N";
            }
            productionLog.setJudgeResult(judge);
            productionLogList.add(productionLog);
        }
        if (productionLogList.size() == 0) {
            return null;
        }
        return productionLogList;

    }

    @Test
    public void AOIParse() throws Exception{
        int startLine = 0;
        File eqpFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (4)\\APJ-HB1-3DAOI1_PARAM-XCEED_20210428103330_Productionlog.CSV");
        String eqpId = "APJ-HB1-3DAOI1";
//        String csvPath = SysConfig.LOCAL_FILE_ROOT_PATH + fabEquipment.getLineNo() + File.separator + fabEquipment.getOfficeName() + File.separator + fabEquipment.getEqpId() + File.separator;
        String csvPath = "C:\\Users\\86187_dar6n7g\\Desktop\\新建文件夹 (4)\\";
        String csvFileName = "DSK_APJ-HB1-3DAOI1_202105121406560_Productionlog.csv";
        File productionlogFile = new File(csvPath + csvFileName);
        List<String> lines = FileUtil.readLines(eqpFile, "UTF-8");
        if (lines == null || lines.size() == 0) {
            System.out.println("return true;");
        }
        if (startLine > lines.size()) {
            System.out.println("return true;");
        }
        String[] columns1 = lines.get(0).split(",", -1);
        Date statrTime = DateUtil.parseDate(columns1[4]);
        Date endTime = DateUtil.parseDate(columns1[5]);
        //一条数据的工作时间
        Long oneduration = (endTime.getTime()-statrTime.getTime())/lines.size();
        for (int i = startLine; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtil.isBlank(line)) {
                System.out.println("return true;");
            }
            String[] columns = line.split(",", -1);
            String startdate = columns[4];
            String enddate = columns[5];
            Date statrtime = DateUtil.parseDate(startdate);
            Date endtime = DateUtil.parseDate(enddate);
            statrtime = new Date(statrtime.getTime()+i*oneduration);
            endtime = new Date(endtime.getTime()-(lines.size()-i-1)*oneduration);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            String start = simpleDateFormat1.format(statrtime);
            String end = simpleDateFormat1.format(endtime);
            columns[4] = start;
            columns[5] = end;
            String productionLine = StringUtil.join(columns, ",");
            productionLine=productionLine+System.getProperty("line.separator");
            FileUtil.write(productionlogFile, productionLine,"GBK", true);
        }
        System.out.println("return true;");
    }


    public static void main(String[] args) {
        File newFile = new File("C:\\Users\\86187_dar6n7g\\Desktop\\Test\\APJ-IGBT-3DAOI1_PARAM-XCEED_20210519113021_Productionlog.CSV");
        try {
            List<String> lines = FileUtil.readLines(newFile, "UTF-8");
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileDataUpload(){
        List<File> fileList = new ArrayList<>();
        fileList = (List<File>) FileUtil.listFiles(new File("C:\\Users\\86187_dar6n7g\\Desktop\\test\\"), new String[]{"csv"}, false);
        for (File eqpFile : fileList) {
            String eqpId = eqpFile.getName().split("_")[1];
            if(eqpId.equals("XrayDevice")){
                eqpId = "APJ-XRAY1";
            }
            eqpId = "APJ-VI1";
            List<EdcDskLogProduction> productions = new ArrayList<>();
            productions = parse1(eqpId,eqpFile,0,true);
            if(productions!=null && productions.size()>0){
                for (EdcDskLogProduction production : productions) {
                    System.out.println(production);
                }
            }else {
                continue;
            }
            if ( productions ==null || productions.size()==0 ){
                System.out.println(eqpFile.getName());
            }else {
                String logJson = JsonUtil.toJsonString(productions);
                if (!"[]".equals(logJson)) {
                    try {
                        parseProductionlog(logJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }





    public List<EdcDskLogProduction> parse1(String eqpId, File file, int startLineno, Boolean lastFlag) {
        List<EdcDskLogProduction> productionLogList = Lists.newArrayList();
        String fileContent = "";
        String encoding = "";
        try {
            if (eqpId.contains("CLEAN") || eqpId.contains("SORT") || eqpId.contains("2DAOI") || eqpId.contains("TRM") || eqpId.contains("SAT") || eqpId.contains("SMT")|| eqpId.contains("DM") || eqpId.contains("DISPENSING")) {
                encoding = "GBK";
            } else {
                encoding = "UTF-8";
            }
            fileContent = FileUtil.readFileToString(file, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] lines = fileContent.split("\\r\\n");
        if(eqpId.contains("DISPENSING") || eqpId.contains("CLEAN")){
            lines = fileContent.split("\\n");
        }
        if(lines.length==1 || lines.length==0){
            System.out.println(fileContent);
        }
        //针对3DAOI进行特殊处理
        lines[0] = "";
        if (startLineno > lines.length) {
            System.out.println("return null111111111111111111111;");
            return null;
        }
        for (int lineno = startLineno; lineno < lines.length; lineno++) {

            String line = lines[lineno];
            if (StringUtil.isBlank(line)) {
                System.out.println("continue;1111111111111111111");
                continue;
            }
            if(line.contains("设备No") || line.contains("设备")){
                System.out.println("continue;222222222222222222222");
                continue;
            }
            EdcDskLogProduction productionLog = new EdcDskLogProduction();
            productionLog.setEqpId(eqpId);
            if(eqpId.contains("VI")){
                productionLog.setEqpId("APJ-VI1");
            }
            //productionLog.setEqpModelId(fabEquipment.getModelId());
            //productionLog.setEqpModelName(fabEquipment.getModelName());


            String[] columns = line.split(",", -1);
            int columnNo = 3;
            String recipeCode = columns[3];
            if(eqpId.contains("SINTERING")){
                recipeCode = columns[27];
            }
            if(eqpId.contains("ASSEMBLY")){
                recipeCode = columns[31];
            }
            productionLog.setRecipeCode(recipeCode);
            try {

                String startDateStr = columns[4];
                if (eqpId.contains("2DAOI")) {
                    startDateStr = startDateStr.substring(0, 23);
                }
                if (startDateStr.length() == 21) {
                    startDateStr = startDateStr + "00";
                }
                if (startDateStr.contains("/")) {
                    startDateStr = startDateStr.replace("/", "-");
                }
                if (startDateStr.length() == 19) {
                    startDateStr = startDateStr + ".000";
                }
                String parsePatterns = "yyyy-MM-dd HH:mm:ss.SSS";
                if(eqpId.contains("DISPENSING")){
                    parsePatterns = "yyyy-MM-dd HH:mm:ss:SSS";
                }
                Date startDate = DateUtil.parseDate(startDateStr, parsePatterns);
                productionLog.setStartTime(startDate);
                String endDateStr = columns[5];
                if (endDateStr.contains("/")) {
                    endDateStr = endDateStr.replace("/", "-");
                }
                if (endDateStr.length() > 10) {
                    if (eqpId.contains("2DAOI")) {
                        endDateStr = endDateStr.substring(0, 23);
                    }
                    if (endDateStr.length() == 21) {
                        endDateStr = endDateStr + "00";
                    }
                    if (endDateStr.length() == 19) {
                        endDateStr = endDateStr + ".000";
                    }
                    Date endDate = DateUtil.parseDate(endDateStr, parsePatterns);
                    productionLog.setEndTime(endDate);
                }
            } catch (ParseException e) {
                System.out.println(e);
                e.printStackTrace();
            }
            int dayInput = 0;
            int lotInput = 0;
            int dayYield = 0;
            int lotYield = 0;
            if (!columns[6].equals("")) {
                dayInput = Integer.parseInt(columns[6].replace(" ",""));
            }
            if (!columns[7].equals("")) {
                lotInput = Integer.parseInt(columns[7].replace(" ",""));
            }
            if (!columns[8].equals("")) {
                dayYield = Integer.parseInt(columns[8].replace(" ",""));
            }
            if (!columns[9].equals("")) {
                lotYield = Integer.parseInt(columns[9].replace(" ",""));
            }
            productionLog.setDayInput(dayInput);
            productionLog.setLotInput(lotInput);
            productionLog.setDayYield(dayYield);
            productionLog.setLotYield(lotYield);
            //持续时间
            //String duration = columns[columnNo++];
            //if( StringUtil.isNumeric(duration)){
            //    productionLog.setDuration(Double.parseDouble(duration));
            //}
            //制品的序列号	制品的批量	制品的品番	制品序列	作业指示书的订单	作业指示书的批量	作业指示书的品番
            productionLog.setMaterialNo(columns[10]); //制品的序列号（订单）
            productionLog.setMaterialLotNo(columns[11]); //制品的批量
            productionLog.setMaterialModel(columns[12]);//制品的品番
            //productionLog.setMaterialNo2(columns[columnNo++]); //制品序列
            productionLog.setOrderNo(columns[23]);  //作业指示书的订单
            productionLog.setLotNo(columns[24]);  //作业指示书的批量
            productionLog.setProductionNo(columns[25]); //作业指示书的品番
            String[] paramsValues = Arrays.copyOfRange(columns, 27, columns.length);
            productionLog.setParamValue(StringUtil.join(paramsValues, ","));
            String judge = columns[26];
            if (judge.equals("OK") || judge.equals("1") || judge.equals("Y") || judge.equals("")) {
                judge = "Y";
            } else {
                judge = "N";
            }
            productionLog.setJudgeResult(judge);
            productionLogList.add(productionLog);
        }
        //更新currentlineno
        if (productionLogList.size() == 0) {
            return null;
        }
        return productionLogList;
    }

    public void parseProductionlog(String msg) {
        //String msg = new String(message, "UTF-8");
        System.out.println("C2S.Q.PRODUCTIONLOG.DATA接收到的消息" + msg);
        List<EdcDskLogProduction> edcDskLogProductionList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogProduction>>() {});
//        if(edcDskLogProductionList.get(0).getEqpId().equals("SIM-HGAZO1")){
//            this.temperatureList(edcDskLogProductionList);}


        List<EdcDskLogProduction> proList = new ArrayList<>();
        List<EdcDskLogProduction> nextproList = new ArrayList<>();

        if (edcDskLogProductionList.size() > 0) {
            EdcDskLogProduction edcDskLogProduction0 = edcDskLogProductionList.get(0);
            String eqpId = edcDskLogProduction0.getEqpId();
            //判断数据是否为同一批次
            MesLotTrack lotTrack = mesLotTrackService.findLotByStartTime(eqpId, edcDskLogProduction0.getStartTime());
            MesLotTrack nextLotTrack = mesLotTrackService.findLastTrack(eqpId, lotTrack.getLotNo(), lotTrack.getStartTime());
            //同一批次
            if (nextLotTrack == null) {
                fixProData(edcDskLogProductionList, lotTrack);
                if(eqpId.equals("SIM-YGAZO1")){
                }
                //不同批次 将开始时间在最新批次之后的数据归为最新批次数据 其他归为旧批次数据
            } else {
                for (EdcDskLogProduction edcDskLogProduction : edcDskLogProductionList) {
                    if (edcDskLogProduction.getStartTime().before(nextLotTrack.getStartTime())) {
                        proList.add(edcDskLogProduction);
                    } else {
                        nextproList.add(edcDskLogProduction);
                    }
                }
                if (proList.size() > 0) {
                    fixProData(proList, lotTrack);
                }
                if (nextproList.size() > 0) {
                    fixProData(nextproList, nextLotTrack);
                }
            }
            if(eqpId.equals("SIM-YGAZO1")){
                if(proList.size() > 0){
                }
                if (nextproList.size() > 0) {
                }
            }
            FabEquipmentStatus fabStatus = fabEquipmentStatusService.findByEqpId(eqpId);
            if(!"RUN".equals(fabStatus.getEqpStatus())){
                EdcDskLogOperation operation = edcDskLogOperationService.findOperationData(eqpId);
                if(operation==null){
                    System.out.println("operation==null eqpId"+eqpId);
                }
                if(edcDskLogProductionList==null){
                    System.out.println("edcDskLogProductionList==null eqpId"+eqpId);
                }if(edcDskLogProductionList.get(edcDskLogProductionList.size()-1).getEndTime()==null){
                    System.out.println("edcDskLogProductionList.get(edcDskLogProductionList.size()-1).getEndTime()==null eqpId"+eqpId);
                }
                if(edcDskLogProductionList.get(edcDskLogProductionList.size()-1).getEndTime().after(operation.getStartTime())){
                    fabStatus.setEqpStatus("RUN");
                    //fabEquipmentStatusService.updateById(fabStatus);
                    EdcEqpState edcEqpState = new EdcEqpState();
                    edcEqpState.setEqpId(eqpId);
                    edcEqpState.setStartTime(edcDskLogProductionList.get(edcDskLogProductionList.size()-1).getEndTime());
                    edcEqpState.setState("RUN");
                    String stateJson = JsonUtil.toJsonString(edcEqpState);
                    //rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
                }
            }
        } else {

        }



    }

    //修正数据   先把所有数据的批量内连番改为每次加一的顺序 再对特殊设备做特殊处理
    public void fixProData(List<EdcDskLogProduction> proList, MesLotTrack mesLotTrack) {
        int i = 1;
        String eqpId = mesLotTrack.getEqpId();
        List<EdcDskLogProduction> productionList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
        //修正批量内连番
        if (productionList.size() > 0) {
            i = productionList.size() + 1;
        }
        for (EdcDskLogProduction edcDskLogProduction : proList) {
            if("N".equals(edcDskLogProduction.getJudgeResult())){
                edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                edcDskLogProduction.setLotYield(i);
            }else{
                edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                edcDskLogProduction.setLotYield(i);
                i++;
            }
        }
        //如果为REFLOW 或 PRINTER 再乘以12
        if (eqpId.contains("SIM-REFLOW") || eqpId.contains("SIM-PRINTER")) {
            for (EdcDskLogProduction edcDskLogProduction : proList) {
                edcDskLogProduction.setJudgeResult("Y");
                edcDskLogProduction.setLotYield(edcDskLogProduction.getLotYield() * 12);
            }
        }
        if (StringUtil.isNotBlank(eqpId)) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            proList.forEach(edcDskLogProduction -> {
                edcDskLogProduction.setEqpNo(fabEquipment.getEqpNo());
                edcDskLogProduction.setEqpModelId(fabEquipment.getModelId());
                edcDskLogProduction.setEqpModelName(fabEquipment.getModelName());
                if(!"N".equals(edcDskLogProduction.getJudgeResult())){
                    edcDskLogProduction.setJudgeResult("Y");
                }
            });
        }
        //将重复数据去除
        if (eqpId.contains("SIM-DM")) {
            Iterator it = proList.iterator();
            while (it.hasNext()) {
                EdcDskLogProduction obj = (EdcDskLogProduction) it.next();
                String[] params = obj.getParamValue().split(",");
                if (params.length > 2 && !"1".equals(params[1]))
                    it.remove();
            }
        }
        List<EdcDskLogProductionDefective> defectiveProList = new ArrayList<>();
        List<EdcDskLogProduction> goodPro = new ArrayList<>();
        for (EdcDskLogProduction edcDskLogProduction : proList) {
            if("N".equals(edcDskLogProduction.getJudgeResult())){
                Date startTime = edcDskLogProduction.getStartTime();
                Date endTime = edcDskLogProduction.getEndTime();
                edcDskLogProduction.setStartTime(null);
                edcDskLogProduction.setEndTime(null);
                JSONObject json = JSONObject.fromObject(edcDskLogProduction);
                EdcDskLogProductionDefective defectivePro = JsonUtil.from(json.toString(),EdcDskLogProductionDefective.class);
                defectivePro.setStartTime(startTime);
                defectivePro.setEndTime(endTime);
                defectivePro.setId(null);
                defectiveProList.add(defectivePro);
            }else{
                goodPro.add(edcDskLogProduction);
            }
        }
        if(defectiveProList.size()>0){
            iEdcDskLogProductionDefectiveService.insertBatch(defectiveProList,100);
        }
        String eventId = null;
        eventId = StringUtil.randomTimeUUID("RPT");
        EdcDskLogProduction lastPro = null;
        boolean updateFlag = false;
        try {
            if (edcDskLogProductionService.insertBatch(goodPro,100)) {
                System.out.println("数据插入成功！");
            }
            //判断该批次是否为最后一个批次 若不是 查询范围为当前批次开始到下一批次开始
            List<EdcDskLogProduction> allProList = new ArrayList<>();
            //MesLotTrack lastTrack = iMesLotTrackService.findLastTrack(mesLotTrack.getEqpId(), mesLotTrack.getLotNo(), mesLotTrack.getStartTime());
            allProList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
            lastPro = goodPro.get(goodPro.size() - 1);
            mesLotTrack.setLotYieldEqp(allProList.size());
            if (eqpId.contains("SIM-REFLOW") || eqpId.contains("SIM-PRINTER")) {
                mesLotTrack.setLotYieldEqp(allProList.size() * 12);
            }
            mesLotTrack.setUpdateBy("gxj");
            updateFlag = mesLotTrackService.updateById(mesLotTrack);
            FabEquipmentStatus fabStatus = fabEquipmentStatusService.findByEqpId(eqpId);
            if(fabStatus!=null){
                fabStatus.setLotYield(allProList.size());
                fabEquipmentStatusService.updateById(fabStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!updateFlag) {
            System.out.println("修改失败");
            mesLotTrack.setStartTime(new Date());
            if (lastPro.getOrderNo() != null) {
                mesLotTrack.setOrderNo(lastPro.getOrderNo());
            }
            mesLotTrack.setCreateBy("EQP");
            mesLotTrackService.insert(mesLotTrack);
        }
    }




    @Test
    public void  OperationParse(){
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\VI-Operation\\");
        List<File> fileList = FileUtil.fetchFiles(file);
        for (File eqpFile : fileList) {
            List<EdcAmsRecord> edcAmsRecordList = Lists.newArrayList();
            String eqpId = eqpFile.getName().split("_")[1];
            List<EdcDskLogOperation> operationList = operationParse(eqpId,eqpFile,0,false);
            if(operationList!=null && operationList.size()>0){
                edcDskLogOperationService.insertBatch(operationList,1000);
                for (EdcDskLogOperation edcDskLogOperation : operationList) {
                    FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                    edcDskLogOperation.setEqpModelName(fabEquipment.getModelName());
                    edcDskLogOperation.setEqpModelId(fabEquipment.getModelId());
                    edcDskLogOperation.setEqpNo(fabEquipment.getEqpNo());
                    String eventId = edcDskLogOperation.getEventId();
                    if(eventId.equals("2")){
                        EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                        edcAmsRecord.setEqpId(edcDskLogOperation.getEqpId());
                        String alarmCode = edcDskLogOperation.getAlarmCode();
                        edcAmsRecord.setAlarmCode(alarmCode);
                        String alarmName = edcDskLogOperation.getEventName();
                        if(edcDskLogOperation.getEventDetail()!=null && !"".equals(edcDskLogOperation.getEventDetail())){
                            if(!alarmName.equals(edcDskLogOperation.getEventDetail())){
                                alarmName = alarmName+":"+edcDskLogOperation.getEventDetail();
                            }
                        }
                        edcAmsRecord.setAlarmName(alarmName);
                        edcAmsRecord.setAlarmSwitch("1");
                        edcAmsRecord.setLotNo(edcDskLogOperation.getLotNo());
                        edcAmsRecord.setLotYield(edcDskLogOperation.getLotYield());
                        edcAmsRecord.setStartDate(edcDskLogOperation.getStartTime());
                        if (fabEquipment != null) {
                            edcAmsRecord.setLineNo(fabEquipment.getLineNo());
                            edcAmsRecord.setStationCode(fabEquipment.getStationCode());
                        }
                        edcAmsRecordList.add(edcAmsRecord);
                    }
                }
                if(edcAmsRecordList.size()>0){
                    edcAmsRecordService.insertBatch(edcAmsRecordList,1000);
                }
            }
        }
    }


    public List<EdcDskLogOperation> operationParse(String eqpId, File file, int startLineno,Boolean lastFlag) {
        List<EdcDskLogOperation> operationLogList = Lists.newArrayList();
        String fileContent = "";
        String encoding = "";
        try {
            if (eqpId.contains("CLEAN") || eqpId.contains("SORT") || eqpId.contains("2DAOI") || eqpId.contains("TRM") || eqpId.contains("SAT") || eqpId.contains("SMT") || eqpId.contains("HTRT")|| eqpId.contains("AT")|| eqpId.contains("LF") || eqpId.contains("VI") || eqpId.contains("PRINTER")) {
                encoding = "GBK";
            }else if(eqpId.contains("DM")){
                encoding = "Shift_JIS";
            }else{
                encoding = "UTF-8";
            }
            fileContent = FileUtil.readFileToString(file, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //为3DAOI设备日志添加标题

        String[] lines = fileContent.split("\\r\\n");
        if(eqpId.contains("DISPENSING")){
            lines = fileContent.split("\\n");
        }
        //||
        lines[0] = "";
        if (lines.length > startLineno) {

        } else if (lines.length < startLineno) {

        }
        for (int lineno = startLineno; lineno < lines.length; lineno++) {
            String line = lines[lineno];
            //}
            //for (String line : lines) {
            if (StringUtil.isBlank(line)) {
                continue;
            }
            EdcDskLogOperation operationLog = new EdcDskLogOperation();
            operationLog.setEqpId(eqpId);
            if(eqpId.contains("VI")){
                operationLog.setEqpId("APJ-VI1");
            }
            //operationLog.setEqpModelId(fabEquipment.getModelId());
            //operationLog.setEqpModelName(fabEquipment.getModelName());

            String[] columns = line.split(",",-1);
            int columnNo = 3;
            operationLog.setRecipeCode(columns[3]);
            operationLog.setOrderNo(columns[4]);
            operationLog.setLotNo(columns[5]);
            operationLog.setProductionNo(columns[6]);
            if(StringUtil.isNotBlank(columns[7])){
                operationLog.setDayInput(Integer.parseInt(columns[7].replace(" ","")));
            }
            if(StringUtil.isNotBlank(columns[8])){
                operationLog.setLotInput(Integer.parseInt(columns[8].replace(" ","")));
            }
            if(StringUtil.isNotBlank(columns[9])){
                operationLog.setDayYield(Integer.parseInt(columns[9].replace(" ","")));
            }
            if(StringUtil.isNotBlank(columns[10])){
                operationLog.setLotYield(Integer.parseInt(columns[10].replace(" ","")));
            }

            try {
                String startDateStr = columns[11];
                if(eqpId.contains("2DAOI")){
                    startDateStr = startDateStr.substring(0,21);
                }
                if(eqpId.contains("DM")){
                    startDateStr=startDateStr.replace("/","-");
                }
                if(startDateStr.contains("/")){
                    startDateStr=startDateStr.replace("/","-");
                }
                if(startDateStr.length()==19){
                    startDateStr = startDateStr + ".0";
                }
                String parsePatterns = "yyyy-MM-dd HH:mm:ss.S";
                if(eqpId.contains("DISPENSING")){
                    parsePatterns = "yyyy-MM-dd HH:mm:ss:SSS";
                }
                Date startDate = DateUtil.parseDate(startDateStr, parsePatterns);
                //只向服务器传输新数据
                /*if(startDate.before(cal.getTime())){
                    continue;
                }*/
                operationLog.setStartTime(startDate);
            } catch (ParseException e) {
                System.out.println("log.error(\"Operation时间处理错误\",e);" + file.getName());
                e.printStackTrace();
            }
            operationLog.setEventId(columns[12]);
            operationLog.setAlarmCode(columns[13]);
            operationLog.setEventName(columns[14]);
            operationLog.setEventDetail(columns[15]);
            if(columns.length>=17){
                operationLog.setCreateBy(columns[16]);
            }
            operationLogList.add(operationLog);
        }
        if (operationLogList.size() == 0) {
            return null;
        }
        //更新currentlineno

        //对APJ-DM事件日志进行特殊处理，把旧数据去除
        return operationLogList;
    }




}
