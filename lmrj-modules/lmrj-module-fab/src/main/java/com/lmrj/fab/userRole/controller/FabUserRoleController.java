package com.lmrj.fab.userRole.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.fab.userRole.entity.FabUserRole;
import com.lmrj.fab.userRole.service.IFabUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wdj
 * @date 2021-06-05 10:39
 */
@RestController
@RequestMapping("fab/fabUserRole")
@ViewPrefix("fab/fabUserRole")
@RequiresPathPermission("fab:fabUserRole")
@LogAspectj(title = "fab_fabUserRole")
public class FabUserRoleController extends BaseCRUDController<FabUserRole> {
    @Autowired
    private IFabUserRoleService IotRoleUserService;


    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param userid
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "{userid}/roleIds")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public List<String> userRoleIds(@PathVariable("userid") String userid, HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        List<String> roleIdList=new ArrayList<String>();
        List<FabUserRole> IotRoleUserList=IotRoleUserService.selectList(new EntityWrapper<>(FabUserRole.class).eq("userId", userid));
        for (FabUserRole userRole: IotRoleUserList) {
            roleIdList.add(userRole.getRoleId());
        }
        return roleIdList;
    }

    /**
     * 新增关系
     * @param userId
     * @param roleIds
     * @return
     */
    @PostMapping("{userId}/insertByUserId")
    @LogAspectj(logType = LogType.INSERT)
    @RequiresMethodPermissions("add")
    public Response insertByUserId(@PathVariable("userId") String userId, @RequestParam("roleIds") String[] roleIds) {
        for (String roleId:roleIds) {
            FabUserRole userRole=new FabUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            IotRoleUserService.insertByRoleId(userId,roleId);
        }
     //   UserUtils.clearCache();
        return Response.ok("添加成功");
    }

    /**
     * 删除关系
     * @param userId
     * @param roleIds
     * @return
     */
    @PostMapping("{userId}/deleteByUserId")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response deleteByUserId(@PathVariable("userId") String userId, @RequestParam("roleIds") String roleIds) {
        EntityWrapper<FabUserRole> entityWrapper=new EntityWrapper<FabUserRole>(FabUserRole.class);
        entityWrapper.eq("userId",userId);
        entityWrapper.in("roleId",roleIds);
        IotRoleUserService.delete(entityWrapper);
    //    UserUtils.clearCache();
        return Response.ok("删除成功");
    }
}
