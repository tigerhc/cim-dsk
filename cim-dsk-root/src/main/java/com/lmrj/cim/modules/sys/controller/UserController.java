package com.lmrj.cim.modules.sys.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.lmrj.cim.modules.sys.service.IUserOrganizationService;
import com.lmrj.common.http.DuplicateValid;
import com.lmrj.common.http.ValidResponse;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.common.response.ResponseError;
import com.lmrj.core.sys.entity.*;
import com.lmrj.core.sys.service.*;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.cim.utils.PageRequest;
import com.lmrj.cim.utils.UserUtils;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.lang.StringUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.sys.controller
 * @title: ?????????????????????
 * @description: ?????????????????????
 * @author: ??????
 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/sys/user")
@ViewPrefix("modules/sys/user")
@RequiresPathPermission("sys:user")
@LogAspectj(title = "????????????")
public class UserController extends BaseBeanController<User> {
	@Autowired
	private IUserService userService;

	@Autowired
	private IUserRoleService userRoleService;

	@Autowired
	private IRoleService roleService;
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private IUserOrganizationService userOrganizationService;

	/**
	 * ?????????????????????????????????????????????????????????????????????
	 *
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	@LogAspectj(logType = LogType.SELECT)
	@RequiresMethodPermissions("list")
	public void list(HttpServletRequest request) throws IOException {
		EntityWrapper<User> entityWrapper = new EntityWrapper<>(entityClass);
		String realname= request.getParameter("realname");
		if (!StringUtil.isEmpty(realname)){
			entityWrapper.like("realname",realname);
		}
		String username= request.getParameter("username");
		if (!StringUtil.isEmpty(username)){
			entityWrapper.like("username",username);
		}
		String phone= request.getParameter("phone");
		if (!StringUtil.isEmpty(phone)){
			entityWrapper.like("phone",phone);
		}
		String orgid= request.getParameter("orgid");
		if (!StringUtil.isEmpty(orgid)){
			List<String> orgIds=Lists.newArrayList();
			orgIds=userOrganizationService.listOrgIds(orgid);
			if(CollectionUtils.isEmpty(orgIds)){
				orgIds=Lists.newArrayList();
			}
			orgIds.add(orgid);
			entityWrapper.in("orgid",orgIds);
		}
		// ?????????
		Page pageBean = userService.selectPage(PageRequest.getPage(),entityWrapper);
		if(CollectionUtils.isNotEmpty(pageBean.getRecords())){
            for(User  user:(List<User>)pageBean.getRecords()){
                Organization office = OfficeUtils.getOffice(user.getOrgid());
                if(office != null){
                    String str=office.getParentIds().replaceAll("/",",")+user.getOrgid();
                    user.setOrgids(str.split(","));
                }
            }
        }
		FastJsonUtils.print(pageBean);
	}

	@PostMapping("add")
	@LogAspectj(logType = LogType.INSERT)
	@RequiresMethodPermissions("add")
	public Response add(User entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		// ????????????
		this.checkError(entity,result);
		userService.insert(entity);
		//????????????
		afterSave(entity,request);
		return Response.ok("????????????");
	}

	@GetMapping(value = "{id}/update")
	public ModelAndView update(@PathVariable("id") String id, Model model, HttpServletRequest request,
							   HttpServletResponse response) {
		User user = userService.selectById(id);
		model.addAttribute("data", user);
		// ?????????????????????List
		List<Role> allRoles = roleService.selectList(new EntityWrapper<Role>());
		request.setAttribute("allRoles", allRoles);
		if (!StringUtil.isEmpty(user.getId())) {
			// ??????????????????
			List<Role> userRoles = roleService.findListByUserId(user.getId());
			List<String> roleIdList = new ArrayList<>();
			for (Role role: userRoles) {
				roleIdList.add(role.getId());
			}
			request.setAttribute("roleIdList", roleIdList);
			List<Organization> organizations = organizationService.findListByUserId(user.getId());
			String organizationIds = "";
			String organizationNames = "";
			for (Organization organization : organizations) {
				if (!StringUtil.isEmpty(organizationIds)) {
					organizationIds += ",";
					organizationNames += ",";
				}
				String organizationId = organization.getId();
				organizationIds += organizationId;
				organizationNames += organization.getName();

			}
			request.setAttribute("organizationIds", organizationIds);
			request.setAttribute("organizationNames", organizationNames);
		}

		return displayModelAndView ("edit");
	}

	@PostMapping("{id}/update")
	@LogAspectj(logType = LogType.UPDATE)
	@RequiresMethodPermissions("update")
	public Response update(User entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		// ????????????
		this.checkError(entity,result);
		userService.insertOrUpdate(entity);
		//????????????
		afterSave(entity,request);
		return Response.ok("????????????");
	}

	@PostMapping("{id}/delete")
	@LogAspectj(logType = LogType.DELETE)
	@RequiresMethodPermissions("delete")
	public Response delete(@PathVariable("id") String id) {
		userService.deleteById(id);
		return Response.ok("????????????");
	}

	@PostMapping("batch/delete")
	@LogAspectj(logType = LogType.DELETE)
	@RequiresMethodPermissions("delete")
	public Response batchDelete(@RequestParam("ids") String[] ids) {
		List<String> idList = java.util.Arrays.asList(ids);
		userService.deleteBatchIds(idList);
		return Response.ok("????????????");
	}

	@PostMapping(value = "{id}/changePassword")
	@LogAspectj(logType = LogType.OTHER,title = "????????????")
	@RequiresMethodPermissions("change:password")
	public Response changePassword(@PathVariable("id") String id, HttpServletRequest request,
								   HttpServletResponse response) {
		String password = request.getParameter("password");
		userService.changePassword(id, password);
		return Response.ok("??????????????????");
	}

	@GetMapping(value = "{id}/avatar")
	public ModelAndView avatar(@PathVariable("id") String id, Model model, HttpServletRequest request,
						 HttpServletResponse response) {
		User user = userService.selectById(id);
		model.addAttribute("data", user);
		return displayModelAndView("avatar");
	}

	@PostMapping(value = "{id}/avatar")
	@LogAspectj(logType = LogType.OTHER,title = "????????????")
	@RequiresMethodPermissions("avatar")
	public Response avatar(User user, HttpServletRequest request, HttpServletResponse response) {
		try {
			User oldUser = userService.selectById(user.getId());
			BeanUtils.copyProperties(user,oldUser);
			userService.insertOrUpdate(oldUser);
			String currentUserId = UserUtils.getUser().getId();
			if (currentUserId.equals(user.getId())) {
				UserUtils.clearCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error("??????????????????");
		}
		return Response.ok("??????????????????");
	}

	public void afterSave(User entity, HttpServletRequest request) {
		// ??????????????????
		String[] roleIdList = request.getParameterValues("roleIdList");
		if (roleIdList != null && roleIdList.length > 0) {
			userRoleService.delete(new EntityWrapper<UserRole>(UserRole.class).eq("userId", entity.getId()));
			List<UserRole> userRoleList = new ArrayList<UserRole>();
			for (String roleid : roleIdList) {
				UserRole userRole = new UserRole();
				userRole.setUserId(entity.getId());
				userRole.setRoleId(roleid);
				userRoleList.add(userRole);
			}
			userRoleService.insertBatch(userRoleList,1000);
		}

		// ??????????????????
		String organizationIdListStr = request.getParameter("organizationIds");
		if (!StringUtil.isEmpty(organizationIdListStr)) {
			String[] organizationIdList = organizationIdListStr.split(",");
			if (organizationIdList != null && organizationIdList.length > 0) {
				userOrganizationService.delete(new EntityWrapper<UserOrganization>(UserOrganization.class).eq("userId", entity.getId()));
				List<UserOrganization> userOrganizationList = new ArrayList<UserOrganization>();
				for (String organizationId : organizationIdList) {
					UserOrganization userOrganization = new UserOrganization();
					userOrganization.setUserId(entity.getId());
					userOrganization.setOrganizationId(organizationId);
					userOrganizationList.add(userOrganization);
				}
				userOrganizationService.insertBatch(userOrganizationList,1000);
			}
		}
	}

	//@GetMapping("export")
	@LogAspectj(logType = LogType.EXPORT)
	@RequiresMethodPermissions("export")
	public Response export(HttpServletRequest request) {
		Response response = Response.ok("????????????");
		try {
			TemplateExportParams params = new TemplateExportParams(
					"");
			//????????????
			EntityWrapper<User> entityWrapper = new EntityWrapper<>(User.class);
			// ?????????
			String organizationid = request.getParameter("organizationid");
			if (!StringUtil.isEmpty(organizationid)) {
				entityWrapper.eq("uo.organization_id", organizationid);
			}
			Page pageBean = userService.selectPage(PageRequest.getPage(),entityWrapper);
			String title = "????????????";
			Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
					title, title, ExcelType.XSSF), User.class, pageBean.getRecords());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			book.write(bos);
			byte[] bytes = bos.toByteArray();
			String bytesRes = StringUtil.bytesToHexString2(bytes);
			title = title+ "-" + DateUtil.getDateTime();
			response.put("bytes",bytesRes);
			response.put("title",title);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(ResponseError.NORMAL_ERROR,"????????????");
		}
		return response;
	}


	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public Response upload(MultipartFile file) {
		Response response = Response.ok("????????????");
		if (file == null) {
			return Response.error(ResponseError.NORMAL_ERROR,"?????????????????????????????????");
		}
		List<User> list = Lists.newArrayList();
	    String fileName = file.getOriginalFilename(); //???????????????
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
			// ????????????sheet
			int sheets = workbook.getNumberOfSheets();
			for (int i = 0; i < sheets; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				// ???????????????
				int rows = sheet.getPhysicalNumberOfRows();
				User budgetadjust = null;
				// ?????????????????????????????? 0 ????????????
				for (int j = 1; j < rows; j++) {
					budgetadjust = new User();
					// ????????? j ???
					HSSFRow row = sheet.getRow(j);
					budgetadjust.setId(row.getCell(0).toString());// ????????????
					list.add(budgetadjust);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.error(ResponseError.NORMAL_ERROR,"????????????");
		}
		return response;
	}

	@PostMapping(value = "validate")
	public ValidResponse validate(DuplicateValid duplicateValid, HttpServletRequest request) {
		ValidResponse validResponse = new ValidResponse();
		Boolean valid = Boolean.FALSE;
		try {
			EntityWrapper<User> entityWrapper = new EntityWrapper<User>(entityClass);
			valid = userService.doValid(duplicateValid,entityWrapper);
			if (valid) {
				validResponse.setStatus("y");
				validResponse.setInfo("????????????!");
			} else {
				validResponse.setStatus("n");
				if (!StringUtil.isEmpty(duplicateValid.getErrorMsg())) {
					validResponse.setInfo(duplicateValid.getErrorMsg());
				} else {
					validResponse.setInfo("??????????????????!");
				}
			}
		} catch (Exception e) {
			validResponse.setStatus("n");
			validResponse.setInfo("??????????????????????????????????????????!");
		}
		return validResponse;
	}

	/**
	 * ??????????????????
	 * @return
	 */
	@GetMapping(value = "info")
	public Response info() {
		Response response = Response.ok();
		User user= UserUtils.getUser();
		if (user==null){
			return Response.error("????????????");
		}else{
			response.putObject(user,"id,username,realname,phone,portrait,email,status");
			//????????????
			//response.put("roles",UserUtils.getRoleStringList());
			List<String> roles = new ArrayList<>();
			roles.add("admin");
			response.put("roles",roles);
		}
		return response;
	}

	/**
	 * ??????????????????
	 *
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping("my/update")
	@LogAspectj(logType = LogType.UPDATE, title = "????????????")
	public Response myUpdate(User user,HttpServletRequest request) {
		String userId = UserUtils.getUser().getId();
		User oldUser =  userService.selectById(userId);
		// ????????????
		BeanUtils.copyProperties(user,oldUser);
		userService.insertOrUpdate(oldUser);
		return Response.ok("????????????");
	}

	/**
	 * ??????????????????
	 *
	 * @param oldPassword
	 * @param password
	 * @param request
	 * @return
	 */
	@PostMapping("my/changePassword")
	@LogAspectj(logType = LogType.OTHER, title = "??????????????????")
	public Response myChangePassword(String oldPassword,
									 String password,HttpServletRequest request) {
		String userId = UserUtils.getUser().getId();
		if (userService.checkPassword(userId,oldPassword)){
			userService.changePassword(userId,password);
		}else{
			return Response.error("???????????????");
		}
		return Response.ok("??????????????????");
	}
}
