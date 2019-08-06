package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.*;
import com.jeeadmin.entity.SysUser;
import com.jeeadmin.entity.SysUserPost;
import com.jeeadmin.entity.SysUserRole;
import com.jeeadmin.mapper.SysUserMapper;
import com.jeeadmin.vo.user.AssignRoleVo;
import com.jeeadmin.vo.user.QueryUserVo;
import com.jeerigger.common.enums.UserStatusEnum;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.util.SysOrgUtil;
import com.jeerigger.module.sys.util.SysParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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
    private ISysOrgService sysOrgService;
    @Autowired
    private ISysUserPostService sysUserPostService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysOrgAdminOrgService sysOrgAdminOrgService;
    @Autowired
    private ISysOrgAdminRoleService sysOrgAdminRoleService;
    @Override
    public Page<SysUser> selectPage(PageHelper<QueryUserVo> pageHelper) {
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
            if (StringUtil.isNotEmpty(queryUserVo.getOrgUuid())) {
                queryWrapper.lambda().eq(SysUser::getOrgUuid, queryUserVo.getOrgUuid());
            }
            if (StringUtil.isNotEmpty(queryUserVo.getUserStatus())) {
                queryWrapper.lambda().eq(SysUser::getUserStatus, queryUserVo.getUserStatus());
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
    public SysUser getUserByUuid(String userUuid) {
        if (StringUtil.isEmpty(userUuid)) {
            throw new ValidateException("用户UUID不能为空！");
        }
        SysUser sysUser = this.getById(userUuid);
        if (sysUser != null && StringUtil.isNotEmpty(sysUser.getOrgUuid())) {
            sysUser.setOrgName(sysOrgService.getById(sysUser.getOrgUuid()).getOrgName());
        }
        List<String> postUuidList=new ArrayList<>();
        List<SysUserPost> userPostList=sysUserPostService.detailPostList(userUuid);
        if(userPostList!=null && userPostList.size()>0){
            for (SysUserPost sysUserPost:userPostList){
                postUuidList.add(sysUserPost.getPostUuid());
            }
        }
        sysUser.setPostUuidList(postUuidList);

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
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysUser::getUserNumber, sysUser.getUserNumber());
        if (StringUtil.isNotEmpty(sysUser.getUuid())) {
            queryWrapper.lambda().ne(SysUser::getUuid, sysUser.getUuid());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("员工编号已存在！");
        }
    }

    @Override
    public List<SysUser> detailUserList(String roleUuid) {
        return this.baseMapper.selectUserListByRoleUuid(roleUuid);
    }

    @Override
    public boolean saveUser(SysUser sysUser) {
        sysUser.setUserStatus(UserStatusEnum.NORMAL.getCode());
        sysUser.setMgrFlag(FlagEnum.NO.getCode());
        //验证数据
        ValidateUtil.validateObject(sysUser);
        //验证登录名
        validateLoginName(sysUser);
        //验证员工编号
        validateUserNumber(sysUser);
        if (sysOrgService.getById(sysUser.getOrgUuid()) == null) {
            throw new ValidateException("选择的组织机构代码已不存在！");
        }
        sysUser.setLoginPassword(StringUtil.md5(SysParamUtil.getInitPassword()));
        //保存用户信息
        boolean saveFlag = this.save(sysUser);
        if (saveFlag) {
            //保存岗位
            saveFlag=saveUserPost(sysUser.getUuid(),sysUser.getPostUuidList());
            if (saveFlag) {
                //保存角色
                saveFlag = saveUserRole(sysUser.getUuid(), sysUser.getRoleUuidList());
            }
        }
        return saveFlag;
    }

    @Override
    public boolean updateUser(SysUser sysUser) {
        if (this.getById(sysUser.getUuid()) == null) {
            throw new ValidateException("该用户不存在！");
        }
        //验证数据有效性
        ValidateUtil.validateObject(sysUser);
        //验证登录名
        validateLoginName(sysUser);
        //验证员工编号
        validateUserNumber(sysUser);

        //删除用户已分配的岗位
        if (sysUserPostService.deleteUserPost(sysUser.getUuid())) {
             saveUserPost(sysUser.getUuid(), sysUser.getPostUuidList());
        }

        //更新用户信息
        return this.updateById(sysUser);
    }

    @Override
    public boolean deleteUser(String userUuid) {
        if (StringUtil.isEmpty(userUuid)) {
            throw new ValidateException("用户UUID不能为空！");
        }
        //删除用户岗位信息
        sysUserPostService.deleteUserPost(userUuid);
        //删除用户角色关系表数据
        sysUserRoleService.deleteUserRole(userUuid);
        //机构管理员需删除机构管理员组织机构关系表
        sysOrgAdminOrgService.deleteOrgAdminOrg(userUuid);
        //机构管理员需删除机构管理员角色关系表
        sysOrgAdminRoleService.deleteOrgAdminRole(userUuid);
        //删除用户信息
        return this.removeById(userUuid);
    }


    @Override
    public boolean updateUserStatus(SysUser sysUser) {
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
    public boolean resetUserPwd(String userUuid) {
        if (this.getById(userUuid) == null) {
            throw new ValidateException("用户已不存在！");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUuid(userUuid);
        sysUser.setLoginPassword(StringUtil.md5(SysParamUtil.getInitPassword()));
        return this.updateById(sysUser);
    }

    @Override
    public boolean assignRole(AssignRoleVo assignRoleVo) {
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
}
