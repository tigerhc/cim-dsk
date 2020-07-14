import com.lmrj.cim.CimBootApplication;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private AmqpTemplate rabbitTemplate;

@Test
        public void ReadFile() throws IOException {
        Map<String, Object> msgMap = new HashMap<String, Object>();
          File file = new File("F:\\lmrj\\cim-dsk\\cim-dsk-root\\src\\main\\resources\\LOG002_20200702_195711_0034.CSV");//文件路径
    /*    FileReader fileReader = null;
            fileReader = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(fileReader);
         int number = 14;//设置指定行数
        String txt = "";
        int lines = 0;
        List<String> list=new ArrayList<>();
        MsMeasureRecord msMeasureRecord=new MsMeasureRecord();
        while (txt != null) {
            lines++;
            txt = reader.readLine();
            if (lines == number) {
                System.out.println("第" + reader.getLineNumber() + "的内容是：" + txt + "\n");
                String a[] = txt.split(",");
                msMeasureRecord.setRecordId(a[1]);
                msMeasureRecord.setLotNo(a[2]);
            }
        }
        reader.close();
        fileReader.close();*/
    List<String> lines = FileUtil.readLines(file, "UTF-8");
    String mesResult=lines.get(lines.size()-1);
    String a[]=mesResult.split(",");
    MsMeasureRecord msMeasureRecord=new MsMeasureRecord();
            msMeasureRecord.setRecordId(a[1]);
            msMeasureRecord.setLotNo(a[2]);
        msgMap.put("msg",msMeasureRecord);
        String msg = JsonUtil.toJsonString(msgMap);
        rabbitTemplate.convertAndSend("zx_lxy", msg);



    }



}

