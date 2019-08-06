package com.jeerigger.common.shiro.realm;

import com.jeerigger.common.enums.UserStatusEnum;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.BaseUser;
import com.jeerigger.common.user.UserData;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.entity.SysUser;
import com.jeerigger.module.sys.entity.UserMenu;
import com.jeerigger.module.sys.entity.UserRole;
import com.jeerigger.module.sys.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SysUserAuthorizingRealm extends AuthorizingRealmAbstract {

    @Autowired
    @Lazy
    private IUserService userService;

    @Override
    protected BaseUser getUserData(String loginName, String password) {
        SysUser sysUser = userService.getSysUser(loginName);
        if (sysUser != null) {
            if (sysUser.getUserStatus().equals(UserStatusEnum.NORMAL.getCode())) {
                if ((StringUtil.md5(password)).equals(sysUser.getLoginPassword())) {
                    UserData userData = new UserData();
                    userData.setUserUuid(sysUser.getUuid());
                    BeanUtils.copyProperties(sysUser, userData);
                    userData.setUserToken(ShiroUtil.getSession().getId().toString());
                    if (sysUser.getMgrFlag().equals(FlagEnum.YES.getCode())) {
                        userData.setUserType(UserTypeEnum.ORG_ADMIN_USER);
                    } else {
                        userData.setUserType(UserTypeEnum.NORMAL_USER);
                    }
                    return userData;
                } else {
                    throw new FrameException(ResultCodeEnum.ERROR_USER_PWD);
                }
            } else {
                if (sysUser.getUserStatus().equals(UserStatusEnum.DISABLE.getCode())) {
                    throw new FrameException(ResultCodeEnum.ERROR_USER_DISABLE);
                } else if (sysUser.getUserStatus().equals(UserStatusEnum.FREEZE.getCode())) {
                    throw new FrameException(ResultCodeEnum.ERROR_USER_FREEZE);
                } else {
                    throw new FrameException(ResultCodeEnum.ERROR_LOGIN_EXCEPTION, "用户登录未知状态异常！");
                }
            }
        } else {
            throw new FrameException(ResultCodeEnum.ERROR_NO_LOGIN_NAME);
        }
    }

    @Override
    protected List<String> getUserPermission(BaseUser userData) {
        List<UserMenu> menuList = userService.getUserMenu(userData.getUserUuid());
        List<String> permissionList = new ArrayList<>();
        if (menuList != null && menuList.size() > 0) {
            for (UserMenu userMenu : menuList) {
                if (StringUtil.isNotEmpty(userMenu.getPermission())) {
                    permissionList.add(userMenu.getPermission());
                }
            }
        }
        permissionList.add("user");
        return permissionList;
    }

    @Override
    protected List<String> getUserRole(BaseUser userData) {
        List<UserRole> userRoleList=userService.getUserRole(userData.getUserUuid());
        List<String> roleList=new ArrayList<>();
        if(userRoleList!=null && userRoleList.size()>0){
            for (UserRole userRole:userRoleList){
                roleList.add(userRole.getRoleCode());
            }
        }
        return roleList;
    }
}
