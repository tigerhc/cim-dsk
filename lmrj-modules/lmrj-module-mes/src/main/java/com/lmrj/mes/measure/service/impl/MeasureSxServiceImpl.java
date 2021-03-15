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
    public List<Map<String, String>> findProductionNo(){
        List<Map<String, String>> result =  measureSxMapper.findProductionNo();
        for (Map map : result){
            String productionNo =(String) map.get("productionNo");
            map.clear();
            map.put("label",productionNo);
            map.put("value",productionNo);
        }
        return measureSxMapper.findProductionNo();
    }

    public List findSxNumber(String productionName, String number,  String startDate,String endDate,String type){
        List<Map<String, String>> result =  measureSxMapper.findSxNumber(productionName,number,startDate,endDate,type);
        List patent = new LinkedList();
        List title = new LinkedList();
        List arr = new LinkedList();
        List totalValue = new LinkedList();
        for (Map map : result){
            title.add(map.get("lotNo"));
            totalValue.add(map.get("a1"));
            totalValue.add(map.get("b1"));
            totalValue.add(map.get("c1"));
            totalValue.add(map.get("d1"));
            totalValue.add(map.get("a2"));
            totalValue.add(map.get("b2"));
            totalValue.add(map.get("c2"));
            totalValue.add(map.get("d2"));
        }

        for (int i = 0; i <8 ; i++) {
            Map element = new HashMap();
            switch (i){
                case 0 :
                               element.put("name","1:A");
                               break;
                case 1 :
                    element.put("name","1:B");
                    break;
                case 2 :
                    element.put("name","1:C");
                    break;
                case 3 :
                    element.put("name","1:D");
                    break;
                case 4 :
                    element.put("name","2:A");
                    break;
                case 5 :
                    element.put("name","2:B");
                    break;
                case 6 :
                    element.put("name","3:C");
                    break;
                case 7 :
                    element.put("name","4:D");
                    break;
            }
//            element.put("name",result.get(i).get("lotNo"));
            element.put("type","line");
            List elementArr = new LinkedList();
            for (int j = 0; j <result.size(); j++) {
                elementArr.add(totalValue.get((j*8)+i));
                element.put("data",elementArr);
            }
           arr.add(element);

        }
        patent.add(title);
        patent.add(arr);








//        for (Map map : result){
//            Map map1 = new HashMap();
//            List data = new LinkedList();
//            element1.add( map.get("lotNo"));
//            map1.put("name",map.get("lotNo"));
//            map1.put("type","line");

//            if (map.get("lotNo").equals("1225F")){
//
//                data.add(25);
//                data.add(63);
//                data.add(33);
//                data.add(72);
//                data.add(45);
//                data.add(56);
//                data.add(23);
//                data.add(62);
//            }else
//            {
//                data.add(55);
//                data.add(63);
//                data.add(31);
//                data.add(72);
//                data.add(65);
//                data.add(96);
//                data.add(26);
//                data.add(12);
//            }




//            data.add(map.get("a1"));
//            data.add(map.get("b1"));
//            data.add(map.get("c1"));
//            data.add(map.get("d1"));
//            data.add(map.get("a2"));
//            data.add(map.get("b2"));
//            data.add(map.get("c2"));
//            data.add(map.get("d2"));
//            map1.put("data",data);
//            element2.add(map1);
//        }
//        patent.add(element1);
//        patent.add(element2);


        return patent;
    }
}
