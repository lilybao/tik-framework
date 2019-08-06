package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.ISysAdminUserService;
import com.jeeadmin.entity.SysAdminUser;
import com.jeeadmin.entity.SysMenu;
import com.jeeadmin.mapper.SysAdminUserMapper;
import com.jeeadmin.vo.user.UpdatePwdVo;
import com.jeeadmin.vo.user.UpdateUserVo;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.SysConstant;
import com.jeerigger.module.sys.entity.UserMenu;
import com.jeerigger.module.sys.service.IUserService;
import com.jeerigger.module.sys.util.SysParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统管理员信息表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysAdminUserServiceImpl extends BaseServiceImpl<SysAdminUserMapper, SysAdminUser> implements ISysAdminUserService {
    @Autowired
    private IUserService userService;

    @Override
    public SysAdminUser getAdminUserByLoginName(String loginName) {

        QueryWrapper<SysAdminUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysAdminUser::getLoginName, loginName);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<SysAdminUser> selectPage(PageHelper<SysAdminUser> pageHelper) {
        Page page = new Page<SysAdminUser>(pageHelper.getCurrent(), pageHelper.getSize());
        QueryWrapper<SysAdminUser> queryWrapper = new QueryWrapper();
        if (pageHelper.getData() != null) {
            SysAdminUser sysAdminUser = pageHelper.getData();
            queryWrapper.lambda().eq(SysAdminUser::getMgrType, UserTypeEnum.SYSTEM_ADMIN_USER.getCode());
            if (StringUtil.isNotEmpty(sysAdminUser.getLoginName())) {
                queryWrapper.lambda().like(SysAdminUser::getLoginName, sysAdminUser.getLoginName());
            }
            if (StringUtil.isNotEmpty(sysAdminUser.getUserName())) {
                queryWrapper.lambda().like(SysAdminUser::getUserName, sysAdminUser.getUserName());
            }
            if (StringUtil.isNotEmpty(sysAdminUser.getUserStatus())) {
                queryWrapper.lambda().like(SysAdminUser::getUserStatus, sysAdminUser.getUserStatus());
            }
        }
        queryWrapper.lambda().orderByAsc(SysAdminUser::getUserSort);
        return (Page<SysAdminUser>) this.page(page, queryWrapper);
    }

    @Override
    public boolean saveAdminUser(SysAdminUser sysAdminUser) {
        //验证登录名
        validateLoginName(sysAdminUser);
        sysAdminUser.setMgrType(UserTypeEnum.SYSTEM_ADMIN_USER.getCode());
        //校验数据准确性
        ValidateUtil.validateObject(sysAdminUser);
        sysAdminUser.setLoginPassword(StringUtil.md5(SysParamUtil.getInitPassword()));
        return this.save(sysAdminUser);
    }

    @Override
    public boolean updateAdminUser(SysAdminUser sysAdminUser) {
        if (this.getById(sysAdminUser.getUuid()) == null) {
            throw new ValidateException("该管理员（" + sysAdminUser.getLoginName() + "）已不存在，不能进行编辑！");
        }

        //校验数据准确性
        ValidateUtil.validateObject(sysAdminUser);
        //验证登录名
        validateLoginName(sysAdminUser);
        sysAdminUser.setMgrType(UserTypeEnum.SYSTEM_ADMIN_USER.getCode());
        return this.updateById(sysAdminUser);
    }

    @Override
    public boolean updateAdminUserStatus(SysAdminUser sysAdminUser) {
        if (StringUtil.isEmpty(sysAdminUser.getUuid()) || StringUtil.isEmpty(sysAdminUser.getUserStatus())) {
            throw new ValidateException("用户唯一标识和状态不能为空！");
        }
        if (this.getById(sysAdminUser.getUuid()) == null) {
            throw new ValidateException("用户不存在！");
        }
        SysAdminUser adminUser = new SysAdminUser();
        adminUser.setUuid(sysAdminUser.getUuid());
        adminUser.setUserStatus(sysAdminUser.getUserStatus());

        return this.updateById(adminUser);
    }


    /**
     * 保存、更新验证登录用户名是否存在
     *
     * @param sysAdminUser
     */
    private void validateLoginName(SysAdminUser sysAdminUser) {
        QueryWrapper<SysAdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdminUser::getLoginName, sysAdminUser.getLoginName());
        if (StringUtil.isNotEmpty(sysAdminUser.getUuid())) {
            queryWrapper.lambda().ne(SysAdminUser::getUuid, sysAdminUser.getUuid());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("该登录名（" + sysAdminUser.getLoginName() + "）已存在！");
        }
    }

    @Override
    public boolean resetPwd(String userUuid) {
        SysAdminUser sysAdminUser = new SysAdminUser();
        sysAdminUser.setLoginPassword(StringUtil.md5(SysParamUtil.getInitPassword()));
        sysAdminUser.setUuid(userUuid);
        return this.updateById(sysAdminUser);
    }

    @Override
    public boolean changePassword(UpdatePwdVo updatePwdVo) {
        SysAdminUser sysAdminUser = this.getById(ShiroUtil.getUserUuid());
        if (sysAdminUser != null) {
            if (sysAdminUser.getLoginPassword().equals(StringUtil.md5(updatePwdVo.getOldPassword()))) {
                sysAdminUser = new SysAdminUser();
                sysAdminUser.setUuid(ShiroUtil.getUserUuid());
                sysAdminUser.setLoginPassword(StringUtil.md5(updatePwdVo.getNewPassword()));
                return this.updateById(sysAdminUser);
            } else {
                throw new ValidateException("输入的老密码不正确！");
            }
        } else {
            throw new FrameException(ResultCodeEnum.ERROR_NO_USER_INFO);
        }
    }

    @Override
    public boolean updateUserInfo(UpdateUserVo updateUserVo) {
        SysAdminUser sysAdminUser = new SysAdminUser();
        BeanUtils.copyProperties(updateUserVo, sysAdminUser);
        sysAdminUser.setUuid(ShiroUtil.getUserUuid());
        if (ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
            sysAdminUser.setMgrType(UserTypeEnum.SUPER_ADMIN_USER.getCode());
        }
        return this.updateById(sysAdminUser);
    }

    @Override
    public List<SysMenu> getSysAdminMenu() {
        List<UserMenu> userMenuList = null;
        if (ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
            userMenuList = userService.getSuperAdminMenu();
        } else if (ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SYSTEM_ADMIN_USER)) {
            userMenuList = userService.getAdminUserMenu(SysConstant.SYS_ADMIN_ROLE);
        }
        List<SysMenu> sysMenuList = new ArrayList<>();
        if (userMenuList != null && userMenuList.size() > 0) {
            for (UserMenu userMenu : userMenuList) {
                SysMenu sysMenu = new SysMenu();
                BeanUtils.copyProperties(userMenu, sysMenu);
                sysMenuList.add(sysMenu);
            }
        }
        return sysMenuList;
    }
}
