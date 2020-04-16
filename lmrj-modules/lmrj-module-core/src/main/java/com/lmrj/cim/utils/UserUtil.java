package com.lmrj.cim.utils;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.utils.CacheUtils;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.spring.SpringContext;
import com.lmrj.core.entity.BaseDataEntity;
import com.lmrj.core.sys.entity.Role;
import com.lmrj.core.sys.entity.User;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.core.sys.service.IMenuService;
import com.lmrj.core.sys.service.IRoleService;
import com.lmrj.core.sys.service.IUserRoleService;
import com.lmrj.core.sys.service.IUserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Qck
 */
@Slf4j
public class UserUtil {

    private static IUserService userService = SpringContext.getBean(IUserService.class);
    private static IRoleService roleService = SpringContext.getBean(IRoleService.class);
    private static IMenuService menuService = SpringContext.getBean(IMenuService.class);
    private static IUserRoleService userRoleService= SpringContext.getBean(IUserRoleService.class);
    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_USER_NAME_ = "username_";
    public static final String CACHE_ROLE_LIST_ = "roleList_";
    public static final String CACHE_PERMISSION_LIST_ = "permission_List_";
    public static final String CACHE_USER_ROLE_LIST_ = "user_role_List_";
    public static final String CACHE_ALL_ROLE_LIST_ = "roleAllList_";


    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static User getUser(String id) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user == null) {
            user = userService.selectById(id);
            if (user == null) {
                return null;
            }
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_USER_NAME_ + user.getUsername(), user);
        }
        return user;
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    public static User getByUserName(String username) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_USER_NAME_ + username);
        if (user == null) {
            user = userService.findByUsername(username);
            if (user == null) {
                return null;
            }
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_USER_NAME_ + user.getUsername(), user);
        }
        return user;
    }

    /**
     * 清除指定用户缓存
     *
     * @param user
     */
    public static void clearCache(User user) {
        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        CacheUtils.remove(USER_CACHE, USER_CACHE_USER_NAME_ + user.getUsername());
        CacheUtils.remove(USER_CACHE, CACHE_ROLE_LIST_ + user.getId());
    }

    /**
     * 获取用户角色列表
     *
     * @return
     */
    public static List<UserRole> getUserRoleList() {
        List<UserRole> roleList = (List<UserRole>) CacheUtils.get(CACHE_USER_ROLE_LIST_);
        if (roleList == null || roleList.size() == 0) {
            roleList = userRoleService.selectList(new EntityWrapper<>());
            // 不加入缓存
            CacheUtils.put(CACHE_USER_ROLE_LIST_,roleList);
        }
        return roleList;
    }
    /**
     * 获取角色列表
     *
     * @return
     */
    public static List<Role> getAllRoleList() {
        List<Role> roleList = (List<Role>) CacheUtils.get(CACHE_ALL_ROLE_LIST_);
        if (roleList == null || roleList.size() == 0) {
            roleList = roleService.selectList(new EntityWrapper<>());
            // 不加入缓存
            CacheUtils.put(CACHE_ALL_ROLE_LIST_,roleList);
        }
        return roleList;
    }
    //protected final static String SYS_USER = "sysUser";
    //
    //public static User getUser(String id) {
    //    //数据字典
    //    Map map=new HashMap();
    //    try {
    //         map=(Map) SpringContextHolder.getBean(RedisTemplate.class).opsForValue().get(SYS_USER);
    //    }catch (Exception e){
    //        log.error("redis取值失败{}",e.getMessage());
    //        initSysUser();
    //    }
    //    return  (User) map.get(id);
    //}
    //
    ///**
    // * 数据字典初始化
    // * @return
    // */
    //public static Map initSysUser() {
    //    List<User> userList = SpringContextHolder.getBean(IUserService.class).selectList(new EntityWrapper<>());
    //    Map map=new HashMap();
    //    userList.forEach(user -> {
    //        map.put(user.getId(),user);
    //    });
    //    try {
    //        SpringContextHolder.getBean(RedisTemplate.class).opsForValue().set(SYS_USER,map);
    //    }catch (Exception e){
    //        log.error("redis存用户信息失败{}"+e.getMessage());
    //    }
    //    return map;
    //}

    public static void updateUserName(BaseDataEntity entity){
        //创建人
        if(StringUtil.isNotBlank(entity.getCreateBy())){
            User creater = UserUtil.getUser(entity.getCreateBy());
            if(creater != null){
                entity.setCreateByName(creater.getUsername());
            }
        }
        //更新人
        if(StringUtil.isNotBlank(entity.getUpdateBy())){
            User updater = UserUtil.getUser(entity.getUpdateBy());
            if(updater != null){
                entity.setUpdateByName(updater.getUsername());
            }
        }
    }
}
