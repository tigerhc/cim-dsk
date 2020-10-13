package com.lmrj.dsk.dashboard.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.dsk.dashboard.entity.FbpbistolO;
import com.lmrj.dsk.dashboard.entity.FipinqtoolO;
import com.lmrj.dsk.dashboard.entity.ToolGroupInfo;
import com.lmrj.dsk.dashboard.service.IDashboardService;

import com.lmrj.util.mapper.JsonUtil;
import org.apache.commons.collections.MapUtils;
import com.lmrj.util.lang.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @author: zhangweijiang
 * @date: 2020-02-15 02:39:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    IDashboardService dashboardServiceImpl;
    // 获取组织机构信息
    //{"trx_id":"FBPBISDAT","action_flg":"Q","iary":[{"data_cate":"TOLG","DATA_ITEM":"101A"}]}
    @RequestMapping("/q1-bak")
    public void getToolGroupInfo(String fab_id_fk ,HttpServletRequest request, HttpServletResponse response) {
        ToolGroupInfo toolGroupInfo = dashboardServiceImpl.findOrgGroupInfo(fab_id_fk);
        ServletUtils.printJson(response, toolGroupInfo);
    }

    //@RequestMapping("/q1")
    //public String getToolGroupInfo2() {
    //    return "{\"oary\":[{\"data_cate\":\"TOLG\",\"data_desc\":\"SIM\",\"data_ext\":\"$TFG\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"8DNJ62ARJS384IU4VVN92NLQG\",\"ext_1\":\"返工间设备组\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"5GI\",\"data_ext\":\"$TJG\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"AHO93HJ3XE2PVJQHPSRAG6LG6\",\"ext_1\":\"激光组\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"6GI\",\"data_ext\":\"$TZDH\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"BD4WHOJ0QZFN8RXJNZK1P1M8K\",\"ext_1\":\"ALD自动化\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"SX\",\"data_ext\":\"$TZR\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"C1LJ5IHM0K5W5H32V0N7NONVH\",\"ext_1\":\"印刷机\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"SMA\",\"data_ext\":\"$TLID\",\"data_id\":\"10\",\"data_item\":\"101A\",\"data_seq_id\":\"F48VH2IIER0D42GR0AEPIXWX9\",\"ext_1\":\"LID组\"}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"success\",\"tbl_cnt\":13,\"telephone\":\"\",\"trx_id\":\"FBPBISDAT\",\"type_id\":\"O\"}";
    //}

    @RequestMapping("/q1")
    public String getToolGroupInfo2() {
        return "{\"oary\":[{\"data_cate\":\"TOLG\",\"data_desc\":\"返工间设备组\",\"data_ext\":\"$TFG\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"8DNJ62ARJS384IU4VVN92NLQG\",\"ext_1\":\"返工间设备组\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"激光组\",\"data_ext\":\"$TJG\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"AHO93HJ3XE2PVJQHPSRAG6LG6\",\"ext_1\":\"激光组\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"ALD自动化组\",\"data_ext\":\"$TZDH\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"BD4WHOJ0QZFN8RXJNZK1P1M8K\",\"ext_1\":\"ALD自动化\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"制绒设备组\",\"data_ext\":\"$TZR\",\"data_id\":\"1\",\"data_item\":\"101A\",\"data_seq_id\":\"C1LJ5IHM0K5W5H32V0N7NONVH\",\"ext_1\":\"印刷机\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"LID组\",\"data_ext\":\"$TLID\",\"data_id\":\"10\",\"data_item\":\"101A\",\"data_seq_id\":\"F48VH2IIER0D42GR0AEPIXWX9\",\"ext_1\":\"LID组\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"101A包装\",\"data_ext\":\"$PACK\",\"data_id\":\"14\",\"data_item\":\"101A\",\"data_seq_id\":\"CBKXTE1O9ABBPIK9FKIX6B32B\",\"ext_1\":\"101A包装\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"EL测试机\",\"data_ext\":\"ELCSJ\",\"data_id\":\"15\",\"data_item\":\"101A\",\"data_seq_id\":\"BRNUB9F5E14EFYDTEOW6VNJAR\",\"ext_1\":\"EL测试机\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"扩散设备组\",\"data_ext\":\"$TKS\",\"data_id\":\"2\",\"data_item\":\"101A\",\"data_seq_id\":\"43AU41NQU3N2F3P8AQSSO8AFJ\",\"ext_1\":\"印刷后画像\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"刻蚀设备组\",\"data_ext\":\"$TKES\",\"data_id\":\"3\",\"data_item\":\"101A\",\"data_seq_id\":\"DSOUFQ4NXR13QNC4ICILSA6H9\",\"ext_1\":\"DM\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"ALD组\",\"data_ext\":\"$TALD\",\"data_id\":\"4\",\"data_item\":\"101A\",\"data_seq_id\":\"3QBQBGTTUV50AEYX73FM1D7XG\",\"ext_1\":\"ALD组\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"PECVD设备组\",\"data_ext\":\"$TPECVD\",\"data_id\":\"5\",\"data_item\":\"101A\",\"data_seq_id\":\"8VRHLVXAI140UWELHDAF5OWCJ\",\"ext_1\":\"REFLOW\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"丝网印刷设备组\",\"data_ext\":\"$TSWYS\",\"data_id\":\"6\",\"data_item\":\"101A\",\"data_seq_id\":\"I4ZLFCKQKVUHKNHJXHEAVUTE\",\"ext_1\":\"画像\"},{\"data_cate\":\"TOLG\",\"data_desc\":\"测试分选组\",\"data_ext\":\"$TCSFX\",\"data_id\":\"8\",\"data_item\":\"101A\",\"data_seq_id\":\"2MBAFEH3S244VTT39627HYLIP\",\"ext_1\":\"测试分选组\"}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"success\",\"tbl_cnt\":13,\"telephone\":\"\",\"trx_id\":\"FBPBISDAT\",\"type_id\":\"O\"}";
    }

    //获取设备状态
    @RequestMapping("/q2-1")
    public void setRightTopData(String fab_id_fk, HttpServletRequest request, HttpServletResponse response) {
        FipinqtoolO fipinqtoolO =  dashboardServiceImpl.findEqpStateByFab(fab_id_fk);
        ServletUtils.printJson(response, fipinqtoolO);
    }

    @RequestMapping("/q2-jn")
    public String setRightTopData2(HttpServletRequest request, HttpServletResponse response) {
        return "{\"oaryB1\":[{\"bay_id\":\"101A-1\",\"bay_name\":\"101车间1线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS01-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-1\",\"bay_name\":\"101车间1线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS01-BG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-2\",\"bay_name\":\"101车间2线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS02-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-2\",\"bay_name\":\"101车间2线\",\"second_time\":2176,\"time\":36,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS02-BG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-3\",\"bay_name\":\"101车间3线\",\"second_time\":3780,\"time\":63,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS03-AG\",\"tool_stat\":\"PM\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-3\",\"bay_name\":\"101车间3线\",\"second_time\":186,\"time\":3,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS03-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-7\",\"bay_name\":\"101车间7线\",\"second_time\":3960,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS07-AG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-7\",\"bay_name\":\"101车间7线\",\"second_time\":9,\"time\":0,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS07-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-7\",\"bay_name\":\"101车间7线\",\"second_time\":3300,\"time\":55,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS07-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-7\",\"bay_name\":\"101车间7线\",\"second_time\":687,\"time\":11,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS07-BG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-9\",\"bay_name\":\"101车间9线\",\"second_time\":1841,\"time\":42,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS09-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-9\",\"bay_name\":\"101车间9线\",\"second_time\":524,\"time\":8,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS09-BG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-10\",\"bay_name\":\"101车间10线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS10-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-10\",\"bay_name\":\"101车间10线\",\"second_time\":3960,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS10-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-10\",\"bay_name\":\"101车间10线\",\"second_time\":21,\"time\":0,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS10-BG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-2\",\"bay_name\":\"101车间2线\",\"second_time\":1221,\"time\":30,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS02-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-3\",\"bay_name\":\"101车间3线\",\"second_time\":3780,\"time\":63,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS03-BG\",\"tool_stat\":\"PM\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-3\",\"bay_name\":\"101车间3线\",\"second_time\":186,\"time\":3,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS03-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-4\",\"bay_name\":\"101车间4线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS04-AG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-4\",\"bay_name\":\"101车间4线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS04-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-5\",\"bay_name\":\"101车间5线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS05-AG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-5\",\"bay_name\":\"101车间5线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS05-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-6\",\"bay_name\":\"101车间6线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS06-AG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-6\",\"bay_name\":\"101车间6线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS06-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-8\",\"bay_name\":\"101车间8线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS08-AG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-8\",\"bay_name\":\"101车间8线\",\"second_time\":0,\"time\":66,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS08-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-9\",\"bay_name\":\"101车间9线\",\"second_time\":1435,\"time\":23,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS09-AG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20},{\"bay_id\":\"101A-9\",\"bay_name\":\"101车间9线\",\"second_time\":3224,\"time\":57,\"timeToal\":66,\"toolIds\":20,\"tool_id_fk\":\"SWYS09-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":20}],\"oaryC\":[{\"time\":364,\"tool_stat\":\"DOWN\",\"totalTime\":1320},{\"time\":828,\"tool_stat\":\"RUN\",\"totalTime\":1320},{\"time\":126,\"tool_stat\":\"PM\",\"totalTime\":1320}],\"oaryE\":[{\"bay_id\":\"101A-7\",\"bay_index\":\"07\",\"bay_name\":\"101车间7线\",\"run_time\":121,\"toolg_id\":\"101A-7\",\"total_time\":132},{\"bay_id\":\"101A-6\",\"bay_index\":\"06\",\"bay_name\":\"101车间6线\",\"run_time\":132,\"toolg_id\":\"101A-6\",\"total_time\":132},{\"bay_id\":\"101A-9\",\"bay_index\":\"09\",\"bay_name\":\"101车间9线\",\"run_time\":80,\"toolg_id\":\"101A-9\",\"total_time\":132},{\"bay_id\":\"101A-8\",\"bay_index\":\"08\",\"bay_name\":\"101车间8线\",\"run_time\":132,\"toolg_id\":\"101A-8\",\"total_time\":132},{\"bay_id\":\"101A-10\",\"bay_index\":\"10\",\"bay_name\":\"101车间10线\",\"run_time\":66,\"toolg_id\":\"101A-10\",\"total_time\":132},{\"bay_id\":\"101A-1\",\"bay_index\":\"01\",\"bay_name\":\"101车间1线\",\"run_time\":0,\"toolg_id\":\"101A-1\",\"total_time\":132},{\"bay_id\":\"101A-3\",\"bay_index\":\"03\",\"bay_name\":\"101车间3线\",\"run_time\":3,\"toolg_id\":\"101A-3\",\"total_time\":132},{\"bay_id\":\"101A-2\",\"bay_index\":\"02\",\"bay_name\":\"101车间2线\",\"run_time\":30,\"toolg_id\":\"101A-2\",\"total_time\":132},{\"bay_id\":\"101A-5\",\"bay_index\":\"05\",\"bay_name\":\"101车间5线\",\"run_time\":132,\"toolg_id\":\"101A-5\",\"total_time\":132},{\"bay_id\":\"101A-4\",\"bay_index\":\"04\",\"bay_name\":\"101车间4线\",\"run_time\":132,\"toolg_id\":\"101A-4\",\"total_time\":132}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"SUCCESS\",\"trx_id\":\"FIPINQTOL\",\"type_id\":\"O\"}";
    }

    /**
     * 左上脚-1
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/q2")
    public String setRightTopData(HttpServletRequest request, HttpServletResponse response,@RequestParam String fab_id_fk) {
//        return "{\n" +
//                "    \"oaryB1\": [\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-1\",\n" +
//                "            \"bay_name\": \"SMA\",\n" +
//                "            \"second_time\": 0,\n" +
//                "            \"time\": 66,\n" +
//                "            \"timeToal\": 66,\n" +
//                "            \"toolIds\": 20,\n" +
//                "            \"tool_id_fk\": \"SWYS01-AG\",\n" +
//                "            \"tool_stat\": \"DOWN\",\n" +
//                "            \"toolg_id\": \"$TSWYS\",\n" +
//                "            \"wholeToolIds\": 20\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-1\",\n" +
//                "            \"bay_name\": \"SX\",\n" +
//                "            \"second_time\": 0,\n" +
//                "            \"time\": 66,\n" +
//                "            \"timeToal\": 66,\n" +
//                "            \"toolIds\": 20,\n" +
//                "            \"tool_id_fk\": \"SWYS01-BG\",\n" +
//                "            \"tool_stat\": \"DOWN\",\n" +
//                "            \"toolg_id\": \"$TSWYS\",\n" +
//                "            \"wholeToolIds\": 20\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-2\",\n" +
//                "            \"bay_name\": \"SIM\",\n" +
//                "            \"second_time\": 0,\n" +
//                "            \"time\": 66,\n" +
//                "            \"timeToal\": 66,\n" +
//                "            \"toolIds\": 20,\n" +
//                "            \"tool_id_fk\": \"SWYS02-AG\",\n" +
//                "            \"tool_stat\": \"DOWN\",\n" +
//                "            \"toolg_id\": \"$TSWYS\",\n" +
//                "            \"wholeToolIds\": 20\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-2\",\n" +
//                "            \"bay_name\": \"6GI\",\n" +
//                "            \"second_time\": 2176,\n" +
//                "            \"time\": 36,\n" +
//                "            \"timeToal\": 66,\n" +
//                "            \"toolIds\": 20,\n" +
//                "            \"tool_id_fk\": \"SWYS02-BG\",\n" +
//                "            \"tool_stat\": \"DOWN\",\n" +
//                "            \"toolg_id\": \"$TSWYS\",\n" +
//                "            \"wholeToolIds\": 20\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-3\",\n" +
//                "            \"bay_name\": \"5GI\",\n" +
//                "            \"second_time\": 3780,\n" +
//                "            \"time\": 63,\n" +
//                "            \"timeToal\": 66,\n" +
//                "            \"toolIds\": 20,\n" +
//                "            \"tool_id_fk\": \"SWYS03-AG\",\n" +
//                "            \"tool_stat\": \"PM\",\n" +
//                "            \"toolg_id\": \"$TSWYS\",\n" +
//                "            \"wholeToolIds\": 20\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"oaryC\": [\n" +
//                "        {\n" +
//                "            \"time\": 364,\n" +
//                "            \"tool_stat\": \"DOWN\",\n" +
//                "            \"totalTime\": 1320\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"time\": 828,\n" +
//                "            \"tool_stat\": \"RUN\",\n" +
//                "            \"totalTime\": 1320\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"time\": 126,\n" +
//                "            \"tool_stat\": \"PM\",\n" +
//                "            \"totalTime\": 1320\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"oaryE\": [\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-7\",\n" +
//                "            \"bay_index\": \"07\",\n" +
//                "            \"bay_name\": \"SMA\",\n" +
//                "            \"run_time\": 121,\n" +
//                "            \"toolg_id\": \"101A-7\",\n" +
//                "            \"total_time\": 132\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-6\",\n" +
//                "            \"bay_index\": \"06\",\n" +
//                "            \"bay_name\": \"SX\",\n" +
//                "            \"run_time\": 132,\n" +
//                "            \"toolg_id\": \"101A-6\",\n" +
//                "            \"total_time\": 132\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-9\",\n" +
//                "            \"bay_index\": \"09\",\n" +
//                "            \"bay_name\": \"SIM\",\n" +
//                "            \"run_time\": 80,\n" +
//                "            \"toolg_id\": \"101A-9\",\n" +
//                "            \"total_time\": 132\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-8\",\n" +
//                "            \"bay_index\": \"08\",\n" +
//                "            \"bay_name\": \"6GI\",\n" +
//                "            \"run_time\": 132,\n" +
//                "            \"toolg_id\": \"101A-8\",\n" +
//                "            \"total_time\": 132\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bay_id\": \"101A-10\",\n" +
//                "            \"bay_index\": \"10\",\n" +
//                "            \"bay_name\": \"5GI\",\n" +
//                "            \"run_time\": 66,\n" +
//                "            \"toolg_id\": \"101A-10\",\n" +
//                "            \"total_time\": 132\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"rtn_code\": \"0000000\",\n" +
//                "    \"rtn_mesg\": \"SUCCESS\",\n" +
//                "    \"trx_id\": \"FIPINQTOL\",\n" +
//                "    \"type_id\": \"O\"\n" +
//                "}";
        Map<String, Object> map = new HashMap<>();
        map.put("rtn_code","0000000");
        map.put("rtn_mesg","SUCCESS");
        map.put("trx_id","FIPINQTOL");
        map.put("type_id","O");
        List<Map> datas = dashboardServiceImpl.findCurStateByPeriod(fab_id_fk);
        if(datas!=null && datas.size()>0){
            List<Map> res = new ArrayList<>();
            for(Map<String, Object> item : datas){
                Map<String, Object> downObj = new HashMap<>();
                downObj.put("time", MapUtils.getIntValue(item, "downTime"));
                downObj.put("tool_stat", "DOWN");
                downObj.put("totalTime", MapUtils.getIntValue(item, "totalTime"));
                res.add(downObj);
                Map<String, Object> runObj = new HashMap<>();
                runObj.put("time", MapUtils.getIntValue(item, "runTime"));
                runObj.put("tool_stat", "RUN");
                runObj.put("totalTime", MapUtils.getIntValue(item, "totalTime"));
                res.add(runObj);
                Map<String, Object> pmObj = new HashMap<>();
                pmObj.put("time", MapUtils.getIntValue(item, "pmTime"));
                pmObj.put("tool_stat", "PM");
                pmObj.put("totalTime", MapUtils.getIntValue(item, "totalTime"));
                res.add(pmObj);
                Map<String, Object> idleObj = new HashMap<>();
                idleObj.put("time", MapUtils.getIntValue(item, "idleTime"));
                idleObj.put("tool_stat", "IDLE");
                idleObj.put("totalTime", MapUtils.getIntValue(item, "totalTime"));
                res.add(idleObj);
                Map<String, Object> otherObj = new HashMap<>();
                otherObj.put("time", MapUtils.getIntValue(item, "otherTime"));
                otherObj.put("tool_stat", "OTHER");
                otherObj.put("totalTime", MapUtils.getIntValue(item, "totalTime"));
                res.add(idleObj);
            }
            map.put("oaryC", res);

        }else{
            map.put("oaryC",new ArrayList<>());
        }
        List<Map> oaryE = dashboardServiceImpl.findSIMState();
        List<Map> oaryERes = new ArrayList<>();
        if(oaryE!=null && oaryE.size()>0){
            for(Map<String, Object> item : oaryE){
                Map<String, Object> simObj = new HashMap<>();
                simObj.put("bay_id", "101A-9");
                simObj.put("bay_index", "09");
                simObj.put("bay_name", "SIM");
                simObj.put("run_time", MapUtils.getString(item, "runTime"));
                simObj.put("toolg_id", "101A-9");
                simObj.put("total_time", MapUtils.getIntValue(item, "totalTime"));
                oaryERes.add(simObj);
            }
            Map<String, Object> SMAObj = new HashMap<>();
            SMAObj.put("bay_id", "101A-7");
            SMAObj.put("bay_index", "07");
            SMAObj.put("bay_name", "SMA");
            SMAObj.put("run_time", 121);
            SMAObj.put("toolg_id", "101A-9");
            SMAObj.put("total_time", 132);
            oaryERes.add(SMAObj);
            Map<String, Object> SXObj = new HashMap<>();
            SXObj.put("bay_id", "101A-6");
            SXObj.put("bay_index", "06");
            SXObj.put("bay_name", "SX");
            SXObj.put("run_time", 132);
            SXObj.put("toolg_id", "101A-6");
            SXObj.put("total_time", 132);
            oaryERes.add(SXObj);
            Map<String, Object> GI6Obj = new HashMap<>();
            GI6Obj.put("bay_id", "101A-8");
            GI6Obj.put("bay_index", "08");
            GI6Obj.put("bay_name", "6GI");
            GI6Obj.put("run_time", 132);
            GI6Obj.put("toolg_id", "101A-8");
            GI6Obj.put("total_time", 132);
            oaryERes.add(GI6Obj);
            Map<String, Object> GI5Obj = new HashMap<>();
            GI5Obj.put("bay_id", "101A-10");
            GI5Obj.put("bay_index", "10");
            GI5Obj.put("bay_name", "5GI");
            GI5Obj.put("run_time", 66);
            GI5Obj.put("toolg_id", "101A-10");
            GI5Obj.put("total_time", 132);
            oaryERes.add(GI5Obj);
            map.put("oaryE", oaryERes);
        }else{
            map.put("oaryE",oaryERes);
        }
        return JsonUtil.toJsonString(map);
    }


    //获取设备历史状态
    @RequestMapping("/q3")
    public void getEqpStatus5DayByFabId(@RequestParam String fab_id_fk,HttpServletRequest request, HttpServletResponse response) {
        FipinqtoolO fipinqtoolO =  dashboardServiceImpl.findEqpStateByPeriod(fab_id_fk);
        ServletUtils.printJson(response, fipinqtoolO);
    }

    @RequestMapping("/q3-bak")
    public String setRightMiddleData(HttpServletRequest request, HttpServletResponse response) {
        return "{\"oaryD\":[{\"crop_mobility\":0.7,\"date\":\"2020-04-13\",\"toolStatusRate\":{\"OTHER\":0,\"DOWN\":0.29,\"STOP\":0,\"IDLE\":0,\"RUN\":0.7,\"PM\":0.01}},{\"crop_mobility\":0.71,\"date\":\"2020-04-12\",\"toolStatusRate\":{\"OTHER\":0,\"DOWN\":0.29,\"STOP\":0,\"IDLE\":0,\"RUN\":0.71,\"PM\":0}},{\"crop_mobility\":0.75,\"date\":\"2020-04-11\",\"toolStatusRate\":{\"OTHER\":0,\"DOWN\":0.25,\"STOP\":0,\"IDLE\":0,\"RUN\":0.75,\"PM\":0}},{\"crop_mobility\":0.73,\"date\":\"2020-04-10\",\"toolStatusRate\":{\"OTHER\":0,\"DOWN\":0.27,\"STOP\":0,\"IDLE\":0,\"RUN\":0.73,\"PM\":0}},{\"crop_mobility\":0.95,\"date\":\"2020-04-08\",\"toolStatusRate\":{\"OTHER\":0,\"DOWN\":0.05,\"STOP\":0,\"IDLE\":0,\"RUN\":0.95,\"PM\":0}}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"SUCCESS\",\"trx_id\":\"FIPINQTOL\",\"type_id\":\"O\"} ";
    }

    //获取设备状态
    @RequestMapping("/q4")
    public void getToolStatusByFabId(@RequestParam String fab_id_fk, HttpServletRequest request, HttpServletResponse response) {
        FbpbistolO fbpbistolO =  dashboardServiceImpl.findEqpStatusByFabId(fab_id_fk);
        ServletUtils.printJson(response, fbpbistolO);
    }

    //@RequestMapping("/q4")
    //public String getToolStatusByFabIdbak(HttpServletRequest request, HttpServletResponse response) {
    //    return "{\"down_rate\":0,\"oary\":[{\"tool_dsc\":\"102A-9#10#排版机\",\"tool_id\":\"102A-ABB01\",\"tool_stat\":\"PM\"},{\"tool_dsc\":\"102A-11#12#排版机\",\"tool_id\":\"102A-ABB02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-5#串焊机\",\"tool_id\":\"102A-ATW05\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-8#串焊机\",\"tool_id\":\"102A-ATW08\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线后膜EVA裁切机\",\"tool_id\":\"102A-BEVA-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线后膜EVA裁切机\",\"tool_id\":\"102A-BEVA-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线后膜EVA裁切机\",\"tool_id\":\"102A-BEVA-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-1#层压机\",\"tool_id\":\"102A-BSCY01\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-2#层压机\",\"tool_id\":\"102A-BSCY02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-3#层压机\",\"tool_id\":\"102A-BSCY03\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-4#层压机\",\"tool_id\":\"102A-BSCY04\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-5#层压机\",\"tool_id\":\"102A-BSCY05\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-6#层压机\",\"tool_id\":\"102A-BSCY06\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线组框机\",\"tool_id\":\"102A-COM-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线组框机\",\"tool_id\":\"102A-COM-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线组框机\",\"tool_id\":\"102A-COM-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线固化线\",\"tool_id\":\"102A-CUR-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线固化线\",\"tool_id\":\"102A-CUR-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线固化线\",\"tool_id\":\"102A-CUR-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线削边机\",\"tool_id\":\"102A-CUT-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线削边机\",\"tool_id\":\"102A-CUT-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线削边机\",\"tool_id\":\"102A-CUT-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线前膜EVA裁切机\",\"tool_id\":\"102A-FEVA-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线前膜EVA裁切机\",\"tool_id\":\"102A-FEVA-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线前膜EVA裁切机\",\"tool_id\":\"102A-FEVA-C\",\"tool_stat\":\"PM\"},{\"tool_dsc\":\"102A-A线装框机械手\",\"tool_id\":\"102A-GLU-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线装框机械手\",\"tool_id\":\"102A-GLU-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线装框机械手\",\"tool_id\":\"102A-GLU-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线分档\",\"tool_id\":\"102A-GRAD-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线分档\",\"tool_id\":\"102A-GRAD-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线分档\",\"tool_id\":\"102A-GRAD-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线玻璃上料\",\"tool_id\":\"102A-GRG-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线玻璃上料\",\"tool_id\":\"102A-GRG-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线玻璃上料\",\"tool_id\":\"102A-GRG-C\",\"tool_stat\":\"PM\"},{\"tool_dsc\":\"102A-7#层压机\",\"tool_id\":\"102A-JCCY07\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-8#层压机\",\"tool_id\":\"102A-JCCY08\",\"tool_stat\":\"PM\"},{\"tool_dsc\":\"102A-9#层压机\",\"tool_id\":\"102A-JCCY09\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-10#层压机\",\"tool_id\":\"102A-JCCY10\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-1#2#排版机\",\"tool_id\":\"102A-JCPB0102\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-3#4#排版机\",\"tool_id\":\"102A-JCPB0304\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-5#排版机\",\"tool_id\":\"102A-JCPB05\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-7#8#排版机\",\"tool_id\":\"102A-JCPB0708\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-1#串焊机1侧\",\"tool_id\":\"102A-KFW01-01\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-1#串焊机2侧\",\"tool_id\":\"102A-KFW01-02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-2#串焊机1侧\",\"tool_id\":\"102A-KFW02-01\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-2#串焊机2侧\",\"tool_id\":\"102A-KFW02-02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-3#串焊机1侧\",\"tool_id\":\"102A-KFW03-01\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-3#串焊机2侧\",\"tool_id\":\"102A-KFW03-02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-4#串焊机1侧\",\"tool_id\":\"102A-KFW04-01\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-4#串焊机2侧\",\"tool_id\":\"102A-KFW04-02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-7#串焊机1侧\",\"tool_id\":\"102A-KFW07-01\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-7#串焊机2侧\",\"tool_id\":\"102A-KFW07-02\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A包装翻转\",\"tool_id\":\"102A-OVERTURN\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A木托盘上料\",\"tool_id\":\"102A-PALLET\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A平移机械手\",\"tool_id\":\"102A-ROBOT\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-A线TPT裁切机\",\"tool_id\":\"102A-TPT-A\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-B线TPT裁切机\",\"tool_id\":\"102A-TPT-B\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-C线TPT裁切机\",\"tool_id\":\"102A-TPT-C\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-9#串焊机\",\"tool_id\":\"102A-TT09\",\"tool_stat\":\"PM\"},{\"tool_dsc\":\"102A-10#串焊机\",\"tool_id\":\"102A-TT10\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-11#串焊机\",\"tool_id\":\"102A-TT11\",\"tool_stat\":\"RUN\"},{\"tool_dsc\":\"102A-12#串焊机\",\"tool_id\":\"102A-TT12\",\"tool_stat\":\"RUN\"}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"SUCCESS\",\"tbl_cnt\":0,\"tbl_cnt_B\":0,\"total_tools\":62,\"total_tools_down\":0,\"trx_id\":\"FBPBISTOL\",\"type_id\":\"O\"}";
    //}


    //获取报警信息
    @RequestMapping("/q5")
    public void updateAlarmByFab(@RequestParam String fab_id_fk, HttpServletRequest request, HttpServletResponse response) {
        FipinqtoolO fipinqtoolO =  dashboardServiceImpl.findAlarmByFab(fab_id_fk);
        ServletUtils.printJson(response, fipinqtoolO);
    }

    @RequestMapping("/q5-bak")
    public String updateAlarmByFab2(HttpServletRequest request, HttpServletResponse response) {
        return "{\"oary\":[{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2019-09-11 13:14:28.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2019-09-11 13:14:28.0\",\"tool_dsc\":\"ALD主机台\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:00:00.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:00:00.0\",\"tool_dsc\":\"10#PECVD装卸片机\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:00:00.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:00:00.0\",\"tool_dsc\":\"02#PECVD\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:00:00.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:00:00.0\",\"tool_dsc\":\"06#PECVD\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:00:00.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:00:00.0\",\"tool_dsc\":\"07#PECVD\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:00:00.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:00:00.0\",\"tool_dsc\":\"26#PECVD\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:07:29.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:07:29.0\",\"tool_dsc\":\"丝网1线子设备22号机(ATM检测)_B\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 07:14:03.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 07:14:03.0\",\"tool_dsc\":\"丝网1线子设备26号机分选_A\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-09 17:08:50.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-09 17:08:50.0\",\"tool_dsc\":\"丝网1线子设备27号机分选_A\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-09 17:08:49.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-09 17:08:49.0\",\"tool_dsc\":\"丝网1线子设备27号机分选_B\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-13 07:13:14.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-13 07:13:14.0\",\"tool_dsc\":\"丝网2线子设备烘箱5_A\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-13 17:23:54.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-13 17:23:54.0\",\"tool_dsc\":\"丝网2线子设备烘箱7_A\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-04-14 08:58:24.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-04-14 08:58:24.0\",\"tool_dsc\":\"丝网9线子设备印刷机8_A\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-03-22 19:53:42.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-03-22 19:53:42.0\",\"tool_dsc\":\"丝网10线子设备印刷机4_A\"},{\"alarm_code\":\"故障\",\"evt_timestamp\":\"2020-03-22 20:57:15.0\",\"fab_id_fk\":\"101A\",\"start_timestamp\":\"2020-03-22 20:57:15.0\",\"tool_dsc\":\"丝网10线子设备烘箱5_A\"}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"SUCCESS\",\"trx_id\":\"FIPINQTOL\",\"type_id\":\"O\"}";
    }

    // 获取设备状态-站点
    //@RequestMapping("/q6")
    //public void q6(@RequestParam String bay_id,HttpServletRequest request, HttpServletResponse response) {
    //    FipinqtoolO fipinqtoolO =  dashboardServiceImpl.findEqpStateByStep(bay_id);
    //    ServletUtils.printJson(response, fipinqtoolO);
    //}

    @RequestMapping("/q6")
    public String q6bak(HttpServletRequest request, HttpServletResponse response) {
        return "{\"oaryB1\":[{\"second_time\":1634,\"time\":27,\"timeToal\":75,\"toolIds\":1,\"tool_id_fk\":\"CT02\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TKS\",\"wholeToolIds\":8},{\"second_time\":80,\"time\":1,\"timeToal\":75,\"toolIds\":3,\"tool_id_fk\":\"PE-ZDH-02\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TPECVD\",\"wholeToolIds\":8},{\"second_time\":0,\"time\":75,\"timeToal\":75,\"toolIds\":2,\"tool_id_fk\":\"SWYS02-AG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":8},{\"second_time\":2176,\"time\":36,\"timeToal\":75,\"toolIds\":2,\"tool_id_fk\":\"SWYS02-BG\",\"tool_stat\":\"DOWN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":8},{\"second_time\":1538,\"time\":47,\"timeToal\":75,\"toolIds\":1,\"tool_id_fk\":\"CT02\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TKS\",\"wholeToolIds\":8},{\"second_time\":0,\"time\":75,\"timeToal\":75,\"toolIds\":1,\"tool_id_fk\":\"ETCH-01-02\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TKES\",\"wholeToolIds\":8},{\"second_time\":3647,\"time\":73,\"timeToal\":75,\"toolIds\":3,\"tool_id_fk\":\"PE-ZDH-02\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TPECVD\",\"wholeToolIds\":8},{\"second_time\":0,\"time\":75,\"timeToal\":75,\"toolIds\":3,\"tool_id_fk\":\"PE03\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TPECVD\",\"wholeToolIds\":8},{\"second_time\":0,\"time\":75,\"timeToal\":75,\"toolIds\":3,\"tool_id_fk\":\"PE04\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TPECVD\",\"wholeToolIds\":8},{\"second_time\":1221,\"time\":39,\"timeToal\":75,\"toolIds\":2,\"tool_id_fk\":\"SWYS02-BG\",\"tool_stat\":\"RUN\",\"toolg_id\":\"$TSWYS\",\"wholeToolIds\":8},{\"second_time\":0,\"time\":75,\"timeToal\":75,\"toolIds\":1,\"tool_id_fk\":\"TEX02\",\"tool_stat\":\"STOP\",\"toolg_id\":\"$TZR\",\"wholeToolIds\":8}],\"oaryC\":[{\"time\":139,\"tool_stat\":\"DOWN\",\"totalTime\":600},{\"time\":75,\"tool_stat\":\"STOP\",\"totalTime\":600},{\"time\":384,\"tool_stat\":\"RUN\",\"totalTime\":600}],\"oaryE\":[{\"run_time\":223,\"toolg_id\":\"$TPECVD\",\"total_time\":225},{\"run_time\":47,\"toolg_id\":\"$TKS\",\"total_time\":75},{\"run_time\":0,\"toolg_id\":\"$TZR\",\"total_time\":75},{\"run_time\":39,\"toolg_id\":\"$TSWYS\",\"total_time\":150},{\"run_time\":75,\"toolg_id\":\"$TKES\",\"total_time\":75}],\"rtn_code\":\"0000000\",\"rtn_mesg\":\"SUCCESS\",\"trx_id\":\"FIPINQTOL\",\"type_id\":\"O\"}";
    }

    // 获取产量
    @RequestMapping("/q7")
    public void findProduction(@RequestParam String bay_id,HttpServletRequest request, HttpServletResponse response) {
        FipinqtoolO fipinqtoolO =  dashboardServiceImpl.findEqpStateByStep(bay_id);
        ServletUtils.printJson(response, fipinqtoolO);
    }

    @RequestMapping("/dayYield")
    public Response dayYield(@RequestParam String stationCode, @RequestParam String lineNo, HttpServletRequest request, HttpServletResponse response) {
        Response res = new Response();
        if (StringUtil.isEmpty(stationCode)) {
            stationCode = "DM";
        } else if (StringUtil.isEmpty(lineNo)){
            lineNo = "SIM";
        }
        String eqpId = null;
        if( stationCode.equals("DM")){
            eqpId ="SIM-DM7";
        }
        List<Map> maps = dashboardServiceImpl.dayYield(lineNo,stationCode,eqpId);
        res.put("yield", maps);
        return res;
    }



}
