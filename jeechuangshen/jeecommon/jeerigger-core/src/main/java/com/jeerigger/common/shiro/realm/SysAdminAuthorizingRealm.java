package com.jeerigger.common.shiro.realm;

import com.jeerigger.common.enums.UserStatusEnum;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.BaseUser;
import com.jeerigger.common.user.UserData;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.SysConstant;
import com.jeerigger.module.sys.entity.SysAdminUser;
import com.jeerigger.module.sys.entity.UserMenu;
import com.jeerigger.module.sys.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SysAdminAuthorizingRealm extends AuthorizingRealmAbstract {

    @Autowired
    @Lazy
    private IUserService userService;

    @Override
    protected BaseUser getUserData(String loginName, String password) {
        SysAdminUser sysAdminUser= userService.getSysAdminUser(loginName);
        if(sysAdminUser != null) {
            if(sysAdminUser.getUserStatus().equals(UserStatusEnum.NORMAL.getCode())){
                if((StringUtil.md5(password)).equals(sysAdminUser.getLoginPassword())) {
                    UserData userData=new UserData();
                    userData.setUserUuid(sysAdminUser.getUuid());
                    BeanUtils.copyProperties(sysAdminUser,userData);
                    userData.setUserToken(ShiroUtil.getSession().getId().toString());
                    if(sysAdminUser.getMgrType().equals(UserTypeEnum.SUPER_ADMIN_USER.getCode())){
                        userData.setUserType(UserTypeEnum.SUPER_ADMIN_USER);
                    } else {
                        userData.setUserType(UserTypeEnum.SYSTEM_ADMIN_USER);
                    }
                    return userData;
                } else {
                    throw new FrameException(ResultCodeEnum.ERROR_USER_PWD);
                }
            }else {
                if(sysAdminUser.getUserStatus().equals(UserStatusEnum.DISABLE.getCode())){
                    throw new FrameException(ResultCodeEnum.ERROR_USER_DISABLE);
                }else if(sysAdminUser.getUserStatus().equals(UserStatusEnum.FREEZE.getCode())){
                    throw new FrameException(ResultCodeEnum.ERROR_USER_FREEZE);
                }else {
                    throw new FrameException(ResultCodeEnum.ERROR_LOGIN_EXCEPTION,"用户登录未知状态异常！");
                }
            }
        } else {
            throw new FrameException(ResultCodeEnum.ERROR_NO_LOGIN_NAME);
        }
    }

    @Override
    protected List<String> getUserPermission(BaseUser userData) {

        List<UserMenu> sysMenuList=null;
        if(userData.getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)){
            sysMenuList = userService.getSuperAdminMenu();
        }else if(userData.getUserType().equals(UserTypeEnum.SYSTEM_ADMIN_USER)){
            sysMenuList = userService.getAdminUserMenu(SysConstant.SYS_ADMIN_ROLE);
        }
        List<String> permissionList=new ArrayList<>();
        if(sysMenuList!=null && sysMenuList.size()>0){
            for (UserMenu userMenu: sysMenuList) {
                if(StringUtil.isNotEmpty(userMenu.getPermission())){
                    permissionList.add(userMenu.getPermission());
                }
            }
        }
        permissionList.add("user");
        return permissionList;
    }

    @Override
    protected List<String> getUserRole(BaseUser userData) {
        List<String> userRoleList=new ArrayList<>();
        if(userData.getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)){
            userRoleList.add("*");
        }else if(userData.getUserType().equals(UserTypeEnum.SYSTEM_ADMIN_USER)){
            userRoleList.add(SysConstant.SYS_ADMIN_ROLE);
        }
        return userRoleList;
    }
}
