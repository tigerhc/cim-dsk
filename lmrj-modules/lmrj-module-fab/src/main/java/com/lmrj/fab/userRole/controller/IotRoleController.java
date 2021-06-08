package com.lmrj.fab.userRole.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.fab.userRole.entity.IotRole;
import com.lmrj.fab.userRole.service.IIotRoleService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author wdj
 * @date 2021-06-05 10:20
 */
@RestController
@RequestMapping("fab/role")
@ViewPrefix("fab/role")
@RequiresPathPermission("fab:role")
@LogAspectj(title = "fab_role")
public class IotRoleController extends BaseBeanController<IotRole> {
    @Autowired
    private IIotRoleService IotRoleService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    //@RequiresMethodMenus("list")
    public void list( HttpServletRequest request) throws IOException {
        //加入条件
        EntityWrapper<IotRole> entityWrapper = new EntityWrapper<>(IotRole.class);
        entityWrapper.orderBy("createDate",false);
        String code= request.getParameter("code");
        if (!StringUtil.isEmpty(code)){
            entityWrapper.like("code",code);
        }
        String name=request.getParameter("name");
        if (!StringUtil.isEmpty(name)){
            entityWrapper.like("name",name);
        }
        String projectCode= request.getParameter("projectId");
        if (!StringUtil.isEmpty(projectCode)){
            entityWrapper.eq("project_id",projectCode);
        }
        // 预处理
        Page pageBean = IotRoleService.selectPage(PageRequest.getPage(),entityWrapper);
        FastJsonUtils.print(pageBean,IotRole.class,"id,name,code,isSys,usable");
    }

    /**
     * 获取可用的用户列表
     *
     * @throws IOException
     */
    @GetMapping(value = "usable/list")
    public List<IotRole> usableLst() throws IOException {
        EntityWrapper<IotRole> entityWrapper = new EntityWrapper<IotRole>(IotRole.class);
        entityWrapper.orderBy("createDate",false);
        List<IotRole> usableLst = IotRoleService.selectList(entityWrapper);
        return usableLst;
    }

    @PostMapping("add")
    @LogAspectj(logType = LogType.INSERT)
    //@RequiresMethodMenus("add")
    public Response add(IotRole entity, BindingResult result) {
        // 验证错误
        this.checkError(entity,result);
        entity.setDelFlag("0");
        IotRoleService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("{id}/update")
    @LogAspectj(logType = LogType.UPDATE)
    public Response update(IotRole entity, BindingResult result) {
        // 验证错误
        this.checkError(entity,result);
        entity.setDelFlag("0");
        IotRoleService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    //@RequiresMethodMenus("delete")
    public Response delete(@PathVariable("id") String id) {
        IotRoleService.deleteById(id);
        return Response.ok("删除成功");
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    //@RequiresMethodMenus("delete")
    public Response batchDelete(@RequestParam("ids") String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        IotRoleService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

  /*  *//**
     * 通过用户ID获得角色
     * @param uid
     * @return
     *//*
    @PostMapping(value = "{uid}/findListByUserId")
    public List<IotRole> findListByUserId(@PathVariable("uid") String uid) {
        try {
            return IotRoleService.findListByUserId(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping(value = "{IotRoleId}/menu")
    public void menu(@PathVariable("IotRoleId") String IotRoleId,
                     HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> dataMap = new HashMap<>();
        EntityWrapper<Menu> entityWrapper = new EntityWrapper<>(Menu.class);
        entityWrapper.setTableAlias("t.");
        //加入条件
        List<Menu> treeNodeList = menuService.selectTreeList(entityWrapper);
        List<VueTreeHelper.VueTreeNode> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        dataMap.put("menus", vueTreeNodes);
        // 获得选择的
        List<Menu> menuList = menuService.findMenuByIotRoleId(IotRoleId);
        List<String> menuIdList = new ArrayList<>();
        for (Menu menu:menuList) {
            menuIdList.add(menu.getId());
        }
        dataMap.put("selectMenuIds", menuIdList);
        String content = JSON.toJSONString(dataMap);
        ServletUtils.printJson(response, content);
    }

    @PostMapping(value = "/setMenu")
    @LogAspectj(logType = LogType.OTHER,title = "菜单授权")
    //@RequiresMethodMenus("authMenu")
    public Response setMenu(@RequestParam("IotRoleId") String IotRoleId,
                            @RequestParam("menuIds") String menuIds) {
        try {
            // 权限设置
            IotRoleMenuService.setMenu(IotRoleId,menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResponseError.NORMAL_ERROR,"保存失败!<br />原因:" + e.getMessage());
        }
        return Response.ok("保存成功");
    }*/
}
