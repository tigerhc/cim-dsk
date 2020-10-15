package com.lmrj.fab.bc.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.bc.entity.FabOrgStructure;
import com.lmrj.fab.bc.mapper.FabOrgStructureMapper;
import com.lmrj.fab.bc.service.IFabOrgStructureService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("fabOrgStructureService")
public class FabOrgStructureServiceImpl extends CommonServiceImpl<FabOrgStructureMapper,FabOrgStructure> implements IFabOrgStructureService {

    @Override
    public String synchro(String orgCode) {
        int existFlag = baseMapper.chkExistFabCode(orgCode);
        if(existFlag > 0){
            return "已同步";
        }

        Map<String, Object> organization = baseMapper.selectOrgByCode(orgCode);
        if(null != organization && organization.size()>0){
            String parentIds = MapUtils.getString(organization,"parentIds");
            if(parentIds.startsWith("1/")){
                parentIds = parentIds.substring(2);
            }
            parentIds = parentIds + "/"+orgCode;
            String[] parent = parentIds.split("/");
            List<String> addList = new ArrayList<>();
            Map<String, Integer> flag = new HashMap<>();
            Map<Integer, FabOrgStructure> parentOrg = new HashMap<>();
            int type = 1;
            for(String pid : parent){
                if(!StringUtil.isEmpty(pid)){
                    FabOrgStructure chkExist = baseMapper.chkExistFab(pid);
                    if(chkExist==null){
                        addList.add(pid);
                        flag.put(pid,type);
                    } else {
                        parentOrg.put(type, chkExist);
                    }
                    type ++;
                }
            }
            if(addList.size()>0){
                System.out.println(addList);
                List<Map<String, Object>> parentArr = baseMapper.selectParentOrg(addList);
                for(int i=0;  i< addList.size(); i++){
                    Map<String, Object> item = null;
                    for(Map<String, Object> p : parentArr){
                        if(addList.get(i).equals(MapUtils.getString(p, "code"))){
                            item = p;
                            break;
                        }
                    }
                    if(item == null){
                        log.error("FabOrgStructureServiceImpl_synchro：sys_organization 没有找到"+addList.get(i));
                    } else{
                        int num = MapUtils.getIntValue(flag, MapUtils.getString(item, "code"));
                        FabOrgStructure thisParent = parentOrg.get(num-1);
                        if(num - 1 == 0){
                            thisParent = thisParent!=null?thisParent:new FabOrgStructure();
                            thisParent.setOrgCode(MapUtils.getString(item, "code"));
                            thisParent.setId(MapUtils.getString(item, "code"));
                            thisParent.setOrgName(MapUtils.getString(item, "name"));
                        } else{
                            thisParent = _getKey(num, thisParent, item);//如果thisParent此时为null说明代码写的bug
                        }
                        thisParent.setOrgType(String.valueOf(num));

                        baseMapper.insert(thisParent);
                        parentOrg.put(num, thisParent);
                    }
                }
            }
        }
        return "同步成功";
    }

    private FabOrgStructure _getKey(int type, FabOrgStructure fabOrgStructure, Map<String, Object> values){
        switch (type){
            case 2:
                fabOrgStructure.setCompanyId(fabOrgStructure.getId());
                fabOrgStructure.setCompanyCode(fabOrgStructure.getOrgCode());
                fabOrgStructure.setCompanyName(fabOrgStructure.getOrgName());
                break;
            case 3:
                fabOrgStructure.setFactoryId(fabOrgStructure.getId());
                fabOrgStructure.setFactoryCode(fabOrgStructure.getOrgCode());
                fabOrgStructure.setFactoryName(fabOrgStructure.getOrgName());
                break;
            case 4:
                fabOrgStructure.setDepartmentId(fabOrgStructure.getId());
                fabOrgStructure.setDepartmentCode(fabOrgStructure.getOrgCode());
                fabOrgStructure.setDepartmentName(fabOrgStructure.getOrgName());
                break;
            case 5:
                fabOrgStructure.setLineId(fabOrgStructure.getId());
                fabOrgStructure.setLineCode(fabOrgStructure.getOrgCode());
                fabOrgStructure.setLineName(fabOrgStructure.getOrgName());
                break;
            case 6:
                fabOrgStructure.setStationId(fabOrgStructure.getId());
                fabOrgStructure.setStationCode(fabOrgStructure.getOrgCode());
                fabOrgStructure.setStationName(fabOrgStructure.getOrgName());
                break;
            default:
                fabOrgStructure.setProcessId(fabOrgStructure.getId());
                fabOrgStructure.setProcessCode(fabOrgStructure.getOrgCode());
                fabOrgStructure.setProcessName(fabOrgStructure.getOrgName());
                break;
        }
        fabOrgStructure.setId(MapUtils.getString(values,"code"));
        fabOrgStructure.setOrgCode(MapUtils.getString(values,"code"));
        fabOrgStructure.setOrgName(MapUtils.getString(values,"name"));
        return fabOrgStructure;
    }
}
