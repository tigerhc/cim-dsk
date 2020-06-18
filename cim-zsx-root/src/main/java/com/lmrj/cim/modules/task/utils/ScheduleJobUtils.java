package com.lmrj.cim.modules.task.utils;


import com.lmrj.cim.modules.task.entity.ScheduleJob;

public class ScheduleJobUtils {
     public static com.lmrj.common.quartz.data.ScheduleJob entityToData(ScheduleJob scheduleJobEntity){
    	 com.lmrj.common.quartz.data.ScheduleJob scheduleJob=new com.lmrj.common.quartz.data.ScheduleJob();
		 scheduleJob.setJobId(scheduleJobEntity.getId());
    	 scheduleJob.setCronExpression(scheduleJobEntity.getCronExpression());
    	 scheduleJob.setDescription(scheduleJobEntity.getDescription());
    	 scheduleJob.setIsConcurrent(scheduleJobEntity.getIsConcurrent());
    	 scheduleJob.setJobName(scheduleJobEntity.getJobName());
		 scheduleJob.setLoadWay(scheduleJobEntity.getLoadWay());
    	 scheduleJob.setJobGroup(scheduleJobEntity.getJobGroup());
    	 scheduleJob.setJobStatus(scheduleJobEntity.getJobStatus());
    	 scheduleJob.setMethodName(scheduleJobEntity.getMethodName());
		 scheduleJob.setMethodParams(scheduleJobEntity.getMethodParams());
		 scheduleJob.setMisfirePolicy(scheduleJobEntity.getMisfirePolicy());
    	 scheduleJob.setExecuteClass(scheduleJobEntity.getExecuteClass());
    	 return scheduleJob;
     }
}
