package com.jeerigger.module.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.api.ISysUserRoleService;
import com.jeerigger.module.sys.entity.SysUserRole;
import com.jeerigger.module.sys.mapper.SysUserPostMapper;
import com.jeerigger.module.sys.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 人员角色关系表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {


    @Override
    public boolean saveUserRole(List<SysUserRole> sysUserRoleList) {
        return this.saveBatch(sysUserRoleList);
    }

    @Override
    public boolean deleteUserRole(String userUuid) {
        QueryWrapper<SysUserRole> whereWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(userUuid)) {
            whereWrapper.lambda().eq(SysUserRole::getUserUuid, userUuid);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }
}
