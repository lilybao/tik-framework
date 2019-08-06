package com.jeerigger.module.sys.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.frame.support.util.SpringUtil;
import com.jeerigger.module.sys.entity.SysDictData;
import com.jeerigger.module.sys.mapper.DictDataMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 * 考虑未来从缓存里获取
 */

public class SysDictUtil {

    private static final String SYS_DICT_DATA_LIST = "sys_dict_data";

    private static Map<String, SysDictData> dictDataMap = new HashMap<>();

    private static DictDataMapper getDictDataMapper() {
        return SpringUtil.getBean(DictDataMapper.class);
    }

    /**
     * 根据字段类型获取字段数据列表
     *
     * @param dictType
     * @return
     */
    public static List<SysDictData> getSysDictDataList(String dictType) {
//        List<DictData> dictDataList= (List<DictData>)CacheUtil.getSysCache(SYS_DICT_DATA_LIST+"_"+dictType);

        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        //只获取状态为正常的数据
        queryWrapper.eq("dict_status", StatusEnum.NORMAL.getCode());
        queryWrapper.orderByAsc("dict_sort");
        return getDictDataMapper().selectList(queryWrapper);
    }

    /**
     * 根据字典类型，字典键值获取字段标签
     *
     * @param dictType
     * @param dictValue
     * @return
     */
    public static String getDictLable(String dictType, String dictValue) {
        return getDictLable(dictType, dictValue, "");
    }

    /**
     * 根据字典类型，字典键值获取字段标签
     *
     * @param dictType
     * @param dictValue
     * @param defaultLable
     * @return
     */
    public static String getDictLable(String dictType, String dictValue, String defaultLable) {
        SysDictData sysDictData = getSysDictData(dictType, dictValue);
        if (sysDictData != null) {
            return sysDictData.getDictLabel();
        } else {
            return defaultLable;
        }
    }

    public static SysDictData getSysDictData(String dictType, String dictValue) {
        SysDictData dictData = dictDataMap.get(dictType + dictValue);
        if (dictData == null) {
            QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dict_type", dictType);
            queryWrapper.eq("dict_value", dictValue);
            dictData = getDictDataMapper().selectOne(queryWrapper);
            dictDataMap.put(dictType + dictValue, dictData);
        }
        return dictData;
    }

}
