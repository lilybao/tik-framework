package com.jeerigger.module.sys.entity;

import com.jeerigger.frame.base.model.BaseTreeModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserMenu extends BaseTreeModel<UserMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型（0:目录 1:菜单 2:权限）
     */
    private String menuType;

    /**
     * 菜单地址
     */
    private String menuHref;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单颜色
     */
    private String menuColor;

    /**
     * 菜单打开方式
     */
    private String menuTarget;

    /**
     * 显示顺序
     */
    private Integer menuSort;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 是否显示（0:隐藏 1:显示 ）
     */
    private String showFlag;
}
