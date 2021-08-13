package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
public class DataRestore {
    @Autowired
    private IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    private IEdcEqpStateService edcEqpStateService;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;
    public void OperationDataRestore() throws Exception{
        if(!jobenabled){
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        Date startTime = simpleDateFormat.parse("2020-05-08 00:00:00");
        Date endTime = simpleDateFormat.parse("2020-05-09 00:00:00");
        /*Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        endTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = cal.getTime();*/
        List<String> eqpIdList = edcDskLogOperationService.findEqpId(startTime, endTime);
        for (String eqpId : eqpIdList) {
            List<EdcDskLogOperation> operationList = edcDskLogOperationService.findDataByTimeAndEqpId(eqpId, startTime, endTime);
            EdcEqpState firstState = new EdcEqpState();
            firstState.setEqpId(eqpId);
            firstState.setStartTime(startTime);
            EdcEqpState lastEqpState = edcEqpStateService.findLastData2(startTime, eqpId);
            firstState.setState(lastEqpState.getState());
            List<EdcEqpState> stateList = new ArrayList<>();
            for (int i = 0; i < operationList.size(); i++) {
                EdcDskLogOperation operation = operationList.get(i);
                if (i == 0) {
                    firstState.setEndTime(operation.getStartTime());
                    Double stateTime = (double) (operation.getStartTime().getTime() - firstState.getStartTime().getTime());
                    firstState.setStateTimes(stateTime);
                    stateList.add(firstState);
                } else if (i == operationList.size() - 1) {
                    EdcEqpState eqpState = new EdcEqpState();
                    eqpState.setEqpId(eqpId);
                    eqpState.setStartTime(operation.getStartTime());
                    eqpState.setEndTime(endTime);
                    eqpState.setStateTimes((double) (endTime.getTime() - operation.getStartTime().getTime()));
                    eqpState = steState(operation, eqpState);
                    if (eqpState != null) {
                        stateList.add(eqpState);
                    }
                } else {
                    EdcDskLogOperation nextOperation = operationList.get(i + 1);
                    EdcEqpState eqpState = new EdcEqpState();
                    eqpState.setEqpId(eqpId);
                    eqpState.setStartTime(operation.getStartTime());
                    eqpState.setEndTime(nextOperation.getStartTime());
                    eqpState.setStateTimes((double) (nextOperation.getStartTime().getTime() - operation.getStartTime().getTime()));
                    eqpState = steState(operation, eqpState);
                    if (eqpState != null) {
                        stateList.add(eqpState);
                    }
                }
            }
            //防止持续时间出错
            for (int i = 0; i < stateList.size()-1; i++) {
                EdcEqpState eqpState=stateList.get(i);
                EdcEqpState nextEqpState=stateList.get(i+1);
                if(eqpState.getEndTime()!=nextEqpState.getStartTime()){
                    eqpState.setEndTime(nextEqpState.getStartTime());
                    eqpState.setStateTimes((double) (nextEqpState.getStartTime().getTime() - eqpState.getStartTime().getTime()));
                }
            }
            edcEqpStateService.insertBatch(stateList,100);
        }
    }

    public EdcEqpState steState(EdcDskLogOperation operation, EdcEqpState eqpState) {
        if ("0".equals(operation.getEventId()) || "7".equals(operation.getEventId())) {
            eqpState.setState("DOWN");
        } else if ("1".equals(operation.getEventId()) || "6".equals(operation.getEventId())) {
            eqpState.setState("RUN");
        } else if ("3".equals(operation.getEventId())) {
            eqpState.setState("IDLE");
        } else if ("2".equals(operation.getEventId())) {
            eqpState.setState("ALARM");
        } else {
            eqpState = null;
        }
        return eqpState;
    }
}
