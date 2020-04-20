package com.lmrj.cim.modules.sys.controller;

import com.lmrj.cim.common.helper.VueTreeHelper;
import com.lmrj.cim.utils.MenuTreeHelper;
import com.lmrj.cim.utils.UserUtils;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.core.sys.entity.Menu;
import com.lmrj.core.sys.service.IMenuService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/sys/menu")
@ViewPrefix("modules/sys/menu")
@RequiresPathPermission("sys:menu")
@LogAspectj(title = "菜单管理")
public class MenuController extends BaseBeanController<Menu> {

    @Autowired
    private IMenuService menuService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void list(HttpServletRequest request) throws IOException {
        EntityWrapper<Menu> entityWrapper = new EntityWrapper<Menu>(entityClass);
        entityWrapper.setTableAlias("t");
        //加入条件
        String keyword = request.getParameter("keyword");
        if (!StringUtil.isEmpty(keyword)){
            entityWrapper.like("name",keyword);
        }
        String projectId= request.getParameter("projectId");
        if (!StringUtil.isEmpty(projectId)){
            entityWrapper.eq("project_id",projectId);
        }
        entityWrapper.orderBy("sort");
        List<Menu> treeNodeList = menuService.selectTreeList(entityWrapper);
        List<VueTreeHelper.VueTreeNode> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        FastJsonUtils.print(vueTreeNodes);
    }

    @PostMapping("add")
    @LogAspectj(logType = LogType.INSERT)
    @RequiresMethodPermissions("add")
    public Response add(Menu entity, BindingResult result,
                        HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity,result);
        menuService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("{id}/update")
    @LogAspectj(logType = LogType.UPDATE)
    @RequiresMethodPermissions("update")
    public Response update(Menu entity, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity,result);
        menuService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response delete(@PathVariable("id") String id) {
        menuService.deleteById(id);
        return Response.ok("删除成功");
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response batchDelete(@RequestParam("ids") String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        menuService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    /**
     * 获得菜单列表
     *
     * @throws IOException
     */
    @GetMapping(value = "getMenusById")
    public void getMenusById(HttpServletRequest request){
        String id = request.getParameter("id");
        List<Menu> treeNodeList = menuService.findMenuByUserIdAndNodeId(UserUtils.getUser().getId(),id);
        List<MenuTreeHelper.MenuTreeNode> menuTreeNodes = MenuTreeHelper.create().sort(treeNodeList);
        FastJsonUtils.print(menuTreeNodes);
    }

    /**
     * 获得菜单列表
     *
     * @throws IOException
     */
    @GetMapping(value = "getMenus")
    public void getMenus(){
        List<Menu> treeNodeList = menuService.findMenuByUserId(UserUtils.getUser().getId());
        List<MenuTreeHelper.MenuTreeNode> menuTreeNodes = MenuTreeHelper.create().sort(treeNodeList);
        FastJsonUtils.print(menuTreeNodes);
    }

    @PostMapping("{id}/changeSort")
    public Response changeSort(@PathVariable("id") String id,
                                     @RequestParam ("sort") Integer sort) {
        menuService.changeSort(id, sort);
        return  Response.ok("排序成功");
    }

    /**
     * 获得菜单列表
     *
     * @throws IOException
     */
    @GetMapping(value = "getPermissions")
    public void getPermissions(){
        //加入条件
        String uid = UserUtils.getUser().getId();
        List<String> permissionValueList = menuService.findPermissionByUserId(uid);
        FastJsonUtils.print(permissionValueList);
    }

    @PostMapping("{id}/generate/button")
    @LogAspectj(logType = LogType.OTHER, title = "生成按钮")
    @RequiresMethodPermissions("generate:button")
    public Response generateButton(@PathVariable("id") String id,
                                   @RequestParam("parentPermission") String parentPermission,
                                   @RequestParam("permissions") String permissions,
                                   @RequestParam("permissionTitles") String permissionTitles) {
        menuService.generateButton(id, parentPermission, permissions.split(","), permissionTitles.split(","));
        return Response.ok("生成成功");
    }
}
