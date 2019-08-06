package com.jeerigger.module.sys.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.frame.support.util.SpringUtil;
import com.jeerigger.module.sys.entity.SysOrg;
import com.jeerigger.module.sys.mapper.OrgMapper;

import java.util.List;

/**
 * 机构工具类
 */
public class SysOrgUtil {
    private static final String SYS_ORG_LIST = "SYS_ORG_LIST";



    private static OrgMapper getOrgMapper() {
        return SpringUtil.getBean(OrgMapper.class);
    }

    /**
     * 获取机构列表
     *
     * @return
     */
    public static List<SysOrg> getSysOrgList() {
//        List<DictData> dictDataList= (List<DictData>)CacheUtil.getSysCache(SYS_DICT_DATA_LIST+"_"+dictType);
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_status", StatusEnum.NORMAL.getCode());
        queryWrapper.orderByAsc("parent_uuid", "org_sort");
        return getOrgMapper().selectList(queryWrapper);
    }

    /**
     * 根据机构UUID获取机构信息
     *
     * @param orgUuid
     * @return
     */
    public static SysOrg getSysOrg(String orgUuid) {
        return getOrgMapper().selectById(orgUuid);
    }

    /**
     * 根据机构编码获取机构信息
     *
     * @param orgCode
     * @return
     */
    public static SysOrg getSysOrgByCode(String orgCode) {
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_code", orgCode);
        return getOrgMapper().selectOne(queryWrapper);
    }

    /**
     * 根据机构UUID获取机构名称
     *
     * @param orgUuid
     * @return
     */
    public static String getOrgName(String orgUuid) {
        SysOrg sysOrg = getSysOrg(orgUuid);
        if (sysOrg != null) {
            return sysOrg.getOrgName();
        } else {
            return "";
        }
    }

    /**
     * 根据机构编码获取机构名称
     *
     * @param orgCode
     * @return
     */
    public static String getOrgNameByCode(String orgCode) {
        SysOrg sysOrg = getSysOrgByCode(orgCode);
        if (sysOrg != null) {
            return sysOrg.getOrgName();
        } else {
            return "";
        }
    }
}
