package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.*;
import com.jeeadmin.entity.*;
import com.jeeadmin.mapper.SysRoleMapper;
import com.jeeadmin.vo.role.AssignMenuVo;
import com.jeeadmin.vo.role.AssignUserVo;
import com.jeeadmin.vo.user.QueryUserVo;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.SysConstant;
import com.jeerigger.module.sys.util.SysDictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysOrgAdminRoleService sysOrgAdminRoleService;

    @Override
    public Page<SysRole> selectPage(PageHelper<SysRole> pageHelper) {
        Page<SysRole> page = new Page<>(pageHelper.getCurrent(), pageHelper.getSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (pageHelper.getData() != null) {
            SysRole sysRole = pageHelper.getData();
            //角色名称
            if (StringUtil.isNotEmpty(sysRole.getRoleName())) {
                queryWrapper.lambda().like(SysRole::getRoleName, sysRole.getRoleName());
            }
            //角色编码
            if (StringUtil.isNotEmpty(sysRole.getRoleCode())) {
                queryWrapper.lambda().like(SysRole::getRoleCode, sysRole.getRoleCode());
            }
            //角色状态
            if (StringUtil.isNotEmpty(sysRole.getRoleStatus())) {
                queryWrapper.lambda().eq(SysRole::getRoleStatus, sysRole.getRoleStatus());
            }
            //角色类型
            if (StringUtil.isNotEmpty(sysRole.getRoleType())) {
                queryWrapper.lambda().eq(SysRole::getRoleType, sysRole.getRoleType());
            }
            //是否系统
            if (StringUtil.isNotEmpty(sysRole.getSysFlag())) {
                queryWrapper.lambda().eq(SysRole::getSysFlag, sysRole.getSysFlag());
            }
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                queryWrapper.lambda().ne(SysRole::getRoleCode, SysConstant.SYS_ADMIN_ROLE);
            }
        }
        queryWrapper.lambda().orderByAsc(SysRole::getRoleSort);
        this.page(page, queryWrapper);
        for (SysRole sysRole : page.getRecords()) {
            if (StringUtil.isNotEmpty(sysRole.getRoleType())){
                String roleTypeName = SysDictUtil.getDictLable(SysConstant.SYS_ROLE_TYPE, sysRole.getRoleType());
                sysRole.setRoleTypeName(roleTypeName);
            }
        }
        return page;
    }


    @Override
    public boolean updateStatus(String roleUuid, String roleStatus) {
        SysRole oldRole = this.getById(roleUuid);
        if (oldRole == null) {
            throw new ValidateException("该角色已不存在不能修改状态！");
        }
        if (oldRole.getRoleCode().equals(SysConstant.SYS_ADMIN_ROLE)) {
            throw new ValidateException("系统管理员默认角色不能进行更新状态！");
        }
        if (oldRole.getSysFlag().equals(FlagEnum.YES.getCode())) {
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                throw new ValidateException("系统内置角色不能修改状态！");
            }
        }
        SysRole sysRole = new SysRole();
        sysRole.setUuid(roleUuid);
        sysRole.setRoleStatus(roleStatus);
        return this.updateById(sysRole);
    }

    @Override
    public boolean saveSysRole(SysRole sysRole) {
        //数据验证
        ValidateUtil.validateObject(sysRole);
        //验证角色编码
        validatorRoleCode(sysRole);
        //保存数据
        boolean saveFlag = this.save(sysRole);
        //保存角色菜单数据
        if (saveFlag) {
            saveFlag = saveRoleMenu(sysRole.getUuid(), sysRole.getMenuUuidList());
        }
        return saveFlag;
    }

    /**
     * 保存角色菜单
     *
     * @param roleUuid
     * @param menuUuidList
     * @return
     */
    private boolean saveRoleMenu(String roleUuid, List<String> menuUuidList) {
        if (menuUuidList != null && menuUuidList.size() > 0) {
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            for (String menuUuid : menuUuidList) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleUuid(roleUuid);
                sysRoleMenu.setMenuUuid(menuUuid);
                sysRoleMenuList.add(sysRoleMenu);
            }
            return sysRoleMenuService.saveRoleMenu(sysRoleMenuList);
        }
        return true;
    }

    /**
     * 验证角色编码
     */
    private void validatorRoleCode(SysRole sysRole) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysRole.getUuid())) {
            queryWrapper.lambda().ne(SysRole::getUuid, sysRole.getUuid());
        }
        queryWrapper.lambda().eq(SysRole::getRoleCode, sysRole.getRoleCode());
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("该角色编码(" + sysRole.getRoleCode() + ")已存在！");
        }
    }

    @Override
    public boolean updateSysRole(SysRole sysRole) {
        SysRole oldRole = this.getById(sysRole.getUuid());
        if (oldRole == null) {
            throw new ValidateException("该角色(" + sysRole.getRoleCode() + ")已不存在，不能进行编辑！");
        }
        if (oldRole.getRoleCode().equals(SysConstant.SYS_ADMIN_ROLE)) {
            throw new ValidateException("系统管理员默认角色不能进行更新！");
        }
        if (oldRole.getSysFlag().equals(FlagEnum.YES.getCode())) {
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                throw new ValidateException("系统内置角色不能进行编辑！");
            }
        }
        //数据验证
        ValidateUtil.validateObject(sysRole);
        //验证角色编码
        validatorRoleCode(sysRole);
        return this.updateById(sysRole);
    }

    @Override
    public boolean deleteSysRole(String roleUuid) {
        SysRole sysRole = this.getById(roleUuid);
        if (sysRole == null) {
            return true;
        } else {
            if (sysRole.getRoleCode().equals(SysConstant.SYS_ADMIN_ROLE)) {
                throw new ValidateException("系统管理员默认角色不能进行删除！");
            }
            if (sysRole.getSysFlag().equals(FlagEnum.YES.getCode())) {
                if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                    throw new ValidateException("系统内置角色不能进行删除！");
                }
            }
        }


        //查询该角色已分配人员
        QueryWrapper<SysUserRole> userRoleWrapper = new QueryWrapper<>();
        userRoleWrapper.lambda().eq(SysUserRole::getRoleUuid, roleUuid);
        if (sysUserRoleService.count(userRoleWrapper) > 0) {
            throw new ValidateException("该角色（" + sysRole.getRoleCode() + "）已分配人员使用，不能删除！");
        }

        //查询该角色是否已分配给组织机构管理员
        QueryWrapper<SysOrgAdminRole> orgAdminRoleWrapper = new QueryWrapper<>();
        orgAdminRoleWrapper.lambda().eq(SysOrgAdminRole::getRoleUuid, roleUuid);
        if (sysOrgAdminRoleService.count(orgAdminRoleWrapper) > 0) {
            throw new ValidateException("该角色（" + sysRole.getRoleCode() + "）已分配组织机构管理员，不能删除！");
        }

        //删除角色菜单关联表信息
        if (sysRoleMenuService.deleteRoleMenu(roleUuid)) {
            //删除角色表
            if (this.removeById(roleUuid)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public List<SysRole> selectRoleList() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getRoleStatus, StatusEnum.NORMAL.getCode());
        queryWrapper.lambda().ne(SysRole::getRoleCode, SysConstant.SYS_ADMIN_ROLE);
        queryWrapper.lambda().orderByAsc(SysRole::getRoleSort);
        return this.list(queryWrapper);
    }

    @Override
    public Page<SysUser> selectUserPage(PageHelper<QueryUserVo> pageHelper) {
        Page<SysUser> page = new Page<>(pageHelper.getCurrent(), pageHelper.getSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        if (pageHelper.getData() != null) {
            QueryUserVo queryUserVo = pageHelper.getData();
            if (StringUtil.isNotEmpty(queryUserVo.getLoginName())) {
                queryWrapper.lambda().like(SysUser::getLoginName, queryUserVo.getLoginName());
            }
            if (StringUtil.isNotEmpty(queryUserVo.getUserName())) {
                queryWrapper.lambda().like(SysUser::getUserName, queryUserVo.getUserName());
            }
            if (StringUtil.isNotEmpty(queryUserVo.getUserEmail())) {
                queryWrapper.lambda().like(SysUser::getUserEmail, queryUserVo.getUserEmail());
            }
            if (StringUtil.isNotEmpty(queryUserVo.getUserPhone())) {
                queryWrapper.lambda().like(SysUser::getUserPhone, queryUserVo.getUserPhone());
            }
            if (StringUtil.isNotEmpty(queryUserVo.getUserMobile())) {
                queryWrapper.lambda().like(SysUser::getUserMobile, queryUserVo.getUserMobile());
            }
        }
        sysUserService.page(page, queryWrapper);
//        for (SysUser sysUser : page.getRecords()) {
//            if (StringUtil.isNotEmpty(sysUser.getOrgUuid())) {
//                sysUser.setOrgName(OrgUtil.getOrgName(sysUser.getOrgUuid()));
//            }
//        }
        return page;
    }

    @Override
    public boolean assignUser(AssignUserVo assignUserVo) {
        SysRole sysRole = this.getById(assignUserVo.getRoleUuid());
        if (sysRole == null) {
            throw new ValidateException("当前操作角色已不存在！");
        } else {
            if (sysRole.getRoleCode().equals(SysConstant.SYS_ADMIN_ROLE)) {
                throw new ValidateException("当前角色为系统管理员默认角色不能授予任何用户！");
            }
        }
        if (assignUserVo.getUserUuidList() != null && assignUserVo.getUserUuidList().size() > 0) {
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            for (String userUuid : assignUserVo.getUserUuidList()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserUuid(userUuid);
                sysUserRole.setRoleUuid(assignUserVo.getRoleUuid());
                QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>(sysUserRole);
                if (sysUserRoleService.count(queryWrapper) < 1) {
                    sysUserRoleList.add(sysUserRole);
                }
            }
            //保存用户角色
            return sysUserRoleService.saveUserRole(sysUserRoleList);
        }
        return true;
    }

    @Override
    public boolean assignMenu(AssignMenuVo assignMenuVo) {
        SysRole sysRole = this.getById(assignMenuVo.getRoleUuid());
        if (sysRole == null) {
            throw new ValidateException("当前操作角色已不存在！");
        } else {
            if (sysRole.getRoleCode().equals(SysConstant.SYS_ADMIN_ROLE)) {
                if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                    throw new ValidateException("该角色只有超级管理员才能分配菜单！");
                }
            }
        }
        boolean assignMenuFlag = sysRoleMenuService.deleteRoleMenu(assignMenuVo.getRoleUuid());
        if (assignMenuFlag) {
            assignMenuFlag = saveRoleMenu(assignMenuVo.getRoleUuid(), assignMenuVo.getMenuUuidList());
        }
        return assignMenuFlag;
    }
}
