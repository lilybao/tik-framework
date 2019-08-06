package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeadmin.api.ISysMenuService;
import com.jeeadmin.api.ISysRoleMenuService;
import com.jeeadmin.entity.SysMenu;
import com.jeeadmin.entity.SysRoleMenu;
import com.jeeadmin.mapper.SysMenuMapper;
import com.jeeadmin.vo.menu.QueryMenuVo;
import com.jeeadmin.vo.menu.SaveMenuSortVo;
import com.jeerigger.common.enums.SysCodeEnum;
import com.jeerigger.frame.base.service.impl.BaseTreeServiceImpl;
import com.jeerigger.frame.exception.ValidateException;
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
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysMenuServiceImpl extends BaseTreeServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> selectChildMenu(QueryMenuVo queryMenuVo) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        if (StringUtil.isEmpty(queryMenuVo.getMenuUuid())) {
            queryMenuVo.setMenuUuid("0");
        }
        wrapper.lambda().eq(SysMenu::getParentUuid, queryMenuVo.getMenuUuid());
        if(StringUtil.isNotEmpty(queryMenuVo.getSysCode())){
            wrapper.lambda().eq(SysMenu::getSysCode, queryMenuVo.getSysCode());
        }
        wrapper.lambda().orderByAsc(SysMenu::getParentUuid, SysMenu::getMenuSort);
        return getListMenu(wrapper);
    }

    @Override
    public List<SysMenu> selectMenuList(QueryMenuVo queryMenuVo) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(queryMenuVo.getMenuName())) {
            wrapper.lambda().like(SysMenu::getMenuName, queryMenuVo.getMenuName());
        }
        if(StringUtil.isNotEmpty(queryMenuVo.getSysCode())){
            wrapper.lambda().eq(SysMenu::getSysCode, queryMenuVo.getSysCode());
        }
        wrapper.lambda().orderByAsc(SysMenu::getParentUuid, SysMenu::getMenuSort);
        return getListMenu(wrapper);
    }

    private List<SysMenu> getListMenu(QueryWrapper<SysMenu> queryWrapper) {
        List<SysMenu> sysMenuList = this.list(queryWrapper);
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getMenuWeight() != null) {
                String menuWeightName = SysDictUtil.getDictLable(SysConstant.SYS_MENU_WEIGHT, sysMenu.getMenuWeight().toString());
                sysMenu.setMenuWeightName(menuWeightName);
            }
        }
        return sysMenuList;
    }

    @Override
    public boolean saveMenu(SysMenu sysMenu) {
        if (StringUtil.isEmpty(sysMenu.getParentUuid())) {
            sysMenu.setParentUuid("0");
        }
        if (StringUtil.isEmpty(sysMenu.getSysCode())) {
            sysMenu.setSysCode(SysCodeEnum.JEE_ADMIN_SYSTEM.getCode());
        }
        ValidateUtil.validateObject(sysMenu);
        //如果父节点不为空则判断父节点是否存在
        validateParentUuid(sysMenu.getParentUuid());
        //验证同级目录下是否已存在菜单名称
        validateMenuName(sysMenu);
        return this.save(sysMenu);
    }

    @Override
    public boolean saveMenuSort(List<SaveMenuSortVo> menuSortVoList) {
        List<SysMenu> menuList = new ArrayList<>();
        for (SaveMenuSortVo menuSortVo : menuSortVoList) {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setUuid(menuSortVo.getMenuUuid());
            sysMenu.setMenuSort(menuSortVo.getMenuSort());
            menuList.add(sysMenu);
        }
        return this.updateBatchById(menuList);
    }

    @Override
    public boolean updateMenu(SysMenu sysMenu) {
        if (StringUtil.isEmpty(sysMenu.getParentUuid())) {
            sysMenu.setParentUuid("0");
        }

        SysMenu oldSysMenu = this.getById(sysMenu.getUuid());
        if (oldSysMenu == null) {
            throw new ValidateException("该菜单不存在,不能进行更新！");
        }
        if (StringUtil.isEmpty(sysMenu.getSysCode())) {
            sysMenu.setSysCode(oldSysMenu.getSysCode());
        }
        ValidateUtil.validateObject(sysMenu);
        //如果父节点不为空则判断父节点是否存在
        validateParentUuid(sysMenu.getParentUuid());
        //验证同级目录下是否已存在菜单名称
        validateMenuName(sysMenu);
        if (this.updateById(sysMenu)) {
            if (!oldSysMenu.getSysCode().equals(sysMenu.getSysCode())) {
                List<SysMenu> menuList = new ArrayList<>();
                List<String> menuPkList = this.getChildrenPk(sysMenu.getUuid());
                menuPkList.addAll(this.getParentPk(sysMenu.getUuid()));
                for (String uuid : menuPkList) {
                    SysMenu menu = new SysMenu();
                    menu.setSysCode(sysMenu.getSysCode());
                    menu.setUuid(uuid);
                    menuList.add(menu);
                }
                this.updateBatchById(menuList);
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * 验证上级菜单是否存在
     *
     * @param menuUuid
     */
    private void validateParentUuid(String menuUuid) {
        if (StringUtil.isNotEmpty(menuUuid) && !menuUuid.equals("0")) {
            if (this.getById(menuUuid) == null) {
                throw new ValidateException("选择的上级菜单不存在！");
            }
        }
    }

    /**
     * 验证同一级下名称是否存在
     */
    private void validateMenuName(SysMenu sysMenu) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysMenu.getUuid())) {
            queryWrapper.lambda().ne(SysMenu::getUuid, sysMenu.getUuid());
        }
        queryWrapper.lambda().eq(SysMenu::getParentUuid, sysMenu.getParentUuid());
        queryWrapper.lambda().eq(SysMenu::getSysCode, sysMenu.getSysCode());
        queryWrapper.lambda().eq(SysMenu::getMenuName, sysMenu.getMenuName());
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("菜单名称已存在，请核实！");
        }
    }

    @Override
    public boolean deleteMenu(String menuUuid) {
        // 查询当前菜单的所有下级菜单
        if (StringUtil.isEmpty(menuUuid)) {
            throw new ValidateException("菜单UUID不能为空！");
        }
        List list = this.getChildrenPk(menuUuid);
        if (list != null && list.size() > 0) {
            throw new ValidateException("请先删除下级菜单！");
        }
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysRoleMenu::getMenuUuid, menuUuid);
        if (sysRoleMenuService.count(wrapper) > 0) {
            throw new ValidateException("该菜单已与角色绑定，请解除绑定后再删除!");
        }
        return this.removeById(menuUuid);
    }

    @Override
    public List<SysMenu> getMenuList() {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        //获取超级管理员权重以外的菜单
        wrapper.lambda().ne(SysMenu::getMenuWeight, SysConstant.MENU_WEIGHT_SUPER);
        wrapper.lambda().orderByAsc(SysMenu::getParentUuid, SysMenu::getMenuSort);
        return this.list(wrapper);
    }
}
