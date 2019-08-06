package com.jeeadmin.api;

import com.jeeadmin.entity.SysMenu;
import com.jeeadmin.vo.menu.QueryMenuVo;
import com.jeeadmin.vo.menu.SaveMenuSortVo;
import com.jeerigger.frame.base.service.BaseTreeService;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysMenuService extends BaseTreeService<SysMenu> {

    /**
     * 根据菜单ID获取下级菜单
     *
     * @param queryMenuVo
     * @return
     */
    List<SysMenu> selectChildMenu(QueryMenuVo queryMenuVo);

    /**
     * 查询所有菜单
     *
     * @param queryMenuVo
     * @return
     */
    List<SysMenu> selectMenuList(QueryMenuVo queryMenuVo);

    /**
     * 新增菜单
     *
     * @param sysMenu
     * @return
     */
    boolean saveMenu(SysMenu sysMenu);

    /**
     * 批量保存菜单排序
     *
     * @param menuSortVoList
     * @return
     */
    boolean saveMenuSort(List<SaveMenuSortVo> menuSortVoList);

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    boolean updateMenu(SysMenu sysMenu);

    /**
     * 删除菜单
     *
     * @param menuUuid
     * @return
     */
    boolean deleteMenu(String menuUuid);


    /**
     * 获取角色可分配菜单
     *
     * @return
     */
    List<SysMenu> getMenuList();
}
