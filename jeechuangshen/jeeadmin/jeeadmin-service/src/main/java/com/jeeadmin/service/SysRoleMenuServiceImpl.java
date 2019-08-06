package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeadmin.api.ISysMenuService;
import com.jeeadmin.api.ISysRoleMenuService;
import com.jeeadmin.entity.SysMenu;
import com.jeeadmin.entity.SysRoleMenu;
import com.jeeadmin.mapper.SysRoleMenuMapper;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
    @Autowired
    private ISysMenuService sysMenuService;

    @Override
    public boolean saveRoleMenu(List<SysRoleMenu> sysRoleMenuList) {
        return this.saveBatch(sysRoleMenuList);
    }

    @Override
    public boolean deleteRoleMenu(String roleUuid) {
        if (StringUtil.isNotEmpty(roleUuid)) {
            QueryWrapper<SysRoleMenu> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(SysRoleMenu::getRoleUuid, roleUuid);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public List<SysRoleMenu> detailMenuList(String roleUuid) {
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleMenu::getRoleUuid, roleUuid);
        List<SysRoleMenu> sysRoleMenusList = this.list(queryWrapper);
        List<SysRoleMenu> menuList = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenusList) {
            QueryWrapper<SysMenu> queryMenuWrapper = new QueryWrapper<>();
            queryMenuWrapper.lambda().eq(SysMenu::getParentUuid, sysRoleMenu.getMenuUuid());
            if (sysMenuService.count(queryMenuWrapper) < 1) {
                menuList.add(sysRoleMenu);
            }
        }
        return menuList;
    }
}
