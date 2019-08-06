package com.jeerigger.module.sys.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.support.util.SpringUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.entity.SysArea;
import com.jeerigger.module.sys.mapper.AreaMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 机构工具类
 */
public class SysAreaUtil {
    private static final String SYS_AREA_LIST = "sys_area_list";

    private static AreaMapper getAreaMapper() {
        return SpringUtil.getBean(AreaMapper.class);
    }

    /**
     * 获取行政区划列表
     *
     * @return
     */
    public static List<SysArea> getSysAreaList() {
//        List<DictData> dictDataList= (List<DictData>)CacheUtil.getSysCache(SYS_DICT_DATA_LIST+"_"+dictType);
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("area_status", StatusEnum.NORMAL.getCode());
        queryWrapper.orderByAsc("parent_uuid", "area_sort");
        return getAreaMapper().selectList(queryWrapper);
    }

    public static List<SysArea> getSysAreaList(String areaType) {
        List<SysArea> sysAreaAllList=getSysAreaList();
        if(StringUtil.isNotEmpty(areaType)){
            int areaTypeInt;
            try{
                areaTypeInt = Integer.parseInt(areaType);
            }catch (Exception ex){
                areaTypeInt=0;
            }

            List<SysArea> sysAreaList=new ArrayList<>();
            for (SysArea sysArea:sysAreaAllList) {
                if(StringUtil.isNotEmpty(sysArea.getAreaType())){
                    int sysAreaType;
                    try{
                        sysAreaType = Integer.parseInt(sysArea.getAreaType());
                    }catch (Exception ex){
                        sysAreaType=0;
                    }
                    if(sysAreaType<=areaTypeInt){
                        if(sysAreaType==areaTypeInt){
                            sysArea.setLeafFlag(FlagEnum.YES.getCode());
                        }
                        sysAreaList.add(sysArea);
                    }
                }else{
                    sysAreaList.add(sysArea);
                }
            }
            return sysAreaList;
        }else{
            return sysAreaAllList;
        }

    }
    /**
     * 根据行政区划UUID获取行政区划信息
     *
     * @param areaUuid
     * @return
     */
    public static SysArea getSysArea(String areaUuid) {
        return getAreaMapper().selectById(areaUuid);
    }

    /**
     * 根据行政区划编码获取行政区划信息
     *
     * @param areaCode
     * @return
     */
    public static SysArea getSysAreaByCode(String areaCode) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("area_code", areaCode);
        return getAreaMapper().selectOne(queryWrapper);
    }

    /**
     * 根据行政区划UUID获取行政区划信息
     *
     * @param areaUuid
     * @return
     */
    public static String getAreaName(String areaUuid) {
        SysArea sysArea = getSysArea(areaUuid);
        if (sysArea != null) {
            return sysArea.getAreaName();
        } else {
            return "";
        }
    }

    /**
     * 根据行政区划编码获取行政区划名称
     *
     * @param areaCode
     * @return
     */
    public static String getAreaNameByCode(String areaCode) {
        SysArea sysArea = getSysAreaByCode(areaCode);
        if (sysArea != null) {
            return sysArea.getAreaName();
        } else {
            return "";
        }
    }
}
