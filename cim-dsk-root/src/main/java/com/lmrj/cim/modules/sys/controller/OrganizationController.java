package com.lmrj.cim.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.lmrj.cim.common.helper.VueTreeHelper;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mvc.entity.tree.BootstrapTreeHelper;
import com.lmrj.common.mvc.entity.tree.BootstrapTreeNode;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.CacheUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.core.sys.service.IOrganizationService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/sys/organization")
@ViewPrefix("modules/sys/organization")
@RequiresPathPermission("sys:organization")
@LogAspectj(title = "部门管理")
public class OrganizationController extends BaseBeanController<Organization> {

    @Autowired
    private IOrganizationService organizationService;

    protected final static String OFFICE_CACHE_NAME = "officeCache";
    public static final String OFFICE_CACHE_ID_ = "office_oid_";

    @Value("${dsk.lineNo}")
    String lineNo;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void list(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        EntityWrapper<Organization> entityWrapper = new EntityWrapper<Organization>(entityClass);
        entityWrapper.setTableAlias("t.");
        //加入条件
        String keyword=request.getParameter("keyword");
        if (!StringUtil.isEmpty(keyword)){
            entityWrapper.like("name",keyword);
        }
        List<Organization> treeNodeList = organizationService.selectTreeList(entityWrapper);
        List<VueTreeHelper.VueTreeNode> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList,"id,name,parentId,parentIds,remarks,orgType,orgGrade");
        String content = JSON.toJSONString(vueTreeNodes);
        ServletUtils.printJson(response, content);
    }

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "vueTreeData")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void vueTreeData(Queryable queryable,
                                  @RequestParam(value = "nodeid", required = false, defaultValue = "") String nodeid, HttpServletRequest request,
                                  HttpServletResponse response, PropertyPreFilterable propertyPreFilterable) throws IOException {
        EntityWrapper<Organization> entityWrapper = new EntityWrapper<Organization>(entityClass);
        entityWrapper.setTableAlias("t.");
        List<Organization> treeNodeList = organizationService.selectTreeList(queryable, entityWrapper);
        List<VueTreeHelper.VueTreeNode> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList,"id,name,parentId,parentIds,remarks");
        String content = JSON.toJSONString(vueTreeNodes);
        ServletUtils.printJson(response, content);
    }

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "bootstrapTreeData")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void bootstrapTreeData(Queryable queryable,
                                  @RequestParam(value = "nodeid", required = false, defaultValue = "") String nodeid, HttpServletRequest request,
                                  HttpServletResponse response, PropertyPreFilterable propertyPreFilterable) throws IOException {
        EntityWrapper<Organization> entityWrapper = new EntityWrapper<Organization>(entityClass);
        entityWrapper.setTableAlias("t.");
        List<Organization> treeNodeList = organizationService.selectTreeList(queryable, entityWrapper);
        List<BootstrapTreeNode> bootstrapTreeNodes = BootstrapTreeHelper.create().sort(treeNodeList);
        propertyPreFilterable.addQueryProperty("text", "href", "tags", "nodes");
        SerializeFilter filter = propertyPreFilterable.constructFilter(entityClass);
        String content = JSON.toJSONString(bootstrapTreeNodes, filter);
        ServletUtils.printJson(response, content);
    }

    @PostMapping("add")
    @LogAspectj(logType = LogType.INSERT)
    @RequiresMethodPermissions("add")
    public Response add(Organization entity, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity,result);
        organizationService.insert(entity);
        try{
            Map map=(Map)CacheUtils.get(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_);
            map.put(entity.getId(),entity);
            CacheUtils.put(OFFICE_CACHE_NAME, OFFICE_CACHE_ID_ , map);
        }catch (Exception e){
            OfficeUtils.initOffice();
        }
        return Response.ok("添加成功");
    }

    //@GetMapping(value = "{id}/update")
    //public ModelAndView update(@PathVariable("id") String id, Model model, HttpServletRequest request,
    //                           HttpServletResponse response) {
    //    Organization entity = organizationService.selectById(id);
    //    model.addAttribute("data", entity);
    //    return displayModelAndView ("edit");
    //}

    @PostMapping("{id}/update")
    public Response update(Organization entity, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity,result);
        organizationService.insertOrUpdate(entity);
        try{
            Map map=(Map)CacheUtils.get(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_);
            map.put(entity.getId(),entity);
            CacheUtils.put(OFFICE_CACHE_NAME, OFFICE_CACHE_ID_ , map);
        }catch (Exception e){
            OfficeUtils.initOffice();
        }
        return Response.ok("更新成功");
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response delete(@PathVariable("id") String id) {
        organizationService.deleteById(id);
        try{
            Map map=(Map)CacheUtils.get(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_);
            map.remove(id);
            CacheUtils.put(OFFICE_CACHE_NAME, OFFICE_CACHE_ID_ , map);
        }catch (Exception e){
            OfficeUtils.initOffice();
        }
        return Response.ok("删除成功");
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response batchDelete(@RequestParam("ids") String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        organizationService.deleteBatchIds(idList);
        try{
            Map map=(Map)CacheUtils.get(OFFICE_CACHE_NAME,OFFICE_CACHE_ID_);
            for(String id:ids){
                map.remove(id);
            }
            CacheUtils.put(OFFICE_CACHE_NAME, OFFICE_CACHE_ID_ , map);
        }catch (Exception e){
            OfficeUtils.initOffice();
        }
        return Response.ok("删除成功");
    }

    @RequestMapping("initCache")
    @LogAspectj(logType = LogType.DELETE)
    public Response initCache() {
        OfficeUtils.initOffice();
        return Response.ok("刷新成功");
    }

    @RequestMapping(value = "findStep")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void findStep(HttpServletResponse response) throws IOException {
        List<Organization> treeNodeList = organizationService.findStep("");
        String content = JSON.toJSONString(treeNodeList );
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "findYieldStep")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void findYieldStep(@RequestParam String subLineNo, HttpServletResponse response) throws IOException {//TODO 仕挂表 宽度不够
        List<Organization> treeNodeList = organizationService.findYieldStep(lineNo,subLineNo);
        String content = JSON.toJSONString(treeNodeList );
        ServletUtils.printJson(response, content);
    }

}
