package com.lmrj.cim.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.cim.modules.monitor.entity.LoginLog;
import com.lmrj.cim.modules.monitor.service.ILoginLogService;
import com.lmrj.cim.modules.sso.service.IOAuthService;
import com.lmrj.core.sys.entity.Role;
import com.lmrj.core.sys.entity.SysProject;
import com.lmrj.core.sys.entity.User;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.cim.modules.sys.service.ISysProjectService;
import com.lmrj.core.sys.service.IUserService;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.cim.utils.PageRequest;
import com.lmrj.cim.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.bbs.controller
 * @title: sys_project控制器
 * @description: sys_project控制器
 * @author: zwj
 * @date: 2019-05-28 07:13:47
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("sysProject")
@ViewPrefix("modules/SysProject")
@RequiresPathPermission("SysProject")
@LogAspectj(title = "sys_project")
public class SysProjectController extends BaseCRUDController<SysProject> {

    @Autowired
    private IOAuthService oAuthService;
    @Autowired
    private ISysProjectService sysProjectService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILoginLogService loginLogService;
    @GetMapping("indexFour")
    public void indexFour(HttpServletRequest request) throws ParseException {
        Page pageBean = oAuthService.activePrincipal(PageRequest.getPage());
        int count=sysProjectService.selectCount(new EntityWrapper<SysProject>());
        int userCount=userService.selectCount(new EntityWrapper<User>());
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        String time=dateFormat.format(date)+" 00:00:00";
        int loginCount =loginLogService.selectCount(new EntityWrapper<LoginLog>().ge("login_time",dateFormat.parse(time)));
        List<Map> mapList= Lists.newArrayList();
        Map map=new HashMap();
        map.put("name","在线用户数");
        map.put("number",pageBean.getTotal());
        Map map1=new HashMap();
        map1.put("name","总用户数");
        map1.put("number",userCount);
        Map map2=new HashMap();
        map2.put("name","今日登陆数");
        map2.put("number",loginCount);
        Map map3=new HashMap();
        map3.put("name","项目数");
        map3.put("number",count);
        //方案模版
        mapList.add(map);
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);
        FastJsonUtils.print(mapList);
    }

    @GetMapping("indexList")
    public void indexList(HttpServletRequest request) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>(User.class);
        //entityWrapper.orderBy("create_date",false);

        Page<User> page = new Page<User>(0, 10);
        page.setOrderByField("CREATE_DATE");
        page.setAsc(false);
        Wrapper<User> wrapper = new com.baomidou.mybatisplus.mapper.EntityWrapper<User>();

        Page<User> userList=userService.selectNewUser(page,wrapper);
        List<UserRole> roleList= UserUtils.getUserRoleList();
        List<Role> roles=UserUtils.getAllRoleList();
        List<Map> mapList=Lists.newArrayList();
        userList.getRecords().forEach(user -> {
            user.setOrgName(OfficeUtils.getOffice(user.getOrgid()).getName());
            roleList.forEach(userRole -> {
                if(user.getId().equals(userRole.getUserId())){
                    roles.forEach(role -> {
                        if(userRole.getRoleId().equals(role.getId())){
                            user.setRoleName(role.getName()+";"+user.getRoleName());
                            return;
                        }
                    });
                }
            });
            Map<String,String> map=new HashMap<>();
            map.put("orgName",user.getOrgName());
            map.put("roleName",user.getRoleName());
            map.put("realName",user.getRealname());
            map.put("createDate", DateUtil.formatDateTime(user.getCreateDate()));
            mapList.add(map);
        });
        //方案模版
        FastJsonUtils.print(mapList);
    }
    @GetMapping("projects")
    public void  getCurrentUserProject(){
        List<SysProject> sysProjectList=sysProjectService.selectList(new EntityWrapper<SysProject>().orderBy("SORT_NO"));
        List<String> projectIds=sysProjectService.filterProject(UserUtils.getUser().getId());
        List<SysProject> projectList=sysProjectList.stream().filter(sysProject -> projectIds.contains(sysProject.getProjectId())).collect(Collectors.toList());
        FastJsonUtils.print(projectList);
    }

    @RequestMapping(value = "/{projectId}/getProject", method = { RequestMethod.GET, RequestMethod.POST })
    public void ajax(Model model, @PathVariable("projectId") String projectId, HttpServletRequest request,
                     HttpServletResponse response) {
        List<SysProject> sysProjectList=sysProjectService.selectList(new EntityWrapper<SysProject>().eq("PROJECT_ID", projectId));
        SysProject entity = new SysProject();
        if(sysProjectList.size()>0){
            entity = sysProjectList.get(0);
        }
        String content = JSON.toJSONString(entity);
        ServletUtils.printJson(response,content);
    }

}
