package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeeadmin.api.ISysOrgAdminOrgService;
import com.jeeadmin.api.ISysOrgService;
import com.jeeadmin.api.ISysUserService;
import com.jeeadmin.entity.SysOrg;
import com.jeeadmin.entity.SysOrgAdminOrg;
import com.jeeadmin.entity.SysUser;
import com.jeeadmin.mapper.SysOrgMapper;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.frame.base.service.impl.BaseTreeServiceImpl;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.SysConstant;
import com.jeerigger.module.sys.util.SysDictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 组织机构表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysOrgServiceImpl extends BaseTreeServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysOrgAdminOrgService sysOrgAdminOrgService;

    @Override
    public List<SysOrg> selectChildOrg(String orgUuid) {
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        if (StringUtil.isEmpty(orgUuid)) {
            orgUuid = "0";
        }
        wrapper.lambda().eq(SysOrg::getParentUuid, orgUuid);
        wrapper.lambda().orderByAsc(SysOrg::getParentUuid, SysOrg::getOrgSort);
        return this.getListOrg(wrapper);
    }

    @Override
    public List<SysOrg> selectOrgList(SysOrg sysOrg) {
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysOrg.getOrgCode())) {
            wrapper.lambda().eq(SysOrg::getOrgCode, sysOrg.getOrgCode());
        }
        if (StringUtil.isNotEmpty(sysOrg.getOrgName())) {
            wrapper.lambda().like(SysOrg::getOrgName, sysOrg.getOrgName());
        }
        if (StringUtil.isNotEmpty(sysOrg.getOrgShortName())) {
            wrapper.lambda().like(SysOrg::getOrgShortName, sysOrg.getOrgShortName());
        }
        if (StringUtil.isNotEmpty(sysOrg.getOrgType())) {
            wrapper.lambda().like(SysOrg::getOrgType, sysOrg.getOrgType());
        }
        if (StringUtil.isNotEmpty(sysOrg.getOrgStatus())) {
            wrapper.lambda().eq(SysOrg::getOrgStatus, sysOrg.getOrgStatus());
        }
        wrapper.lambda().orderByAsc(SysOrg::getParentUuid, SysOrg::getOrgSort);
        return this.getListOrg(wrapper);
    }

    private List<SysOrg> getListOrg(QueryWrapper<SysOrg> wrapper) {
        List<SysOrg> listOrg = this.list(wrapper);
        for (SysOrg sysOrg : listOrg) {
            if (StringUtil.isNotEmpty(sysOrg.getOrgType())) {
                String orgTypeName = SysDictUtil.getDictLable(SysConstant.SYS_ORG_TYPE, sysOrg.getOrgType());
                sysOrg.setOrgTypeName(orgTypeName);
            }
        }
        return listOrg;
    }

    @Override
    public SysOrg detailOrg(String orgUuid) {
        SysOrg sysOrg = this.getById(orgUuid);
        if (sysOrg != null && StringUtil.isNotEmpty(sysOrg.getParentUuid()) && !sysOrg.getParentUuid().equals("0")) {
            sysOrg.setParentOrg(this.getById(sysOrg.getParentUuid()));
        }
        return sysOrg;
    }

    @Override
    public boolean saveSysOrg(SysOrg sysOrg) {
        if (StringUtil.isEmpty(sysOrg.getParentUuid())) {
            sysOrg.setParentUuid("0");
        }
        //新增设置状态为正常
        sysOrg.setOrgStatus(StatusEnum.NORMAL.getCode());

        //校验业务数据
        ValidateUtil.validateObject(sysOrg);
        //验证上级组织机构
        validateParentUuid(sysOrg.getParentUuid());
        //校验组织机构代码是否已存在
        validateOrgCode(sysOrg);
        //验证同一级下名称是否存在
        validateOrgName(sysOrg);

        return this.save(sysOrg);
    }


    /**
     * 验证同一级下名称是否存在
     */
    private void validateOrgName(SysOrg sysOrg) {
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysOrg.getUuid())) {
            queryWrapper.lambda().ne(SysOrg::getUuid, sysOrg.getUuid());
        }
        queryWrapper.lambda().eq(SysOrg::getParentUuid, sysOrg.getParentUuid());
        queryWrapper.lambda().and(wrapper -> wrapper.eq(SysOrg::getOrgName, sysOrg.getOrgName()).or().eq(SysOrg::getOrgShortName, sysOrg.getOrgShortName()));
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("组织机构名称或组织机构简称已存在，请核实！");
        }
    }

    /**
     * 验证上级组织机构是否存在
     *
     * @param orgUuid
     */
    private void validateParentUuid(String orgUuid) {
        if (StringUtil.isNotEmpty(orgUuid) && !orgUuid.equals("0")) {
            if (this.getById(orgUuid) == null) {
                throw new ValidateException("选择的上级组织机构不存在！");
            }
        }
    }

    @Override
    public boolean updateSysOrg(SysOrg sysOrg) {
        if (this.getById(sysOrg.getUuid()) == null) {
            throw new ValidateException("该组织机构信息已不存在，不能进行编辑！");
        }
        //校验数据
        ValidateUtil.validateObject(sysOrg);
        //验证上级组织机构
        validateParentUuid(sysOrg.getParentUuid());
        //校验组织机构代码是否已存在
        validateOrgCode(sysOrg);
        //验证同一级下组织机构名称是否存在
        validateOrgName(sysOrg);

        return this.updateById(sysOrg);
    }

    @Override
    public boolean updateStatus(SysOrg sysOrg) {
        if (this.getById(sysOrg.getUuid()) == null) {
            throw new ValidateException("更新的组织机构不存在！");
        }
        List childrenPkList = this.getChildrenPk(sysOrg.getUuid());
        childrenPkList.add(sysOrg.getUuid());
        UpdateWrapper<SysOrg> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysOrg::getOrgStatus, sysOrg.getOrgStatus());
        updateWrapper.lambda().in(SysOrg::getUuid, childrenPkList);
        boolean updateFlag = this.update(new SysOrg(), updateWrapper);
        if (updateFlag) {
            //如果将当前节点启用则将所有父级节点也进行启用
            if (sysOrg.getOrgStatus().equals(StatusEnum.NORMAL.getCode())) {
                List parentPkList = this.getParentPk(sysOrg.getUuid());
                if (parentPkList != null && parentPkList.size() > 0) {
                    UpdateWrapper<SysOrg> updateParentWrapper = new UpdateWrapper<>();
                    updateParentWrapper.lambda().set(SysOrg::getOrgStatus, StatusEnum.NORMAL.getCode());
                    updateParentWrapper.lambda().in(SysOrg::getUuid, parentPkList);
                    this.update(new SysOrg(), updateParentWrapper);
                }
            }
        }
        return updateFlag;
    }


    /**
     * 添加或者更新的时候校验组织机构代码是否已存在
     *
     * @param sysOrg
     */
    private void validateOrgCode(SysOrg sysOrg) {
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysOrg::getOrgCode, sysOrg.getOrgCode());
        if (StringUtil.isNotEmpty(sysOrg.getUuid())) {
            queryWrapper.lambda().ne(SysOrg::getUuid, sysOrg.getUuid());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("组织机构代码（" + sysOrg.getOrgCode() + "）已存在！");
        }
    }

    @Override
    public boolean deleteSysOrg(String orgUuid) {

        //判断是否存在下级组织机构
        List list = this.getChildrenPk(orgUuid);
        if (list != null && list.size() > 0) {
            throw new ValidateException("请先删除下级组织结构！");
        }

        //判断该组织机构是否已与人员绑定
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.lambda().eq(SysUser::getOrgUuid, orgUuid);
        if (sysUserService.count(userWrapper) > 0) {
            throw new ValidateException("该组织机构已与用户绑定不能删除！");
        }

        QueryWrapper<SysOrgAdminOrg> orgAdminOrgWrapper = new QueryWrapper<>();
        orgAdminOrgWrapper.lambda().eq(SysOrgAdminOrg::getOrgUuid, orgUuid);
        if (sysOrgAdminOrgService.count(orgAdminOrgWrapper) > 0) {
            throw new ValidateException("该组织机构已与组织机构管理员绑定不能删除！");
        }

        return this.removeById(orgUuid);
    }

}
