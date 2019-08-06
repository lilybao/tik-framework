package com.jeerigger.module.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.common.enums.UserStatusEnum;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.SysConstant;
import com.jeerigger.module.sys.api.ISysUserPostService;
import com.jeerigger.module.sys.api.ISysUserRoleService;
import com.jeerigger.module.sys.api.ISysUserService;
import com.jeerigger.module.sys.entity.*;
import com.jeerigger.module.sys.mapper.OrgMapper;
import com.jeerigger.module.sys.mapper.SysRoleMapper;
import com.jeerigger.module.sys.mapper.SysUserMapper;
import com.jeerigger.module.sys.util.SysOrgUtil;
import com.jeerigger.module.sys.util.SysParamUtil;
import com.jeerigger.module.sys.vo.AssignUserRoleVo;
import com.jeerigger.module.sys.vo.UpdateUserInfoVo;
import com.jeerigger.module.sys.vo.UpdateUserPwdVo;
import com.jeerigger.module.sys.vo.UserRoleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private ISysUserPostService sysUserPostService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private OrgMapper sysOrgMapper;


    @Override
    public SysUser getUserByLoginName(String loginName) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getLoginName, loginName);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<SysUser> selectPage(PageHelper<SysUser> pageHelper) {

        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能查看用户信息！");
        }

        List orgAdminOrgList = this.baseMapper.getOrgAdminOrgUuid(ShiroUtil.getUserUuid());
        if (orgAdminOrgList == null && orgAdminOrgList.size() < 1) {
            return null;
        }
        Page<SysUser> page = new Page<>(pageHelper.getCurrent(), pageHelper.getSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(SysUser::getOrgUuid, orgAdminOrgList);
        if (pageHelper.getData() != null) {
            SysUser sysUser = pageHelper.getData();
            if (StringUtil.isNotEmpty(sysUser.getLoginName())) {
                queryWrapper.lambda().like(SysUser::getLoginName, sysUser.getLoginName());
            }
            if (StringUtil.isNotEmpty(sysUser.getUserName())) {
                queryWrapper.lambda().like(SysUser::getUserName, sysUser.getUserName());
            }
            if (StringUtil.isNotEmpty(sysUser.getUserEmail())) {
                queryWrapper.lambda().like(SysUser::getUserEmail, sysUser.getUserEmail());
            }
            if (StringUtil.isNotEmpty(sysUser.getUserPhone())) {
                queryWrapper.lambda().like(SysUser::getUserPhone, sysUser.getUserPhone());
            }
            if (StringUtil.isNotEmpty(sysUser.getUserMobile())) {
                queryWrapper.lambda().like(SysUser::getUserMobile, sysUser.getUserMobile());
            }
            if (StringUtil.isNotEmpty(sysUser.getOrgUuid())) {
                queryWrapper.lambda().eq(SysUser::getOrgUuid, sysUser.getOrgUuid());
            }
            if (StringUtil.isNotEmpty(sysUser.getUserStatus())) {
                queryWrapper.lambda().eq(SysUser::getUserStatus, sysUser.getUserStatus());
            }
        }
        this.page(page, queryWrapper);
        for (SysUser sysUser : page.getRecords()) {
            if (StringUtil.isNotEmpty(sysUser.getOrgUuid())) {
                sysUser.setOrgName(SysOrgUtil.getOrgName(sysUser.getOrgUuid()));
            }
        }
        return page;
    }

    @Override
    public SysUser detailUserInfo(String userUuid) {
        if (StringUtil.isEmpty(userUuid)) {
            throw new ValidateException("用户UUID不能为空！");
        }
        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能执行该操作！");
        }
        List orgAdminOrgList = this.baseMapper.getOrgAdminOrgUuid(ShiroUtil.getUserUuid());
        if (orgAdminOrgList == null && orgAdminOrgList.size() < 1) {
            throw new ValidateException("您没有查看该用户信息的权限！");
        }
        SysUser sysUser = this.getById(userUuid);

        if (sysUser != null && StringUtil.isNotEmpty(sysUser.getOrgUuid())) {
            if (!orgAdminOrgList.contains(sysUser.getOrgUuid())) {
                throw new ValidateException("您没有查看该用户信息的权限！");
            }
            sysUser.setOrgName(SysOrgUtil.getOrgName(sysUser.getOrgUuid()));
            List<String> postUuidList=new ArrayList<>();
            List<SysUserPost> userPostList=sysUserPostService.detailPostList(userUuid);
            if(userPostList!=null && userPostList.size()>0){
                for (SysUserPost sysUserPost:userPostList){
                    postUuidList.add(sysUserPost.getPostUuid());
                }
            }
            sysUser.setPostUuidList(postUuidList);
        } else {
            throw new ValidateException("该用户还没有分配组织机构不能进行查看操作！");
        }
        
        return sysUser;
    }


    /**
     * 验证用户登录名是否存在
     *
     * @param sysUser
     */
    private void validateLoginName(SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getLoginName, sysUser.getLoginName());
        if (StringUtil.isNotEmpty(sysUser.getUuid())) {
            queryWrapper.lambda().ne(SysUser::getUuid, sysUser.getUuid());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("用户登录名已存在！");
        }
    }

    /**
     * 验证员工编码
     *
     * @param sysUser
     */
    private void validateUserNumber(SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUserNumber, sysUser.getUserNumber());
        if (StringUtil.isNotEmpty(sysUser.getUuid())) {
            queryWrapper.lambda().ne(SysUser::getUuid, sysUser.getUuid());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("员工编号已存在！");
        }
    }

    @Override
    public boolean saveUser(SysUser sysUser) {
        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能新增用户！");
        }
        sysUser.setUserStatus(UserStatusEnum.NORMAL.getCode());
        sysUser.setMgrFlag(FlagEnum.NO.getCode());
        //验证数据
        ValidateUtil.validateObject(sysUser);
        //验证登录名
        validateLoginName(sysUser);
        //验证员工编号
        validateUserNumber(sysUser);

        if (SysOrgUtil.getSysOrg(sysUser.getOrgUuid()) == null) {
            throw new ValidateException("选择的组织机构代码已不存在！");
        }
        sysUser.setLoginPassword(StringUtil.md5(SysParamUtil.getInitPassword()));
        //保存用户信息
        boolean saveFlag = this.save(sysUser);
        if (saveFlag) {
            saveFlag = saveUserRole(sysUser.getUuid(), sysUser.getRoleUuidList());
        }
        return saveFlag;
    }

    @Override
    public boolean updateUser(SysUser sysUser) {

        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能更新用户信息！");
        }

        if (this.getById(sysUser.getUuid()) == null) {
            throw new ValidateException("该用户不存在！");
        }
        //验证数据有效性
        ValidateUtil.validateObject(sysUser);
        //验证登录名
        validateLoginName(sysUser);
        //验证员工编号
        validateUserNumber(sysUser);
        //更新用户信息
        return this.updateById(sysUser);
    }

    @Override
    public boolean updateUserInfo(UpdateUserInfoVo updateUserInfoVo) {
        SysUser sysUser = new SysUser();
        ValidateUtil.validateObject(updateUserInfoVo);
        BeanUtils.copyProperties(updateUserInfoVo, sysUser);
        sysUser.setUuid(ShiroUtil.getUserUuid());
        //删除用户已分配的岗位
        if (sysUserPostService.deleteUserPost(sysUser.getUuid())) {
            saveUserPost(sysUser.getUuid(), sysUser.getPostUuidList());
        }
        return this.updateById(sysUser);
    }

    @Override
    public boolean deleteUser(String userUuid) {
        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能删除用户！");
        }
        SysUser sysUser=this.getById(userUuid);
        if(sysUser.getMgrFlag().equals(FlagEnum.YES.getCode())){
            throw new ValidateException("该用户是机构管理员，您没有权限删除！");
        }
        QueryWrapper<SysUserRole> whereWrapper = new QueryWrapper<>();
        whereWrapper.lambda().eq(SysUserRole::getUserUuid, userUuid);
        //删除用户岗位信息
        sysUserPostService.deleteUserPost(userUuid);
        //删除用户角色关系表数据
        sysUserRoleService.deleteUserRole(userUuid);
        //删除用户信息
        this.removeById(userUuid);
        return true;
    }


    @Override
    public boolean updateUserStatus(SysUser sysUser) {
        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能修改用户状态！");
        }
        if (StringUtil.isEmpty(sysUser.getUuid()) || StringUtil.isEmpty(sysUser.getUserStatus())) {
            throw new ValidateException("用户唯一标识和状态不能为空！");
        }
        if (this.getById(sysUser.getUuid()) == null) {
            throw new ValidateException("用户不存在！");
        }
        SysUser user = new SysUser();
        user.setUuid(sysUser.getUuid());
        user.setUserStatus(sysUser.getUserStatus());

        return this.updateById(user);
    }

    @Override
    public boolean resetUserPassword(String userUuid) {
        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能重置用户密码！");
        }
        if (this.getById(userUuid) == null) {
            throw new ValidateException("用户已不存在！");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUuid(userUuid);
        sysUser.setLoginPassword(StringUtil.md5(SysParamUtil.getInitPassword()));

        return this.updateById(sysUser);
    }

    @Override
    public boolean changePassword(UpdateUserPwdVo changePwdVo) {
        SysUser sysUser = this.getById(ShiroUtil.getUserUuid());
        if (sysUser != null) {
            if (sysUser.getLoginPassword().equals(StringUtil.md5(changePwdVo.getOldPassword()))) {
                sysUser = new SysUser();
                sysUser.setUuid(ShiroUtil.getUserUuid());
                sysUser.setLoginPassword(StringUtil.md5(changePwdVo.getNewPassword()));
                return this.updateById(sysUser);
            } else {
                throw new ValidateException("输入的老密码不正确！");
            }
        } else {
            throw new FrameException(ResultCodeEnum.ERROR_NO_USER_INFO);
        }
    }


    @Override
    public List<SysOrg> selectOrgAdminOrgList() {
        if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            throw new ValidateException("您不是组织机构管理员不能执行该操作！");
        }
        List orgAdminOrgList = this.baseMapper.getOrgAdminOrgUuid(ShiroUtil.getUserUuid());
        if (orgAdminOrgList == null && orgAdminOrgList.size() < 1) {
            return null;
        }

        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysOrg::getOrgStatus, StatusEnum.NORMAL.getCode());
        wrapper.lambda().in(SysOrg::getUuid, orgAdminOrgList);

        wrapper.lambda().orderByAsc(SysOrg::getParentUuid, SysOrg::getOrgSort);

        return sysOrgMapper.selectList(wrapper);
    }

    @Override
    public List<UserRoleVo> selectUserRoleList(String userUuid) {
        List<String> userAssignedRoleList = null;
        //新增用户获取可分配的角色ID时，没有已分配的角色
        if (StringUtil.isNotEmpty(userUuid)) {
            //获取用户已分配的角色列表
            userAssignedRoleList = this.baseMapper.getUserRoleUuid(userUuid);
        }

        List<String> orgAdminRoleList = null;
        if (ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.ORG_ADMIN_USER)) {
            //获取组织机构管理员 可给用户分配的角色列表
            orgAdminRoleList = this.baseMapper.getOrgAdminRoleUuid(ShiroUtil.getUserUuid());
        }

        List<String> roleUuidList = new ArrayList<>();
        if (orgAdminRoleList != null && orgAdminRoleList.size() > 0) {
            roleUuidList.addAll(orgAdminRoleList);
        }

        if (userAssignedRoleList != null && userAssignedRoleList.size() > 0) {
            for (String uuid : userAssignedRoleList) {
                if (!roleUuidList.contains(uuid)) {
                    roleUuidList.add(uuid);
                }
            }
        }

        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getRoleStatus, StatusEnum.NORMAL.getCode());
        queryWrapper.lambda().ne(SysRole::getRoleCode, SysConstant.SYS_ADMIN_ROLE);
        queryWrapper.lambda().in(SysRole::getUuid, roleUuidList);

        List<UserRoleVo> userRoleList = new ArrayList<>();

        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        if (sysRoleList != null && sysRoleList.size() > 0) {
            for (SysRole sysRole : sysRoleList) {
                UserRoleVo userRole = new UserRoleVo();
                BeanUtils.copyProperties(sysRole, userRole);
                userRole.setRoleUuid(sysRole.getUuid());

                if (userAssignedRoleList != null && userAssignedRoleList.size() > 0) {
                    if (userAssignedRoleList.contains(userRole.getRoleUuid())) {
                        userRole.setCheckedFlag(FlagEnum.YES.getCode());
                    } else {
                        userRole.setCheckedFlag(FlagEnum.NO.getCode());
                    }
                } else {
                    userRole.setCheckedFlag(FlagEnum.NO.getCode());
                }
                if (orgAdminRoleList != null && orgAdminRoleList.size() > 0) {
                    if (orgAdminRoleList.contains(userRole.getRoleUuid())) {
                        userRole.setDisableFlag(FlagEnum.NO.getCode());
                    } else {
                        userRole.setDisableFlag(FlagEnum.YES.getCode());
                    }
                } else {
                    userRole.setDisableFlag(FlagEnum.YES.getCode());
                }
                userRoleList.add(userRole);
            }
        }
        return userRoleList;
    }

    @Override
    public boolean assignRole(AssignUserRoleVo assignRoleVo) {
        if (this.getById(assignRoleVo.getUserUuid()) == null) {
            throw new ValidateException("用户已不存在！");
        }
        //删除用户已分配的角色
        if (sysUserRoleService.deleteUserRole(assignRoleVo.getUserUuid())) {
            return saveUserRole(assignRoleVo.getUserUuid(), assignRoleVo.getRoleUuidList());
        }
        return true;
    }

    /**
     * 保存用户已分配的角色
     *
     * @param userUuid
     * @param roleUuidList
     * @return
     */
    private boolean saveUserRole(String userUuid, List<String> roleUuidList) {
        if (roleUuidList != null && roleUuidList.size() > 0) {
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            for (String roleUuid : roleUuidList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserUuid(userUuid);
                sysUserRole.setRoleUuid(roleUuid);
                sysUserRoleList.add(sysUserRole);
            }
            return sysUserRoleService.saveUserRole(sysUserRoleList);
        } else {
            return true;
        }
    }
    @Autowired
    private Environment env;

    @Override
    public List<SysMenu> getUserMenu(String userUuid) {
        List<UserMenu> menuList = userService.getUserMenu(userUuid);
        List<SysMenu> sysMenuList = new ArrayList<>();
        if (menuList != null && menuList.size() > 0) {
            for (UserMenu userMenu : menuList) {
                SysMenu sysMenu=new SysMenu();
                BeanUtils.copyProperties(userMenu,sysMenu);
                sysMenuList.add(sysMenu);
            }
        }
        return sysMenuList;
    }

    /**
     * 保存用户岗位
     *
     * @param userUuid
     * @param postUuidList
     * @return
     */
    private boolean saveUserPost(String userUuid, List<String> postUuidList) {
        if (postUuidList != null && postUuidList.size() > 0) {
            List<SysUserPost> sysUserPostList = new ArrayList<>();
            for (String postUuid : postUuidList) {
                SysUserPost sysUserPost = new SysUserPost();
                sysUserPost.setUserUuid(userUuid);
                sysUserPost.setPostUuid(postUuid);
                sysUserPostList.add(sysUserPost);
            }
            return sysUserPostService.saveUserPost(sysUserPostList);
        } else {
            return true;
        }
    }
}
