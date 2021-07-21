package com.lmrj.rw.plan.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rw.plan.entity.RwPlanHis;
import com.lmrj.rw.plan.entity.VwodPlan;
import com.lmrj.rw.plan.entity.WodPlanDay;
import com.lmrj.rw.plan.mapper.WodPlanDayMapper;
import com.lmrj.rw.plan.service.IRwPlanHisService;
import com.lmrj.rw.plan.service.IVwodPlanService;
import com.lmrj.rw.plan.service.IWodPlanDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Transactional
@Service("wodplandayservice")
public class WodPlanDayServiceImpl extends CommonServiceImpl<WodPlanDayMapper, WodPlanDay> implements IWodPlanDayService{
    @Autowired
    private IRwPlanHisService rwPlanHisService;
    @Autowired
    private IVwodPlanService vwodPlanService;
    @Override
    public String checkTime(WodPlanDay wodPlanDay) {
        String ret = "";
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        try {
        switch (wodPlanDay.getPlanCycle()){
            case "1"://日
                //允许当天以及his表中存在前一天的数据
              String effectStr =  format.format(wodPlanDay.getEffectDate());
              String assignStr = format.format(wodPlanDay.getAssignedTime());
              if(!effectStr.equals(assignStr)){
                  //判断是否存在上一天的历史记录
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(wodPlanDay.getAssignedTime());
                  calendar.add(Calendar.DAY_OF_MONTH, -1);
                  Date beforetime = calendar.getTime();
                  String beforeStr = format.format(beforetime);
                  Date startAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beforeStr + " 00:00:00");
                  Date endAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(assignStr + " 00:00:00");
                  List<RwPlanHis> rwPlanHisList =  rwPlanHisService.selectList(new EntityWrapper<RwPlanHis>().between("assigned_time",startAssign,endAssign).eq("plan_id",wodPlanDay.getId()));
                  if(rwPlanHisList!=null&&rwPlanHisList.size()>0){
                      //判断当前周期是否已经发起过
                      endAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(assignStr + " 00:00:00");
                      rwPlanHisList =  rwPlanHisService.selectList(new EntityWrapper<RwPlanHis>().ge("assigned_time",endAssign).eq("plan_id",wodPlanDay.getId()));
                      if(rwPlanHisList!=null&&rwPlanHisList.size()>0){
                          ret = "本次工单周期内已发起计划，无法发起新计划";
                  }
                  }else{
                      ret = "上次工单周期内未发起计划，无法发起新计划";
                  }
              }else{
                  Date startAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(effectStr + " 00:00:00");
                  Date endAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(effectStr + " 23:59:59");
                  List<RwPlanHis> rwPlanHisList =  rwPlanHisService.selectList(new EntityWrapper<RwPlanHis>().between("assigned_time",startAssign,endAssign).eq("plan_id",wodPlanDay.getId()));
              if(rwPlanHisList!=null&&rwPlanHisList.size()>0){
                  ret = "当前工单周期已经发起过计划，无法再次发起！";
              }
              }
                break;
            case "2"://周
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置星期一为一周开始的第一天
                calendar.setMinimalDaysInFirstWeek(4);
                calendar.setTime(wodPlanDay.getEffectDate());
                int weekYearbefore = calendar.get(Calendar.YEAR);//获得当前的年
                int weekOfYearbefore = calendar.get(Calendar.WEEK_OF_YEAR);//获得当前日期属于今年的第几周
                calendar.setTime(wodPlanDay.getAssignedTime());
                int weekYear = calendar.get(Calendar.YEAR);//获得当前的年
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);//获得当前日期属于今年的第几周

                  //一致就判断当前周是否已经存在
                    calendar.setWeekDate(weekYear, weekOfYear, 2);//获得指定年的第几周的开始日期
                    long starttime = calendar.getTime().getTime();//创建日期的时间该周的第一天，
                    calendar.setWeekDate(weekYear, weekOfYear, 1);//获得指定年的第几周的结束日期
                    long endtime = calendar.getTime().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStart = simpleDateFormat.format(starttime);//将时间戳格式化为指定格式
                    String dateEnd = simpleDateFormat.format(endtime);
                    Date startAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStart + " 00:00:00");
                    Date endAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateEnd + " 23:59:59");
                    List<VwodPlan> vwodPlans = vwodPlanService.selectList(new EntityWrapper<VwodPlan>().between("assigned_time",startAssign,endAssign).eq("plan_id",wodPlanDay.getId()));
                   if(vwodPlans!=null&&vwodPlans.size()>0){
                    ret = "当前工单周期已经发起过计划，无法再次发起！";
                   }
                if(weekYearbefore==weekYear&&weekOfYear==weekOfYearbefore){

                }else{
                    //判断上个周期内是否存在数据
                    calendar.setTime(startAssign);
                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    Date lastweekStart = calendar.getTime();
                    String lastweekStartstr = simpleDateFormat.format(lastweekStart);
                    lastweekStart =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastweekStartstr + " 00:00:00");
                    vwodPlans = vwodPlanService.selectList(new EntityWrapper<VwodPlan>().between("assigned_time",lastweekStart,startAssign).eq("plan_id",wodPlanDay.getId()));
                    if(vwodPlans!=null&&vwodPlans.size()>0){

                    }else{
                        ret = "上次工单周期未发起过计划，无法再次发起！";
                    }
                }
                //不考虑当前时间后的周期校验
                break;
            case "3"://月
                break;
            case "4"://双月
                break;
        }
        } catch (ParseException e) {
                e.printStackTrace();
            }

        return null;
    }
}
