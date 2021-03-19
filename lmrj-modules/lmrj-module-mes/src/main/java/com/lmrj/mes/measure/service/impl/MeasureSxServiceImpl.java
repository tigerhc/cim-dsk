package com.lmrj.mes.measure.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.measure.entity.measureSx;
import com.lmrj.mes.measure.mapper.MeasureSxMapper;
import com.lmrj.mes.measure.service.MeasureSxService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Transactional
@Service("MeasureSxService")
@Slf4j
public class MeasureSxServiceImpl extends CommonServiceImpl<MeasureSxMapper, measureSx> implements MeasureSxService {
    @Autowired
    private MeasureSxMapper measureSxMapper;

    public List<Map<String, String>> findProductionNo(String type) {
        List<Map<String, String>> result = measureSxMapper.findProductionNo(type);
        for (Map map : result) {
            String productionNo = (String) map.get("productionNo");
            map.clear();
            map.put("label", productionNo);
            map.put("value", productionNo);
        }
        return result;
    }

    public List findSxNumber(String productionName, String number, String startDate, String endDate, String type, String local) {
        number = "1";
        List<Map<String, String>> result = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
        number = "2";
        List<Map<String, String>> result2 = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
        List patent = new LinkedList();
        List title = new LinkedList();
        List arr = new LinkedList();
        List totalValue = new LinkedList();
        List totalValue2 = new LinkedList();
        for (Map map : result) {
            title.add(map.get("lotNo"));
            totalValue.add(map.get(local + "1"));
            totalValue.add(map.get(local + "2"));
        }
        for (Map map : result2) {
            totalValue2.add(map.get(local + "1"));
            totalValue2.add(map.get(local + "2"));
        }

        for (int i = 0; i < 6; i++) {
            Map element = new HashMap();
            if (i == 2 || i == 3) {
                Map markLine = new HashMap();
                List dataB = new ArrayList();
                Map basic = new HashMap();
                basic.put("type", "max");
                basic.put("name", "最大数据");
                dataB.add(basic);
                markLine.put("data", dataB);
                element.put("markLine", markLine);
            }
            switch (i) {
                case 0:
                    element.put("name", "1-1:" + local.toUpperCase());
                    break;
                case 1:
                    element.put("name", "1-2:" + local.toUpperCase());
                    break;
                case 2:
                    element.put("name", "上限");
                    break;
                case 3:
                    element.put("name", "下限");
                    break;
                case 4:
                    element.put("name", "2-1:" + local.toUpperCase());
                    break;
                case 5:
                    element.put("name", "2-2:" + local.toUpperCase());
                    break;
            }
            element.put("type", "line");
            List elementArr = new LinkedList();
            int size = (result.size() > result2.size()) ? result2.size() : result.size();
            for (int j = 0; j < size; j++) {
                if (i == 2) {
                    if (local.equals("a")) {
                        elementArr.add(14.3);
                    } else if (local.equals("b")) {
                        elementArr.add(1.00);
                    } else if (local.equals("c")) {
                        elementArr.add(0.13);
                    } else if (local.equals("d")) {
                        elementArr.add(17);
                    }
                } else if (i == 3) {
                    if (local.equals("a")) {
                        elementArr.add(13.9);
                    } else if (local.equals("b")) {
                        elementArr.add(0.4);
                    } else if (local.equals("c")) {
                        elementArr.add(0.07);
                    } else if (local.equals("d")) {
                        elementArr.add(0);
                    }
                } else {
                    if (i < 2) {
                        elementArr.add(totalValue.get((j * 2) + i));
                    } else if (i > 3) {
                        if (i == 4) {
                            int q = 0;
                            elementArr.add(totalValue2.get((j * 2) + q));
                        }
                        if (i == 5) {
                            int q = 1;
                            elementArr.add(totalValue2.get((j * 2) + q));
                        }
                    }
                }
            }
            element.put("data", elementArr);
            arr.add(element);
        }
        patent.add(title);
        patent.add(arr);
        Map min = new HashMap();
        if (local.equals("a")) {
            min.put("min", 13.9);
        } else if (local.equals("b")) {
            min.put("min", 0.4);
        } else if (local.equals("c")) {
            min.put("min", 0.07);
        } else if (local.equals("d")) {
            min.put("min", 0);
        }
        patent.add(min);
        return patent;
    }
}
