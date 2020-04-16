package com.lmrj.cim.utils;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.utils.CacheUtils;
import com.lmrj.util.spring.SpringContext;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.core.sys.service.IOrganizationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package cn.dataact.lmrj.utils
 * @title:
 * @description: Cache工具类
 * @author: 张飞
 * @date: 2018/3/22 16:11
 * @copyright: 2017 www.gzst.gov.cn Inc. All rights reserved.
 */
public class OfficeUtils {

    protected final static String OFFICE_CACHE_NAME = "officeCache";
    public static final String OFFICE_CACHE_ID_ = "office_oid_";

    public static Organization getOffice(String id) {
        //数据字典
        if (CacheUtils.get(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_ )==null){
            initOffice();
        }
        Map map=(Map)CacheUtils.get(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_);
       return  (Organization) map.get(OFFICE_CACHE_ID_+id);
    }

    /**
     * 数据字典初始化
     * @return
     */
    public static List<Organization> initOffice() {
        List<Organization> organizationList = SpringContext.getBean(IOrganizationService.class).selectList(new EntityWrapper<>());
        Map map=new HashMap();
        for (Organization organization : organizationList) {
            map.put(OFFICE_CACHE_ID_ + organization.getId(),organization);
        }
        CacheUtils.put(OFFICE_CACHE_NAME, OFFICE_CACHE_ID_ , map);
        return organizationList;
    }

    /*
     * 清除换成
     */
    public static void clear(String id) {
        CacheUtils.remove(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_ + id);
    }

}
