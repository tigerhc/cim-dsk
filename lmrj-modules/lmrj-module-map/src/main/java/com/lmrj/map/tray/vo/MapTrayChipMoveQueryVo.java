 package com.lmrj.map.tray.vo;

import java.util.List;

import lombok.Data;

/**
 * @author Administrator
 * @date 2020/08/14
 */
@Data
public class MapTrayChipMoveQueryVo {

    public static final int DEFAULT_PAGE_SIZE = 50;

    private String lotNo;

    private List<String> chipIds;

    private String chipId;

    private List<String> eqpIds;

    private String sTime;

    private String eTime;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private int offset;

    private int total;


}
