package com.lmrj.cim.quartz;

import com.lmrj.cim.CimZhlxMain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CimZhlxMain.class)
@RunWith(SpringRunner.class)
public class ProductionYieldReCalTaskTest {
    @Autowired
    ProductionYieldReCalTask productionYieldReCalTask;
    @Test
    public void fileAnalysistest() throws Exception{
        productionYieldReCalTask.fileAnalysistest();
    }
}
