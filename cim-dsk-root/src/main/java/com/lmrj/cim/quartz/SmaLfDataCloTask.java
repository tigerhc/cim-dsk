package com.lmrj.cim.quartz;

import com.lmrj.mes.measure.entity.MeasureSma;
import com.lmrj.mes.measure.service.MeasureSmaService;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SmaLfDataCloTask {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private MeasureSmaService measureSmaService;
    @Value("${dsk.lineNo}")
    String lineNo;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;

    /**
     * 获取SMA分离检查数据，每两小时获取一次
     */
    //@Scheduled(cron = "0 20 * * * ?")
    public void smaDataCol() {
        if (!jobenabled) {
            return;
        }
        String lineRecordFilePath = "cim\\cim-dsk-root\\logs\\";
        int startLineno = findLastRecordContent(lineRecordFilePath);
        String filePath = "";
        List<String> lines = null;
        File pathfile = new File("D:\\DSK1");
        pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\SMA\\SMA(IT).CSV");
        try {
            lines = FileUtil.readLines(pathfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (startLineno >= lines.size()) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<MeasureSma> smaList = new ArrayList<>();
        try {
            for (int i = startLineno; i < lines.size(); i++) {
                String dataLine = lines.get(i);
                String columns[] = dataLine.split(",");
                MeasureSma measureSma = new MeasureSma();
                measureSma.setLineNo("SMA");
                measureSma.setMeasureType("LF");
                String judge = columns[4];
                if(!"OK".equals(judge)){
                    continue;
                }
                measureSma.setMeasureJudgment("OK");
                measureSma.setMeasureName(columns[5]);
                measureSma.setMeasureCounter(columns[6]);
                measureSma.setSerialCounter(columns[3].replace("000", ""));
                measureSma.setMeaDate(df.parse(columns[1]));
                measureSma.setCreateDate(new Date());
                String lotNo = columns[1].split("-")[0];
                measureSma.setLotNo(lotNo);
                measureSma.setProductionNo("SMA");
                measureSma.setD1h(Double.parseDouble(columns[7]));
                measureSma.setD1l(Double.parseDouble(columns[8]));
                measureSma.setD2h(Double.parseDouble(columns[9]));
                measureSma.setD2l(Double.parseDouble(columns[10]));
                measureSma.setA31(Double.parseDouble(columns[11]));
                measureSma.setA32(Double.parseDouble(columns[12]));
                measureSma.setB31(Double.parseDouble(columns[13]));
                measureSma.setB32(Double.parseDouble(columns[14]));
                measureSma.setC31(Double.parseDouble(columns[15]));
                measureSma.setR31(Double.parseDouble(columns[16]));
                measureSma.setR32(Double.parseDouble(columns[17]));
                measureSma.setA41(Double.parseDouble(columns[18]));
                measureSma.setA42(Double.parseDouble(columns[19]));
                measureSma.setB41(Double.parseDouble(columns[20]));
                measureSma.setB42(Double.parseDouble(columns[21]));
                measureSma.setC41(Double.parseDouble(columns[22]));
                measureSma.setR41(Double.parseDouble(columns[23]));
                measureSma.setR42(Double.parseDouble(columns[24]));
                smaList.add(measureSma);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            measureSmaService.insertBatch(smaList,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lines.size() > startLineno) {
            log.info("SMA分离数据解析完成.解析到{}", lines.size());
            startLineno = lines.size();
            try {
                File lineRecord = new File(lineRecordFilePath);
                FileUtil.write(lineRecord, startLineno + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int findLastRecordContent(String lineRecordFileName) {
        File lineRecord;
        lineRecord = new File(lineRecordFileName);
        String content = "4";
        if (lineRecord.exists()) {
            try {
                content = FileUtil.readFileToString(lineRecord, "GBK");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Integer.parseInt(content);
    }
}
