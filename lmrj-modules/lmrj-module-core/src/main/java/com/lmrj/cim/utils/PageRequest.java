package com.lmrj.cim.utils;

import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.util.lang.StringUtil;

/**
 * All rights Reserved, Designed By www.dataact.cn
 *
 * @version V1.0
 * @package com.lmrj.web.utils
 * @title:
 * @description: 分页
 * @author: 张飞
 * @date: 2018/10/19 12:17
 * @copyright: 2017 www.dataact.cn Inc. All rights reserved.
 */
public class PageRequest {
    public static Page getPage(){
        String page = ServletUtils.getRequest().getParameter("page");
        String limit = ServletUtils.getRequest().getParameter("limit");
        Integer pageInt = 1;
        Integer limitInt = 10;
        if (!StringUtil.isEmpty(page)){
            pageInt = Integer.parseInt(page);
        }
        if (!StringUtil.isEmpty(limit)){
            limitInt = Integer.parseInt(limit);
        }
        Page pageBean = new Page(pageInt,limitInt);
        return pageBean;
    }
}
