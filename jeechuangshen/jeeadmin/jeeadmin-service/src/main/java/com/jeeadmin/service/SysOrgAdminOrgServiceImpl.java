package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeadmin.api.ISysOrgAdminOrgService;
import com.jeeadmin.api.ISysOrgService;
import com.jeeadmin.entity.SysOrg;
import com.jeeadmin.entity.SysOrgAdminOrg;
import com.jeeadmin.mapper.SysOrgAdminOrgMapper;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 机构管理员  组织机构分配表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
@Service
public class SysOrgAdminOrgServiceImpl extends BaseServiceImpl<SysOrgAdminOrgMapper, SysOrgAdminOrg> implements ISysOrgAdminOrgService {

    @Autowired
    private ISysOrgService sysOrgService;

    @Override
    public boolean saveOrgAdminOrg(List<SysOrgAdminOrg> sysOrgAdminOrgList) {
        return this.saveBatch(sysOrgAdminOrgList);
    }

    @Override
    public boolean deleteOrgAdminOrg(String userUuid) {
        if (StringUtil.isNotEmpty(userUuid)) {
            QueryWrapper<SysOrgAdminOrg> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(SysOrgAdminOrg::getUserUuid, userUuid);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public List<SysOrgAdminOrg> detailOrgList(String userUuid) {
        QueryWrapper<SysOrgAdminOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysOrgAdminOrg::getUserUuid,userUuid);
        List<SysOrgAdminOrg> sysOrgAdminOrgList = this.list(queryWrapper);
        List<SysOrgAdminOrg> orgAdminOrgList = new ArrayList<>();
        for (SysOrgAdminOrg sysOrgAdminOrg : sysOrgAdminOrgList) {
            QueryWrapper<SysOrg> queryOrgWrapper = new QueryWrapper<>();
            queryOrgWrapper.lambda().eq(SysOrg::getParentUuid, sysOrgAdminOrg.getOrgUuid());
            if (sysOrgService.count(queryOrgWrapper) < 1) {
                orgAdminOrgList.add(sysOrgAdminOrg);
            }
        }
        return orgAdminOrgList;
    }
}
